package cn.chao.maven.annotation;
  
import cn.chao.maven.entity.AdminEntity;
import cn.chao.maven.entity.LogEntity;
import cn.chao.maven.service.LogService;
import cn.chao.maven.utils.MyUtil;
import cn.chao.maven.utils.WebUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;    
import org.aspectj.lang.annotation.*;    
import org.slf4j.Logger;    
import org.slf4j.LoggerFactory;    
import org.springframework.stereotype.Component;    
import org.springframework.web.context.request.RequestContextHolder;    
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;    
import javax.servlet.http.HttpServletRequest;    
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;

/**
 * 切点类   
 * @author Mr Du
 */
@Aspect    
@Component    
public class SystemLogAspect {
    //注入Service用于把日志保存数据库    
    @Resource
    private LogService logServiceImp;
    //本地异常日志记录对象    
    private static final Logger logger = LoggerFactory.getLogger(SystemLogAspect. class);
    
    //Controller层切点    
    @Pointcut("@annotation(SysLog)")
    public void controllerAspect() {
    }

    // 基本是数据类型
    private static String[] types = {"java.lang.Integer", "java.lang.Double",
            "java.lang.Float", "java.lang.Long", "java.lang.Short",
            "java.lang.Byte", "java.lang.Boolean", "java.lang.Char",
            "java.lang.String", "int", "double", "long", "short", "byte",
            "boolean", "char", "float"};
    /**  
     * 前置通知 用于拦截Controller层记录用户的操作  
     *  
     * @param joinPoint 切点  
     */    
    @Before("controllerAspect()")    
     public  void doBefore(JoinPoint joinPoint) {    
    
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
        HttpSession session = request.getSession();    
        //读取session中的用户    
        AdminEntity user = (AdminEntity)SecurityUtils.getSubject().getPrincipal();
        //请求的IP
        //String ip = request.getRemoteAddr();
        
        String requestURI=request.getRequestURI();
        
        String ip= WebUtils.getRemoteAddr(request);
        String method = joinPoint.getSignature().getDeclaringTypeName() + 
                "." + joinPoint.getSignature().getName();
        StringBuilder params = new StringBuilder();
        boolean b = true;
        if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {    
            for ( int i = 0; i < joinPoint.getArgs().length; i++) {    
//           	 params+=joinPoint.getArgs()[i]+";";
                // 获取对象类型
                System.out.println("当前参数："+ joinPoint.getArgs()[i]);
                String typeName = joinPoint.getArgs()[i].getClass().getTypeName();
                System.out.println("当前参数类型是："+typeName);
                // 判断类型。
                int a = 0;
                for (String t : types) {
                    if (!t.equals(typeName)) {
                        // 2 通过反射获取实体类属性
                        a++;
                    }
                }
                //
                if(a == types.length){
                    b = false;
                }
                //
                System.out.println("赋值操作");
                if(b) {
                    // 1 判断是否是基础类型
                    System.out.println("基本类型设置参数1");
                    params.append(joinPoint.getArgs()[i]).append("; ");
                } else {
                    // 2 通过反射获取实体类属性
                    System.out.println("基本类型设置参数2");
                    params.append(getFieldsValue(joinPoint.getArgs()[i]));
                }
           }    
       }    
         try {
            //*========控制台输出=========*//
            //System.out.println("=====前置通知开始=====");
            String operation=getControllerMethodDescription(joinPoint);
            String username=user.getUsername();
            //System.out.println("请求参数:" + params);
            LogEntity log=new LogEntity();
//            log.setCreateTime(MyUtil.getNowDateStr2());
            log.setCreateTime(new Date());
            log.setIp(ip);
            log.setOperation(operation);
            log.setParams(String.valueOf(params));
            log.setUsername(username);
            log.setMethod(requestURI);
			//*========保存数据库日志=========*//
            System.out.println(log);
             System.out.println(params);
            logServiceImp.insLog(log);
            //保存数据库
        }  catch (Exception e) {    
            //记录本地异常日志    
            logger.error("==前置通知异常==");    
            logger.error("异常信息:{}", e.getMessage());    
        }    
    }    
    
    /**  
     * 获取注解中对方法的描述信息 用于Controller层注解  
     *  
     * @param joinPoint 切点  
     * @return 方法描述  
     * @throws Exception  
     */    
     public  static String getControllerMethodDescription(JoinPoint joinPoint)  throws Exception {    
        String targetName = joinPoint.getTarget().getClass().getName();    
        String methodName = joinPoint.getSignature().getName();    
        Object[] arguments = joinPoint.getArgs();    
        Class targetClass = Class.forName(targetName);    
        Method[] methods = targetClass.getMethods();    
        String description = "";    
         for (Method method : methods) {    
             if (method.getName().equals(methodName)) {    
                Class[] clazzs = method.getParameterTypes();    
                 if (clazzs.length == arguments.length) {    
                    description = method.getAnnotation(SysLog. class).value();    
                     break;    
                }    
            }    
        }    
         return description;    
    }


    /**
     * 解析实体类，获取实体类中的属性
     * @param obj 实体类
     * @return
     */
    public static String getFieldsValue(Object obj) {
        //通过反射获取所有的字段，getFileds()获取public的修饰的字段
        //getDeclaredFields获取private protected public修饰的字段
        Field[] fields = obj.getClass().getDeclaredFields();
        String typeName = obj.getClass().getTypeName();
        for (String t : types) {
            if (t.equals(typeName)) {
                return "";
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Field f : fields) {
            //在反射时能访问私有变量
            f.setAccessible(true);
            try {
                for (String str : types) {
                    //这边会有问题，如果实体类里面继续包含实体类，这边就没法获取。
                    //其实，我们可以通递归的方式去处理实体类包含实体类的问题。
                    if (f.getType().getName().equals(str)) {
                        sb.append(f.getName() + " : " + f.get(obj) + ", ");
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        sb.append("}");
        return sb.toString();
    }
}    
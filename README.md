# start
一个 SpringBoot 项目的开端。
# MarkDown 的语法使用详情在这里查看。
# 上传内容摘要与日期（2019.07.01 始）
1. Spring Boot 新建完成后台的第一次提交，目前登录后可以访问 /hello，后期增加更多内容。  === 2019-07-10 20:27
2. 修改内容，检测当前上传的 Git 任务是否正确。 === 2019-07-10 20:33
3. 集成 Shiro ,添加了 Shiro 的全局配置，以及定义的 Realm 域，并设置到启动的 Config 中。    === 2019-07-10 21:55
4. 集成 Shiro，修改了其中的配置内容，使得 Spring Boot 集成 shiro 可以运行。    === 2019-07-10 22:31
5. 集成 MyBatis 。通过 @Select 等注解进行 CRUD 的操作，增加了一个 Modeule HPA ，方便新建带有 JPA 注解的实体类。具体的方法如下。  2019-07-11 18:28
6. 集成 JPA 。丢失 JPA 自动生成的注解实体类。补上内容。  === 2019-07-11  18:32
7. 引入自定义的注解，Controller、Service、ServiceImpl，还有就是验证码的引入。页面元素的配置以及css、js、html 的使用。 === 2019-07-13 00:37
8. 增加和修改了很多内容，配置文件中，部分采用了代码配置的方式，也有 application.properties 的内容，增加页面的处理能力，并未全部开放 Shiro的内容，页面部分、管理员管理、用户管理、SQL监控、ShiroTAG的使用，系统日志等修改完成。   === 2019-07-17  20:38
9. 增加了 Redis 的配置工具类，实现了部分测试的功能，新增了 Spring Boot 集成 WebMagic (爬虫)的内容，页面、后台、测试都有。  === 2019-07-27  23:25
10. 昨天上传内容的时候出现了问题，显示结果为 远程 Github 的内容没有收到更新，但是 idea 提交更新已经结束了。出现 detached HEAD 这个问题。解决步骤如下。这个版本上传了一个测试的内容。 === 2019-07-28 12:38
11. 通过 Create README.md 内容。 === 2019-07-28 13:31
12. 批量增加用户的功能，通过 Excel 执行导入功能。
13. 项目部署，通过 NGINX 反向代理 + Spring Session + Redis 。=== 2019年10月10日11:23:07
## 上传内容的详细设计
1. 新建 Spring Boot 的项目， https://start.spring.io/  添加项目依赖，actuator、jpa、web、devtools、spring-security 等内容。并新建南了 TestController 
2. 测试上传内容，并执行内容无误。Git 的 idea 的命令使用。（git commit && add等等。）git add 添加到要提交到队列中，git commit 中提交到本地仓库，git push 传到远程仓库。
3. Spring Boot 集成 Shiro 的内容：https://blog.csdn.net/Roobert_Chao/article/details/89891034 详细的过程文档。
### 3.1 早先的系统验证方式 cookie & session
**Cookie：** request.getCookie();
Cookie[] cookies = request.getCookies();
```java
public class CookieControl{
    public static void main(){
        // 设置 Cookie 中的信息内容
        Cookie userCookie=new Cookie("loginInfo",loginInfo); 
        userCookie.setMaxAge(30*24*60*60);   //存活期为一个月 30*24*60*60
        userCookie.setPath("/");
        response.addCookie(userCookie);
        
        // 获取 Cookie 中的信息内容
        Cookie[] cookies = request.getCookies();
        String sysUser = "";      // 字符串类型
        for (int i = 0; i <cookies.length ; i++) {
           if("Hm_VVNFUklE".equals(cookies[i].getName())){
              sysUser = cookies[i].getValue();
           }
        }
    }
}
```
**Session：** request.getSession();
HttpSession session = request.getSession();
```java
public class SessionControl{
    public static void main(String[] args){
          HttpSession session = request.getSession();
          SysUser sysUser = (SysUser) session.getAttribute("Hm_VVNFUklE");
    }
}
```
4. Spring Boot 配置文件的 Java 代码注册 Bean 。代替 XML 中注入 Bean 。
5. Spring Boot 配置 MyBatis 的内容。MyBatis 众所周知的是通过编写实体类，通过 JDBC 的连接操作数据库，一方面是通过 XML 配置内容，另一方面可以通过 @Select 等注解进行操作。在 Spring Boot 项目中集成工具 Beetl 中，也可以使用专门的sql 配置文件操作。
6. Spring Boot 集成 JPA ，注入依赖是一方面，JPA 注解类中还要使用 jpa 注解。通过新建【Module】-->【Java】-->【JavaEE Persistence】-->【Persistence】-->【右键 Project 】-->【Generate Persistence Mapping】-->【①By Database Schema】-->【②By Hibernate Mappings】-->【Insert Database Schema】
7. Spring Boot 项目从这里看是就比较完善了，经过这次之后便可以进行操作很多内容。三层内容划分的也比较明显（没有使用分布式的划分）。
### 7.1 项目中存有 AOP 切面保存日志的功能（执行带有指定 @SysLog 标签的 Controller 方法都要进行插入日志操作）
1. AOP 面向切面编程中需要以下内容方可实现：（Spring 最核心的两个功能就是 AOP（面向切面）和 IOC（控制反转））
① 在类上使用 @Component 注解 把切面类加入到IOC容器中。
② 在类上使用 @Aspect 注解 使之成为切面类。
```html
<!--// 增加这个内容来确定是否开启注解代理-->
<!--@EnableAspectJAutoProxy(proxyTargetClass=true)-->

<!--// 导入这一个 Spring Boot starter AOP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>2.1.4.RELEASE</version>
</dependency>

<!-- aspectjrt 和 aspectjweaver 是与 aspectj 相关的包,用来支持切面编程的； -->
<!-- aspectjrt 包是 aspectj 的 runtime 包；-->
<!-- aspectjweaver 是 aspectj 的织入包；-->
<!-- 导入两个 -->
<!-- aspectjrt -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>1.8.10</version>
</dependency>
<!-- aspectjweaver -->
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.9.4</version>
</dependency>
```
2. 在编写取参数的 SystemLogAspect.java 的切面类中，还可以进行优化。

8. 关闭了 Shiro 的拦截功能，使得全面访问可以执行。
9. 远程上传失败，测试上传。出现 idea 提交结束，但是远程 Github 账户上未更新，可能是上传到远程失败，本地上传到了暂存区成功，一直处在暂存区。
10. 在IDEA中用git提交代码时的detached HEAD问题。
```$xslt
第一次遇到这个问题，没有完全理解是因为什么，按照如下步骤操作内容：
1、在本地代码处右键，打开 git bash
2、查看所有分支：git branch
3、切换分支到master：git checkout master       由于我一直使用的是主分支，在执行到这一步之后呢，我的 idea 就可以正常的提交了。而且是提交出了问题的两次提交都可以进行了。
4、如果你有其他分支，就切换到原来的分支：git checkout +开发分支
5、后面就可以使用 idea 进行提交了
```
11. 通过 Github 客户端进行执行创建了一个 README.md 。
12. 添加一个批量增加用户的功能模块。（点击按钮上传 Excel 文件，上传成功，解析 Excel 中的内容。）
```html
Excel 中 cell 的内容可能是纯数字，可能是字符，可根据不同的方法来获取值。 
    对于纯数字，可用 getNumericCellValue()获取 ，一般使用的是 double 的类型。这里可能需要做一个 double 的强制转换 int 。下边有给出：：。
    对于字符，可用 getStringCellValue()获取 ，一般使用的是 String 的类型。
    也可用 setCellType(HSSFCell.CELL_TYPE_STRING) 统一将 cell 中的内容当做字符串 
    或者用 getCellType() 获取类型，再根据不同类型调用不同的方法来获取内容
```
### 12.1、使用 POI 进行 Excel 大数据量写入的功能
> 1. 使用 ThreadPoolExecutor、CountDownLatch。由于 POI 的 sheet 中使用 TreeMap 存储行数据集，所以对 Sheet 的行创建是非线程安全的，所以进行了同步操作。
> 2. POI 效率高，支持公式，宏，能够修饰单元格属性，支持字体、数字、日期操作。

```导入依赖
<!-- POI 效率高，支持公式，宏，能够修饰单元格属性，支持字体、数字、日期操作 -->
<!-- https://mvnrepository.com/artifact/org.apache.poi/poi -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi</artifactId>
    <version>3.17</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>3.17</version>
</dependency>
<!-- POI 效率高，支持公式，宏，能够修饰单元格属性，支持字体、数字、日期操作 -->
```

**（导入 Excel 数据）数据导入主要涉及三个步骤： 1、文件上传。 2、Excel解析。 3、数据插入。**

12.1 **文件上传 userList.html 页面添加批量添加用户按钮**
```html
<shiro:hasPermission name="user:user:save">
    <div class="layui-inline">
        <a class="layui-btn layui-btn-normal usersAdd_btn">批量添加用户</a>
    </div>
</shiro:hasPermission>
```

12.2 **userLists.js 中添加文件上传，读取 Excel 文件**
```javascript
layui.use(['upload','jquery','layer'],function(){
        var $ = layui.$,upload=layui.upload;
        //批量导入员工信息
        upload.render({
            elem: '.usersAdd_btn'
            ,url: 'http://localhost:8012/mq-sysadmin/sysuser/excel'
            ,accept: 'file' //普通文件      images（图片）、file（所有文件）、video（视频）、audio（音频）。
            ,exts: 'xls|xlsx'   // 允许上传文件的后缀是 xls 或者是 xlsx 的文件
            // ,acceptMime: 'image/jpg, image/png'（只显示 jpg 和 png 文件，所以上传的文件是 jpg 和 png 的） 
            ,before: function(obj){ // obj 参数包含的信息。
                layer.load(); // 打开上传 loading。上传过程中，上传按钮会有loading显示，正在上传时，文件上传控件不可用，上传成功或者失败之后才可用。
            }
            ,choose: function(obj){     // 选择文件时执行
                obj.preview(function(index, file, result){
                    console.log(index);     //  得到文件索引
                    console.log(file);      //  得到文件对象
                    console.log(result);    //  得到文件base64编码，比如图片
                    // obj.resetFile(index, file, '123.jpg'); //重命名文件名，layui 2.3.0 开始新增
                    // 这里还可以做一些 append 文件列表 DOM 的操作
                    // obj.upload(index, file); //对上传失败的单个文件重新上传，一般在某个事件中使用
                    // delete files[index]; //删除列表中对应的文件，一般在某个事件中使用
                });
            }
            ,done: function(res){
                console.log(res);   
                layer.closeAll('loading'); // 关闭 loading
                layer.alert("导入成功！");
            }
            ,error: function(){
                layer.closeAll('loading'); // 关闭 loading
                layer.msg('请求异常');
            }
        });
    });
```

12.3 **SpringMVC 中接收上传文件即可**

## 注意：
1. 生产上严禁使用 System.out 输出（ 快捷键 sout ），性能太低，原因是System.out输出会导致线程等待（同步），而使用 Logger 输出线程不等待日志的输出（异步），而继续执行。本文使用的是 Lombok 的插件，引入 @Log4j 即可使用 log.debug()等内容。
2. MySql 中的 ‘utf-8’编码只支持最大 3 个字节每字符。真正的大家正在使用的是 UTF-8 编码是应该能支持 4 字节每个字符。2010 年之后，增加了一个变通的方法：新的字符集 ‘utf8mb4’才是真正意义上的 "UTF-8"。
3. Ajax 请求中 ContentType 与 DataType 的区别（可以到jQuery官网查询）：
  * 3.1、 contentType：告诉服务器，我要发什么类型的数据给你。
  * 3.2、 dataType：告诉服务器，我要想什么类型的数据，返回给我啥类型，如果没有指定，那么会自动推断是返回 XML，还是JSON，还是script，还是String。
## 插件（plugins）的使用
### 1、Layui 插件。更多内容详见官方文档：https://www.layui.com/doc/

* 1.1、**表单内容以及动态刷新**

* 1.1.1、**使用 layui.use 监听 select 事件。**

* **HTML表单页面：**
~~~~
    <select lay-filter="demo" lay-verify="required">   lay-filter 表示监听，lay-verify 表示验证。
~~~~
* **JavaScript语法：**

```javascript
layui.use(['layer', 'jquery', 'form'], function () {
    var layer = layui.layer,
    $ = layui.jquery,form = layui.form;
    form.on('select(demo)', function(data){
        if(data.value == 1){
            $("#search").attr("disabled","true");   // attr 添加属性内容
            form.render('select');  // 刷新表单
        }else{
            $("#search").removeAttr("disabled");    // removeAttr 去除属性内容
            form.render('select');  // 刷新表单
        }
    });
});
```

* 1.1.2、**使用 layui.use 监听 radio 事件。**

* **HTML表单页面：**

```html
<div class="layui-form-item">       
    <label class="layui-form-label">角色类型</label>
    <div class="layui-input-block">
        <input type="radio" id="system" name="roleType" lay-filter="roleType" value="0" title="系统管理员">          <!-- lay-filter 表示监听内容 -->
        <input type="radio" id="qiye" name="roleType"  lay-filter="roleType" value="10" title="企业管理员" checked="" >
    </div>
</div>
```
* **JavaScript语法：**

```javascript
form.on("radio(roleType)", function (data) {        //  radio 的设置监听 lay-filter='roleType'
    var sex = data.value;
    if (this.value == '0') {
        $("#company").attr("disabled","true");
        form.render('select');
    } else if (this.value == '10') {
        $("#company").removeAttr("disabled");
        form.render('select');
    }
});
```

* 1.1.3、**只引用 JQuery 的话，监听单选按钮改变事件如下。**

* **HTML表单页面：**

```html
    <input type="radio" name="sex" checked="checked" value="1">男
    <input type="radio" name="sex" value="2">女
```

* **JavaScript语法：**

```javascript
$(document).ready(function() { 
    $('input[type=radio][name=sex]').change(function() {
        if (this.value == '1') { 
            alert("性别内容 1"); 
        } else if (this.value == '2') { 
            alert("性别内容 2"); 
        } 
    }); 
});
```

* 1.1.4、**表格渲染的新写法（table.render）**

- 表格的分页

```html
    page: {
      layout: ['limit', 'count', 'prev', 'page', 'next', 'skip','refresh'] //自定义分页布局
      //,curr: 5 //设定初始在第 5 页
      ,groups: 4 //只显示 1 个连续页码
      ,first: true
      ,last: true
    }
```
        
- 表格的高度

```javascript
    height: 'full-100' /* 表格有宽度之后，可设表格的高度最大化当前窗口 */
```
  
- 表格的宽度

```html
    cellMinWidth:80 /* 设置每一个元素的最小宽度为 80  */
```

- 表格的表头中加入 templet 方法函数

```html
    {
        field: 'canshu',
        title: '表头的标题',
        sort: true, // 允许排序
        align: 'center',    // 内容居中
        templet: function (res) {   // 设置参数方法。
          return fangfa(res.canshu)     // 可以通过 res.canshu 取出参数内容。
        }
    }
```

* 1.1.5、**弹出层（layer.open）的使用**

- 弹出层遮盖住背景
    shadeClose: true, //点击遮罩关闭

- 弹出层类型可通过官方选择。 
    type: 2,    可传入的值有：0（信息框，默认）1（页面层）2（iframe层）3（加载层）4（tips层）。
    skin: 'layui-layer-rim', // 加上边框    目前layer内置的skin有：layui-layer-lanlayui-layer-molv
    
- 弹出层的按钮 yes(btn1)、btn2、btn3
- 弹出层成功的回调：
    
```html
    success: function (layero, index) {     /* layer.open 成功后的回调函数 */
        var body = layer.getChildFrame('body', index);
        var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
        // console.log(body.html()) //得到iframe页的body内容
        // 初始化表单数据的值
        body.find(".address2").val(address2);
    }

    success:function(layero,index){
        var body = layer.getChildFrame('body', index);  // 获取当前弹窗页。
        // 初始化弹窗层表单的值
        body.contents().find("#id").val(data.id);
        body.contents().find("#account").val(data.nickName);
        body.contents().find("#expired").val(data.expiredTime);
        body.contents().find("#lastpay").val(data.lastPayTime);
    }
    
    var index = parent.layer.getFrameIndex(window.name); // 先得到当前iframe层的索引
    
    parent.layer.close(index); // 执行关闭
    parent.$(".refresh").click();// 通过触发页面局部刷新按钮来刷新父页面更新修改后的表格数据
 
```   
##### layer弹出层的关闭问题——在执行完毕后关闭当前弹出层
1. layer.close(index) - 关闭特定层
```javascript
    // 当你想关闭当前页的某个层时
    var index = layer.open();
    var index = layer.alert();
    var index = layer.load();
    var index = layer.tips();
    // 正如你看到的，每一种弹层调用方式，都会返回一个index
    layer.close(index); //此时你只需要把获得的index，轻轻地赋予layer.close即可
    
    // 如果你想关闭最新弹出的层，直接获取layer.index即可
    layer.close(layer.index); //它获取的始终是最新弹出的某个层，值是由layer内部动态递增计算的
    
    // 当你在 iframe 页面关闭自身时
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
```
2. layer.closeAll(type) - 关闭所有层
```javascript
    layer.closeAll();               // 关闭所有层
    layer.closeAll('dialog');       // 关闭信息框
    layer.closeAll('page');         // 关闭所有页面层
    layer.closeAll('iframe');       // 关闭所有的iframe层
    layer.closeAll('loading');      // 关闭加载层
```
3. 关闭弹出层之后刷新父页面
```javascript
    window.parent.location.reload();    //  刷新父页面
    parent.layer.close(index);          //  关闭弹出层
    // 可以在父页面设置 render() 渲染方法，通过 子页面调用父页面的方法 重新渲染父页面。
```

### 2、**HTML 中 `&nbsp; &ensp; &emsp; &thinsp;`等 6 种空白空格的区别。**
```html
&nbsp; 不换行空格，它是按下 Space 键产生的空格。
&ensp; 半角空格，占据的宽度正好是 1/2 个中文宽度。
&emsp; 全角空格，占据的宽度正好是 1 个中文宽度。
&thinsp; 窄空格，是 emsp 的六分之一。
&zwnj; 零宽不连字，HTML字符值引用为： &#8204; 。
&zwj; 零宽连字，
```
**浏览器还会把以下字符当作空白进行解析：空格（`&#x0020;`）、制表位（`&#x0009;`）、换行（`&#x000A;`）和回车（`&#x000D;`）还有（`&#12288;`）**

### 3、**Java 从 request 中获取请求参数。**
```java
public class requestControl{
    public static void main(String[] args){
      // 1. 获取请求方式  Get Post
      request.getMethod(); // get和post都可用，
      // 2. 获取请求类型 application/json ，multipart/form-data， application/xml 等
      request.getContentType();
      // 3. 获取所有参数 Key  不适用 contentType 为 multipart/form-data 。 multipart/form-data 请求文件资源。
      request.getParameterNames(); 
      // 4. 获取参数值 Value  同样在文件上传中的内容不适用。
      request.getParameter("test"); 
      // 5. 获取参数的集合  以 Map 集合的形式存放参数内容，文件上传时不适用。
      request.getParameterMap();
      // 6. 获取文本流   application/json、xml、multipart/form-data 文本流或者大文件形式提交的请求等形式的报文
      request.getInputStream(); 
      // 7. 获取URL
      request.getRequestURL();
      // 8. 获取参数列表 。
      // 8.1、仅限 GET 。客户端发送 http://localhost/testServlet?a=b&c=d&e=f , 通过 request.getQueryString() 得到的是 a=b&c=d&e=f 。  
      request.getQueryString();
      // 8.2、当编码方式是(application/x- www-form-urlencoded)时才能使用
      request.getParameter();
      // 8.3、浏览器编码方式("multipart/form-data")时使用
      request.getInputStream();
      // 8.4、浏览器编码方式("multipart/form-data")时使用
      request.getReader();
      /*
        ① ：当 form 表单内容采用 enctype=application/x-www-form-urlencoded 编码时，先通过调用 request.getParameter() 方法得到参数后,
            再调用 request.getInputStream() 或 request.getReader() 已经得不到流中的内容。
        因为在调用 request.getParameter() 时系统可能对表单中提交的数据以流的形式读了一次,反之亦然。
      
        ② ：当 form 表单内容采用 enctype=multipart/form-data 编码时，即使先调用 request.getParameter() 也得不到数据，
            所以这时调用 request.getParameter() 方法对 request.getInputStream() 或 request.getReader() 没有冲突，
        即使已经调用了 request.getParameter() 方法也可以通过调用 request.getInputStream() 或 request.getReader() 得到表单中的数据，
        而 request.getInputStream() 和 request.getReader() 在同一个响应中是不能混合使用的,如果混合使用就会抛异常。
      */
    }
}
```

### 4. **String 非空、Integer 非空、List 非空、对象 非空**
```
public class notEmpty(){
    public static void main(String[] args){
        // 1、String的非空判断。
        StringUtils.isNotEmpty(String str);
        // 2、Integer的非空判断。
        null != Integer ;
        // 3、list的大小判断。
        list.size() == 0
        // 4、对象的非空判断
        null ！= object      
    }
}
```

### 5. **JQuery 添加元素，详情查看：** https://blog.csdn.net/Roobert_Chao/article/details/88843957
```html
5.1、append()    - <在被选元素的结尾插入内容>结尾靠近结束标签</在被选元素的结尾插入内容>
5.2、prepend()   - <在被选元素的开头插入内容>开头靠近开始标签</在被选元素的开头插入内容>
5.3、after()     - <在被选元素之后插入内容></在被选元素之后插入内容>在结束的位置加入
5.4、before()    - 在开始的位置加入<在被选元素之前插入内容></在被选元素之前插入内容>
```

### 6. **jQuery 的 Ajax 中 get()、post()的跨域方法**

* 6.1、get() 请求中使用 Jsonp
```javascript
//  ajax 跨域同时使用 jsonp 只能支持 get 方式请求
$.ajax({
    type: "get",
    url: "请求地址",
    dataType: 'jsonp', //   jsonp 进行跨域请求 只支持 get
    data:{          //  这里填写是传给服务端的数据 可传可不传 数据必须是 json 格式。
        "a":"b",
        "c":"d"
    },
    success: function(data) {   // 成功回调
        console.log(data);
    },
    error: function(xhr, type) { // 失败回调
    }
});
```
* 6.2、post() 请求
```javascript
$.ajax({
    type:"post",
    url:"请求地址",
    data:{  //这里填写是传给服务端的数据 可传可不传 数据必须是json格式
        "a":"b",
        "c":"d"
    },
    dataType:'json',  // 一定是json，不能在使用jsonp
    crossDomain: true,  //  一定要加
    success:function(result){
        console.log(result);
    },
    error:function(result){
        console.log(result);
    }
});
```
```html
    // 服务器端需要设置如下内容。
      Access-Control-Allow-Origin: *
      Access-Control-Allow-Methods: POST
      Access-Control-Max-Age: 1000
```

### 7. JavaScript 中通过 jQuery 设置生成随机字符串。
```javascript
// 获取长度为len的随机字符串
var pwdlength = function _getRandomString(len) {
    len = len || 32;
    var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678'; // 默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1
    var maxPos = $chars.length;
    var pwd = '';
    for (i = 0; i < len; i++) {
        pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
    }
    return pwd;
}
// 这里通过 pwdlength(32) 即可得到返回值内容。 （） JavaScript 中表示执行此方法。
```

### 8. 在 java 中把 double类型 转成 int类型。
```java
public class doubleSwapInt{
    public static void main(String[] args){
      double a = 35;
      double b = 20;    // a / b = 7 / 5 
      double c = a / b; // 1.75
      System.out.println(c);    //  1.75
      System.out.println(Math.ceil(c));     // 2.0 向上取整
      System.out.println(Math.floor(c));    // 1.0 向下取整
      System.out.println(Math.round(c));    // 2.0 四舍五入
      
//      double d = 5.5;
//      int i = (int) d;
//      System.out.println(i);    //  5
    }
}
```

### 9.**jQuery 中的 $(function(){})、$(document).ready(function(){})**
> jQuery 的模块可以分为 3 部分：入口模块、底层支持模块和功能模块。
> 1. $(function(){}) 是 $(document).ready(function(){}) 的简称。
> > 1. 简单理解是(function($){...})(jQuery)用来定义一些需要预先定义好的函数。
> > 2. $(function(){ })则是用来在 DOM 加载完成之后运行\执行那些预行定义好的函数。
> 2. (function(arg){})表示一个匿名函数。(function(arg){...})(param) 这就相当于定义了一个参数为arg的匿名函数，并且将param作为参数来调用这个匿名函数
> > (function($){...})(jQuery)  只在形参使用$，是为了不与其他库冲突，所以实参用jQuery。
> 3. function(arg){...}定义了一个参数为arg的匿名函数，然后使用(function(arg){...})(param)来调用这个匿名函数。其中param是传入这个匿名函数的参数。

```text
var fn = function($){....};
fn(jQuery);
<===>
(function($){...})(jQuery) 
```
#### jQuery.extends(); 函数用于将一个或多个对象的内容合并到目标对象。
1. 如果只为 $.extend() 指定了一个参数，则意味着参数 target 被省略。此时，target 就是 jQuery 对象本身。通过这种方式，我们可以为全局对象 jQuery 添加新的函数。
2. 如果多个对象具有相同的属性，则后者会覆盖前者的属性值。
```text
$.extend( [deep ], target, object1 [, objectN ] )
// 1. deep	可选。 Boolean类型 指示是否深度合并对象，默认为false。如果该值为true，且多个对象的某个同名属性也都是对象，则该"属性对象"的属性也将进行合并。
// 2. target	Object类型 目标对象，其他对象的成员属性将被附加到该对象上。
// 3. object1	可选。 Object类型 第一个被合并的对象。
// 4. objectN	可选。 Object类型 第N个被合并的对象。
```
#### jQuery 中的 $.fn 的用法
1. $.fn.method()=function(){} 的调用把方法扩展到了对象的 prototype 上，所以实例化一个 jQuery 对象的时候，它就具有了这些方法。
```text
$.fn.blueBorder = function(){
 this.each(function(){
  $(this).css("border","solid blue 2px");
 });
 return this;
};
$.fn.blueText = function(){
 this.each(function(){
  $(this).css("color","blue");
 });
 return this;
};
```
由于有 return this,所以支持链式，在调用的时候可以这样写：$('.blue').blueBorder().blueText();
2. $.fn.extend({}) 是对$.fn.method()=function(){}的扩展，它可以定义多个方法：
```text
$.fn.extend({
    a: function() { },
    b: function() { }
});

等效于(<===>)：

$.fn.a = function() { };
$.fn.b = function() { };
```
3. $.extend({}) ，为 jQuery 类添加静态方法（jQuery 添加静态的方法）。
```text
$.extend({
    abc: function(){
        alert('abc');
    }
});
```
使用方式： $.abc()。
4. 

## svn、git 等版本控制
### svn 版本控制
1. 添加【右键项目，选择TortoiseSVN 】-->【选择add选项】
2. 提交【右键项目文件，选择svn commit】-->【勾选要提交的文件】-->【ok】
3. 删除【右键，选择SVN管理软件】-->【Repo-brower 版本浏览器】-->【点击右键，选择删除】-->【删除的时候需要提交svn操作日志（无用文件夹删除）。】

### git 版本控制
1. Git 命令执行版本控制：https://blog.csdn.net/Roobert_Chao/article/details/86618599
2. idea 中使用 Git 操作：【VCS】-->【Checkout from Version Control】-->【Git】(项目获得版本控制，执行 git 命令)
#### 2.1. 下方有版本控制

## 项目中的问题。
### 一、编码问题 Code Problem
1. **Tomcat 的编码**
tomcat 默认的编码方式就是 UTF-8 <===> server.tomcat.uri-encoding=UTF-8
2. **项目编码**
【File】-->【Settins】-->【Editor】-->【File Encoding】-->【修改以下内容：[Global Encoding:UTF-8]、[Project Encoding:UTF-8]、[Default encoding for properties files:UTF-8]、[Transparent native-to-ascii conversion]】
3. **修改项目编码**
【application.properties 增加配置文件内容】
```properties
spring.http.encoding.charset=UTF-8
spring.http.encoding.force=true
spring.http.encoding.enabled=true
```
### 二、Dao 层关联数据查询 JOIN ON / JOIN ，左连接，根据左边的值为准。
1. SELECT * FROM test.contract a JOIN test.customer b ON a.Num = b.Num2 JOIN test.customer3 c ON a.num = c.num3;
    
 ```html
    1.1、简述分析：查询 test 数据库中 contract 表的所有内容，并将表 contract 取名为 a ，但是需要加入 customer 表的所有内容，并将表 customer 取名为 b，
        要求 a 表的 Num 和 b 表的 Num2 相等，同时又需要加入 customer3 表中的所有内容，并将 customer3 表命名为 c ，要求 a 的 num 和 c 的 num3 相等。 
    1.2、多表连接表的连接顺序从左往右
 ```
 
2. MyBatis 现在一改之前的做法，不在使用 逆向工程，生成快捷的 mapper 与 dao 。`
##### dao 层传递参数的问题。
```text
1、可以通过仿照之前使用逆向工程生成的 mapper 与 dao 层直接模仿者去写。 
    有一点很是重要 @Param 的参数十分重要。即便是 通过逆向工程生成的代码中，也是有很大比重的。
2、返回值的内容设置。①、resultType ②、resultMap 的使用也是非常重要的。
3、dao 层传递参数接收到 xml 文件中，执行对应的 sql 语句。
    我们可以从逆向工程的配置文件中读出内容看出：大多数方法的参数是一个参数并且不是简单的基本类型就是就是自定义的实体类。
    那么 ? 多个参数，传递不同的类型要如何去操作呢。
    其实，在逆向工程当中， 更新方法的参数就是两个参数的。而且参数类型也是不尽相同的，我们可以参照这个方法“举一反三”。
    java 接口（interface）文件 ：int updateByExample(@Param("record") LogEntity record, @Param("example") LogEntityExample example);
    xml 映射（mapper）文件：
          <update id="updateByExample" parameterType="map">
            update tb_log           更新表
            set id = #{record.id,jdbcType=BIGINT},      通过实体类取出参数 ，通过 jdbcType 设置参数类型，对应的数据库中额类型
              username = #{record.username,jdbcType=VARCHAR},
              operation = #{record.operation,jdbcType=VARCHAR},
              method = #{record.method,jdbcType=VARCHAR},
              params = #{record.params,jdbcType=VARCHAR},
              ip = #{record.ip,jdbcType=VARCHAR},
              create_time = #{record.createTime,jdbcType=TIMESTAMP}
            <if test="_parameter != null">
              <include refid="Update_By_Example_Where_Clause" />    // 这个位置引入了 sql 片段，下面是这个 sql 片段的全部内容。
            </if>
          </update>
                        // 具体的使用方法，还需要结合 xxxExample 的类，我觉的通过 update 更新的这个方法能够说明如何使用参数了。
          <sql id="Update_By_Example_Where_Clause">
              <where>
                <foreach collection="example.oredCriteria" item="criteria" separator="or">  // foreach 循环语句
                  <if test="criteria.valid">    
                    <trim prefix="(" prefixOverrides="and" suffix=")">      // 通过 trim 标签设置拼接 sql 的前缀后缀以及间断点。
                      <foreach collection="criteria.criteria" item="criterion">
                        <choose>
                          <when test="criterion.noValue">
                            and ${criterion.condition}
                          </when>
                          <when test="criterion.singleValue">
                            and ${criterion.condition} #{criterion.value}
                          </when>
                          <when test="criterion.betweenValue">
                            and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}
                          </when>
                          <when test="criterion.listValue">
                            and ${criterion.condition}
                            <foreach close=")" collection="criterion.value" item="listItem" open="(" separator=",">
                              #{listItem}
                            </foreach>
                          </when>
                        </choose>
                      </foreach>
                    </trim>
                  </if>
                </foreach>
              </where>
           </sql>
       除去 通过 实体类与简单类型（例如：java.lang.String ）还可以直接使用 map 和 list ，使用的时候要注意参数的位置与个数。      
``` 

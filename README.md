# start
一个 SpringBoot 项目的开端。
# MarkDown 的语法使用详情在这里查看。
# 上传内容摘要与日期（2019.07.01 始）
1. Spring Boot 新建完成后台的第一次提交，目前登录后可以访问 /hello，后期增加更多内容。  === 2019-07-10 20:27
2. 修改内容，检测当前上传的 Git 任务是否正确。 === 2019-07-10 20:33
3. 集成 shiro ,添加了 Shiro 的全局配置，以及定义的 Realm 域，并设置到启动的 Config 中。    === 2019-07-10 21:55
4. 集成 shiro，修改了其中的配置内容，使得 Spring Boot 集成 shiro 可以运行。    === 2019-07-10 22:31
5. 集成 MyBatis 。通过 @Select 等注解进行 CRUD 的操作，增加了一个 Modeule HPA ，方便新建带有 JPA 注解的实体类。具体的方法如下。  2019-07-11 18:28
6. 集成 JPA 。丢失 JPA 自动生成的注解实体类。补上内容。  === 2019-07-11  18:32
7. 引入自定义的注解，Controller、Service、ServiceImpl，还有就是验证码的引入。页面元素的配置以及css、js、html 的使用。 === 2019-07-13 00:37
8. 增加和修改了很多内容，配置文件中，部分采用了代码配置的方式，也有 application.properties 的内容，增加页面的处理能力，并未全部开放 Shiro的内容，页面部分、管理员管理、用户管理、SQL监控、ShiroTAG的使用，系统日志等修改完成。   === 2019-07-17  20:38
9. 增加了 Redis 的配置工具类，实现了部分测试的功能，新增了 Spring Boot 集成 WebMagic (爬虫)的内容，页面、后台、测试都有。  === 2019-07-27  23:25
10. 昨天上传内容的时候出现了问题，显示结果为 远程 Github 的内容没有收到更新，但是 idea 提交更新已经结束了。出现 detached HEAD 这个问题。解决步骤如下。这个版本上传了一个测试的内容。 === 2019-07-28 12:38
11. 通过 Create README.md 内容。 === 2019-07-28 13:31
## 上传内容的详细设计
1. 新建 Spring Boot 的项目， https://start.spring.io/  添加项目依赖，actuator、jpa、web、devtools、spring-security 等内容。并新建南了 TestController 
2. 测试上传内容，并执行内容无误。Git 的 idea 的命令使用。（git commit && add等等。）git add 添加到要提交到队列中，git commit 中提交到本地仓库，git push 传到远程仓库。
3. Spring Boot 集成 Shiro 的内容：https://blog.csdn.net/Roobert_Chao/article/details/89891034 详细的过程文档。
4. Spring Boot 配置文件的 Java 代码注册 Bean 。代替 XML 中注入 Bean 。
5. Spring Boot 配置 MyBatis 的内容。MyBatis 众所周知的是通过编写实体类，通过 JDBC 的连接操作数据库，一方面是通过 XML 配置内容，另一方面可以通过 @Select 等注解进行操作。在 Spring Boot 项目中集成工具 Beetl 中，也可以使用专门的sql 配置文件操作。
6. Spring Boot 集成 JPA ，注入依赖是一方面，JPA 注解类中还要使用 jpa 注解。通过新建【Module】-->【Java】-->【JavaEE Persistence】-->【Persistence】-->【右键 Project 】-->【Generate Persistence Mapping】-->【①By Database Schema】-->【②By Hibernate Mappings】-->【Insert Database Schema】
7. Spring Boot 项目从这里看是就比较完善了，经过这次之后便可以进行操作很多内容。三层内容划分的也比较明显（没有使用分布式的划分）。
### 7.1 项目中存有 AOP 切面保存日志的功能（执行带有指定 @SysLog 标签的 Controller 方法都要进行插入日志操作）
1. AOP 面向切面编程中需要以下内容方可实现：（Spring 最核心的两个功能就是 AOP（面向切面）和 IOC（控制反转））
① 在类上使用 @Component 注解 把切面类加入到IOC容器中。
② 在类上使用 @Aspect 注解 使之成为切面类。
```$xml
// 增加这个内容来确定是否开启注解代理
@EnableAspectJAutoProxy(proxyTargetClass=true)

<!--// 导入这一个 Spring Boot starter AOP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>2.1.4.RELEASE</version>
</dependency>

aspectjrt 和 aspectjweaver 是与 aspectj 相关的包,用来支持切面编程的； 
aspectjrt 包是 aspectj 的 runtime 包；
aspectjweaver 是 aspectj 的织入包；
// 导入两个 
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
11. 通过 Github 客户端进行执行创建了一个
## 注意：
1. 生产上严禁使用 System.out 输出（ 快捷键 sout ），性能太低，原因是System.out输出会导致线程等待（同步），而使用 Logger 输出线程不等待日志的输出（异步），而继续执行。本文使用的是 Lombok 的插件，引入 @Log4j 即可使用 log.debug()等内容。
2. 
## 插件（plugins）的使用
1. Layui 插件。更多内容详见官方文档：https://www.layui.com/doc/
`1.1、`**表单内容以及动态刷新**
`1.1.1、`使用 layui.use 监听 select 事件。
**HTML表单页面：**<select lay-filter="demo" lay-verify="required">   lay-filter 表示监听，lay-verify 表示验证。
**JavaScript语法：**
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
`1.1.2、`使用 layui.use 监听 radio 事件。
**HTML表单页面：**
```html
<div class="layui-form-item">       
    <label class="layui-form-label">角色类型</label>
    <div class="layui-input-block">
        <input type="radio" id="system" name="roleType" lay-filter="roleType" value="0" title="系统管理员">          <!-- lay-filter 表示监听内容 -->
        <input type="radio" id="qiye" name="roleType"  lay-filter="roleType" value="10" title="企业管理员" checked="" >
    </div>
</div>
```
**JavaScript语法：**
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
`1.1.3、`只引用 JQuery 的话，监听单选按钮改变事件如下。
**HTML表单页面：**
```html
<input type="radio" name="sex" checked="checked" value="1">男
<input type="radio" name="sex" value="2">女
```
**JavaScript语法：**
```javascript
$(document).ready(function() { 
    $('input[type=radio][name=sex]').change(function() {
        if (this.value == '1') { 
            alert("这是个男孩"); 
        } else if (this.value == '2') { 
            alert("这是个女孩"); 
        } 
    }); 
});
```
2. HTML 中 `&nbsp; &ensp; &emsp; &thinsp;`等 6 种空白空格的区别。
`&nbsp; 不换行空格，它是按下 Space 键产生的空格。` 
`&ensp; 半角空格，占据的宽度正好是 1/2 个中文宽度。`
`&emsp; 全角空格，占据的宽度正好是 1 个中文宽度。`
`&thinsp; 窄空格，是 emsp 的六分之一。`
`&zwnj; 零宽不连字，HTML字符值引用为： &#8204; 。`
`&zwj; 零宽连字，`
**浏览器还会把以下字符当作空白进行解析：空格（`&#x0020;`）、制表位（`&#x0009;`）、换行（`&#x000A;`）和回车（`&#x000D;`）还有（`&#12288;`）**
## svn、git 等版本控制
### svn 版本控制
1. 【右键项目，选择TortoiseSVN 】-->【选择add选项】
2. 【右键项目文件，选择svn commit】-->【】
### git 版本控制
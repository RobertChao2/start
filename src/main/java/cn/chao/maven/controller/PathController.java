package cn.chao.maven.controller;

import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log
public class PathController {

/*    @RequestMapping("{path}")
    public String paramPath(@PathVariable("path") String path){
        log.fine("PathController  ===》 当前访问的路径 Path：" + path);
        return path;
    }*/

    @RequestMapping({"/login","/"})
    public String index_1(){
        return "login.html";
    }

    @RequestMapping({"index"})
    public String index_2(){
        return "index.html";
    }

    @RequestMapping({"page/shiroTag"})
    public String shiroTAG(){
        return "page/shiroTag.html";
    }

    @RequestMapping("page/webmagic/webMagicList")
    public String WebMagicList(){
        return "/page/webmagic/webMagicList";
    }
}

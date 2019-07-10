package cn.chao.maven.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("hello")
    public String getHello(){
        return "hello , Spring Boot !!! I think you are a good start .";
    }

    @GetMapping("git")
    public String git(){
        return "hello ,finish Git update.";
    }
}

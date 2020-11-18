package com.example.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by gang on 2020/11/18.
 */
@Controller
public class HelloController {
    //返回hello，springboot集成的thymeleaf会默认去寻找resource/templates/hello.html的模板
    @RequestMapping("/")
    public String hello() {
        return "hello";
    }
}

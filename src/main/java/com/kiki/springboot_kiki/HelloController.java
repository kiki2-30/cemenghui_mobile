package com.kiki.springboot_kiki;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "恭喜！我的第一个 Spring Boot 应用跑起来啦！";
    }
}
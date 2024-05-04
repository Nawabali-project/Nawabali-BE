package com.nawabali.nawabali;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/ping")
    public String check() {
        return "Pong! 로그아웃핸들러 변경";
    }
}

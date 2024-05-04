package com.nawabali.nawabali;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/ping")
    public String check() {
        return "Pong! http 로그아웃url 메서드 사용 변경";
    }
}

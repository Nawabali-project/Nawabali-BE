package com.nawabali.nawabali;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/ping")
    public String check() {
        return "Pong! 무중단배포 성공! 0.1초 안에 Old/New 서버 교체 완료!";
    }
}

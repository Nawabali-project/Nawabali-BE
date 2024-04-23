package com.nawabali.nawabali;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/ping")
    public String check() {
        return "무중단배포 성공! 0.1초 안에 기존에서 신규 서버로 교체 완료되었습니다.";
    }
}

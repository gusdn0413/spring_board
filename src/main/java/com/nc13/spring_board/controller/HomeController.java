package com.nc13.spring_board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 우리가 해당 클래스를 스프링 프레임워크가 직접 생성/초기화를 할 수 있도록 어노테이션 ㄱㄱ
// @Controller 특정 주소를 접속할 떄 보여줄 페이지를 정해줌
@Controller
public class HomeController {
    // 스프링은 URL 기반으로 특정 페이지의 실행 여부를 결정
    // 어떤 메소드를 실행시킬지를 연결(=mapping) 시켜주어야 함
    // @RequestMapping(주소, 연결방식)
    // @GetMapping(주소)
    // @PostMapping(주소)
    @GetMapping("/")
    public String home() {
        System.out.println("인덱스 ㄱㄱ");
        return "index";
    }
}

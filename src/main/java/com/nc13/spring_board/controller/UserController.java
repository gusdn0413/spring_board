package com.nc13.spring_board.controller;

import com.nc13.spring_board.model.UserDTO;
import com.nc13.spring_board.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 사용자가 로그인을 할 시 실행할 auth method
    @PostMapping("auth")
    public String auth(UserDTO userDTO, HttpSession session) {
        UserDTO auth = userService.auth(userDTO);
        if (auth != null) {
            session.setAttribute("login", auth);
            return "redirect:/board/showAll";
        }
        System.out.println("auth = " + auth);
        // 해당 메소드를 실행시키고 나서 특정 URL 로 이등
        return "redirect:/";
    }

    @GetMapping("register")
    public String showRegister() {
        return "user/register";
    }

    @PostMapping("register")
    public String register(UserDTO userDTO, RedirectAttributes redirectAttributes) {
        System.out.println("userDTO = " + userDTO);
        if (userService.validateUsername(userDTO)) {
            System.out.println("슛");
        } else {
            redirectAttributes.addFlashAttribute("message", "중복된 아이디 불가능");
            return "redirect:/showMessage";
        }
        return "redirect:/";
    }
}

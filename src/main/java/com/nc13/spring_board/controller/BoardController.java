package com.nc13.spring_board.controller;

import com.nc13.spring_board.model.BoardDTO;
import com.nc13.spring_board.model.UserDTO;
import com.nc13.spring_board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/board/")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("showAll")
    public String showAll(HttpSession session, Model model) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }
        List<BoardDTO> list = boardService.showAll();
        model.addAttribute("list", list);
        return "board/showAll";
    }
}

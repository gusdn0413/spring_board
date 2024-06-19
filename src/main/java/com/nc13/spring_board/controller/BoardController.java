package com.nc13.spring_board.controller;

import com.nc13.spring_board.model.BoardDTO;
import com.nc13.spring_board.model.UserDTO;
import com.nc13.spring_board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("write")
    public String showWrite(HttpSession session) {

        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }
        return "board/write";
    }

    @PostMapping("write")
    public String write(HttpSession session, BoardDTO boardDTO) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }
        boardDTO.setWriterId(login.getId());
        boardService.insert(boardDTO);
        return "redirect:/board/showOne/" + boardDTO.getId();
    }

    @GetMapping("showOne/{id}")
    public String showOne(HttpSession session, @PathVariable int id,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "해당 글 번호는 유효하지 않습니다");
            return "redirect:/showMessage";
        }

        model.addAttribute("boardDTO", boardDTO);
        return "/board/showOne";
    }

    @GetMapping("update/{id}")
    public String showUpdate(@PathVariable int id,
                             HttpSession session, RedirectAttributes redirectAttributes,
                             Model model) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지않는 글 번호입니다");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != login.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다");
            return "redirect:/showMessage";
        }

        model.addAttribute("boardDTO", boardDTO);
        return "board/update";
    }

    @PostMapping("update/{id}")
    public String update(@PathVariable int id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes,
                         BoardDTO newBoardDTO) {

        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }
        BoardDTO boardDTO = boardService.selectOne(id);

        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "유효하지않은 글 번호 입니다");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != login.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다");
            return "redirect:/showMessage";
        }

        newBoardDTO.setId(id);
        boardService.update(newBoardDTO);
        return "redirect:/board/showOne/" + id;
    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable int id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }
        BoardDTO boardDTO = boardService.selectOne(id);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "유효하지않은 글 번호 입니다");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != login.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다");
            return "redirect:/showMessage";
        }
        boardService.delete(id);
        return "redirect:/board/showAll";
    }

//    @GetMapping("test")
//    public String test(HttpSession session) {
//        UserDTO login = (UserDTO) session.getAttribute("login");
//        if (login == null) {
//            return "redirect:/";
//        }
//        for (int i = 0; i < 300; i++) {
//            BoardDTO boardDTO = new BoardDTO();
//            boardDTO.setTitle("테스트 제목" + i);
//            boardDTO.setContent(i + "번 테스트 글의 내용");
//            boardDTO.setWriterId(login.getId());
//            boardService.insert(boardDTO);
//        }
//        return "redirect:/board/showAll";
//    }
}

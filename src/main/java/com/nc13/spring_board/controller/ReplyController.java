package com.nc13.spring_board.controller;

import com.nc13.spring_board.model.ReplyDTO;
import com.nc13.spring_board.model.UserDTO;
import com.nc13.spring_board.service.BoardService;
import com.nc13.spring_board.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reply/")
public class ReplyController {

    private final ReplyService replyService;
    private final BoardService boardService;

    @PostMapping("insert/{boardId}")
    public String insert(ReplyDTO replyDTO, HttpSession session,
                         @PathVariable int boardId, RedirectAttributes redirectAttributes) {

        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }
        if (boardService.selectOne(boardId) == null) {
            redirectAttributes.addFlashAttribute("message", "유효하지 않은 글 번호입니다.");
            return "redirect:/showMessage";
        }

        replyDTO.setWriterId(login.getId());
        replyDTO.setBoardId(boardId);
        replyService.insert(replyDTO);

        return "redirect:/board/showOne/" + boardId;
    }

    @PostMapping("update/{replyId}")
    public String update(@PathVariable int replyId, HttpSession session,
                         ReplyDTO updateReplyDTO, RedirectAttributes redirectAttributes) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        ReplyDTO replyDTO = replyService.selectOne(replyId);
        if (replyDTO == null) {
            redirectAttributes.addFlashAttribute("message", "유효하지 않은 댓글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (replyDTO.getWriterId() != login.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다.");
            return "redirect:/showMessage";
        }

        updateReplyDTO.setId(replyId);
        replyService.update(updateReplyDTO);

        return "redirect:/board/showOne/" + replyDTO.getBoardId();
    }

    @GetMapping("delete/{replyId}")
    public String delete(@PathVariable int replyId, HttpSession session,
                         RedirectAttributes redirectAttributes) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        ReplyDTO replyDTO = replyService.selectOne(replyId);
        if (replyDTO == null) {
            redirectAttributes.addFlashAttribute("message", "잘못된 번호입니다");
            return "redirect:/showMessage";
        }

        if (replyDTO.getWriterId() != login.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다");
            return "redirect:/showMessage";
        }

        replyService.delete(replyId);

        return "redirect:/board/showOne/" + replyDTO.getBoardId();
    }
}

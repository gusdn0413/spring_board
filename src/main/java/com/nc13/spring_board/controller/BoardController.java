package com.nc13.spring_board.controller;


import com.nc13.spring_board.model.BoardDTO;
import com.nc13.spring_board.model.ReplyDTO;
import com.nc13.spring_board.model.UserDTO;
import com.nc13.spring_board.service.BoardService;
import com.nc13.spring_board.service.ReplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/")
public class BoardController {
    private final BoardService boardService;
    private final ReplyService replyService;

    @GetMapping("showAll")
    public String moveToFirstPage() {
        return "redirect:/board/showAll/1";
    }

    @GetMapping("showAll/{pageNo}")
    public String showAll(Model model, @PathVariable int pageNo) {

        // 가장 마지막 페이지의 번호
        int maxPage = boardService.selectMaxPage();
        model.addAttribute("maxPage", maxPage);

        // 우리가 이제 pageNo를 사용하여
        // 시작 페이지 번호
        // 끝 페이지 번호
        // 을 계산해 주어야 한다.
        // 이때에는 크게 3가지가 있다.
        // 1. 현재 페이지가 3 이하일 경우
        // 시작: 1, 끝: 5
        // 2. 현재 페이지가 최대 페이지 -2 이상일 경우
        // 시작: 최대 페이지 -4 끝: 최대 페이지
        // 3. 그외
        // 시작: 현재 페이지 -2 끝: 현재 페이지 +2

        // 시작 페이지
        int startPage;

        // 끝 페이지
        int endPage;

        if (maxPage < 5) {
            startPage = 1;
            endPage = maxPage;
        } else if (pageNo <= 3) {
            startPage = 1;
            endPage = 5;
        } else if (pageNo >= maxPage - 2) {
            startPage = maxPage - 4;
            endPage = maxPage;
        } else {
            startPage = pageNo - 2;
            endPage = pageNo + 2;
        }

        model.addAttribute("curPage", pageNo);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);


        List<BoardDTO> list = boardService.selectAll(pageNo);
        model.addAttribute("list", list);


        return "board/showAll";
    }

    @GetMapping("write")
    public String showWrite() {

        return "board/write";
    }

    @PostMapping("write")
    public String write(BoardDTO boardDTO,Authentication authentication) {
        UserDTO login = (UserDTO) authentication.getPrincipal();
        boardDTO.setWriterId(login.getId());
        boardService.insert(boardDTO);

        return "redirect:/board/showOne/" + boardDTO.getId();
    }

    @GetMapping("showOne/{boardId}")
    public String showOne(HttpSession session, @PathVariable int boardId, Model model, RedirectAttributes redirectAttributes) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(boardId);

        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "해당 글 번호는 유효하지 않습니다.");
            return "redirect:/showMessage";
        }

        List<ReplyDTO> replyList = replyService.selectAll(boardId);
        model.addAttribute("boardDTO", boardDTO);
        model.addAttribute("replyList", replyList);

        return "board/showOne";
    }

    @GetMapping("update/{id}")
    public String showUpdate(@PathVariable int id, HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(id);

        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != login.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다.");
            return "redirect:/showMessage";
        }

        model.addAttribute("boardDTO", boardDTO);

        return "board/update";
    }

    @PostMapping("update/{boardId}")
    public String update(@PathVariable int boardId, HttpSession session, RedirectAttributes redirectAttributes, BoardDTO attempt) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(boardId);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "유효하지 않은 글 번호입니다.");
            return "redirect:/showMessage";
        }

        if (login.getId() != boardDTO.getWriterId()) {
            redirectAttributes.addFlashAttribute("message", "권한이 없습니다.");
            return "redirect:/showMessage";
        }

        attempt.setId(boardId);

        boardService.update(attempt);

        return "redirect:/board/showOne/" + boardId;
    }

    @GetMapping("delete/{boardId}")
    public String delete(@PathVariable int boardId, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDTO login = (UserDTO) session.getAttribute("login");
        if (login == null) {
            return "redirect:/";
        }

        BoardDTO boardDTO = boardService.selectOne(boardId);
        if (boardDTO == null) {
            redirectAttributes.addFlashAttribute("message", "존재하지 않는 글번호");
            return "redirect:/showMessage";
        }

        if (boardDTO.getWriterId() != login.getId()) {
            redirectAttributes.addFlashAttribute("message", "권한 없음");
            return "redirect:/showMessage";
        }

        boardService.delete(boardId);

        return "redirect:/board/showAll";
    }

    // Restful API 로써 JSON 의 결과값을 리턴해야하는 경우 매핑 어노테이션 위에 ResponseBody 어노테이션을 씀
    @ResponseBody
    @PostMapping("uploads")
    public Map<String, Object> uploads(MultipartHttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();

        String uploadPath = "";

        MultipartFile file = request.getFile("upload");
        String fileName = file.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String uploadName = UUID.randomUUID() + extension;

        String realPath = request.getServletContext().getRealPath("board/uploads/");
        Path realDir = Paths.get(realPath);

        if (!Files.exists(realDir)) {
            try {
                Files.createDirectories(realDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File uploadFile = new File(realPath + uploadName);

        try {
            file.transferTo(uploadFile);
        } catch (IOException e) {
            System.out.println("파일 전송 중 에러");
            e.printStackTrace();
        }

        uploadPath = "/board/uploads/" + uploadName;

        resultMap.put("uploaded", true);
        resultMap.put("url", uploadPath);

        return resultMap;
    }
}
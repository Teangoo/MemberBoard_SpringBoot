package com.its.memberboard.controller;

import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<BoardDTO> boardDTOPage = boardService.findByAll(pageable);
        int startPage = Math.max(1, boardDTOPage.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boardDTOPage.getTotalPages(), boardDTOPage.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boardList", boardDTOPage);
        return "/boardPages/list";
    }

    @GetMapping("/save-form")
    public String saveForm() {
        return "/boardPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        Long saveId = boardService.save(boardDTO);
        if (saveId > 0) {
            return "redirect:/board/" + saveId;
        } else {
            return "/save-fail";
        }
    }
}

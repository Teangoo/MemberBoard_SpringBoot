package com.its.memberboard.controller;

import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.dto.CommentDTO;
import com.its.memberboard.service.BoardService;
import com.its.memberboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

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
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        List<CommentDTO> commentDTOList = commentService.findById(id);
        model.addAttribute("board",boardDTO);
        model.addAttribute("commentList",commentDTOList);
        return "/boardPages/detail";
    }

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestParam("id") Long id){
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/update-form/{id}")
    public String updateForm(@PathVariable Long id , Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate" , boardDTO);
        return "/boardPages/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO) throws IOException {
       Long updateId = boardService.update(boardDTO);
       return "redirect:/board/"+updateId;
    }

    @GetMapping("/search")
    public String search(@RequestParam String q , @RequestParam String type, Model model,@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardDTO> boardDTOList = boardService.search(q, type,pageable);
        int startPage = Math.max(1, boardDTOList.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boardDTOList.getTotalPages(), boardDTOList.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("type",type);
        model.addAttribute("q",q);
        model.addAttribute("boardList", boardDTOList);
        return "/boardPages/search";
    }
}

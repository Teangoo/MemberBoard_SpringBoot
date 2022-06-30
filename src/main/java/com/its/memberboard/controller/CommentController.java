package com.its.memberboard.controller;

import com.its.memberboard.dto.CommentDTO;
import com.its.memberboard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/save")
    public @ResponseBody List<CommentDTO> save(@ModelAttribute CommentDTO commentDTO){
        commentService.save(commentDTO);
        List<CommentDTO> commentDTOList = commentService.findById(commentDTO.getBoardId());
        return commentDTOList;
    }

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestParam("id") Long id){
        commentService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}

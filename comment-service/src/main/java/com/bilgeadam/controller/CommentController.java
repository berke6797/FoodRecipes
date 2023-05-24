package com.bilgeadam.controller;

import com.bilgeadam.dto.request.MakeCommentRequestDto;
import com.bilgeadam.dto.request.UpdateCommentRequestDto;
import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/make-comment/{token}")
    public ResponseEntity<Boolean> makeComment(@RequestBody MakeCommentRequestDto dto,@PathVariable String token){
        return ResponseEntity.ok(commentService.makeComment(dto,token));
    }
    @PutMapping("/update-comment/{token}")
    public ResponseEntity<Boolean> updateComment(@PathVariable String token, @RequestBody UpdateCommentRequestDto dto){
        return ResponseEntity.ok(commentService.updateComment(token,dto));
    }
    @GetMapping("/find-all-comment/{token}")
    public ResponseEntity<List<Comment>> findAllCommentFromUser(@PathVariable String token){
        return ResponseEntity.ok(commentService.findAllCommentFromUser(token));
    }
}

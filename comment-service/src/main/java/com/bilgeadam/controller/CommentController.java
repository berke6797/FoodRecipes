package com.bilgeadam.controller;

import com.bilgeadam.dto.request.MakeCommentRequestDto;
import com.bilgeadam.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/make-comment/{token}")
    public ResponseEntity<Boolean> makeComment(@RequestBody MakeCommentRequestDto dto,@PathVariable String token){
        return ResponseEntity.ok(commentService.makeComment(dto,token));
    }
}

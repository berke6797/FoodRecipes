package com.bilgeadam.controller;

import com.bilgeadam.dto.request.MakeCommentRequestDto;
import com.bilgeadam.dto.request.UpdateCommentRequestDto;
import com.bilgeadam.repository.entity.Comment;
import com.bilgeadam.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static com.bilgeadam.constant.ApiUrls.*;
@RestController
@RequestMapping(COMMENT)
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    @PostMapping(MAKE_COMMENT+"/{token}")
    public ResponseEntity<Boolean> makeComment(@RequestBody MakeCommentRequestDto dto,@PathVariable String token){
        return ResponseEntity.ok(commentService.makeComment(dto,token));
    }
    @PutMapping(UPDATE_COMMENT+"/{token}")
    public ResponseEntity<Boolean> updateComment(@PathVariable String token, @RequestBody UpdateCommentRequestDto dto){
        return ResponseEntity.ok(commentService.updateComment(token,dto));
    }
    @GetMapping(FIND_ALL_COMMENT+"/{token}")
    public ResponseEntity<List<Comment>> findAllCommentFromUser(@PathVariable String token){
        return ResponseEntity.ok(commentService.findAllCommentFromUser(token));
    }

    @DeleteMapping(DELETE_COMMENT+"/{token}/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable String token, @PathVariable String commentId){
        return ResponseEntity.ok(commentService.deleteComment(token,commentId));
    }
}

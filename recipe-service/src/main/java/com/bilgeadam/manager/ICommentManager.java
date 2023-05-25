package com.bilgeadam.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "http://localhost:9091/api/v1/comment", name = "recipeToComment")
public interface ICommentManager {
    // Recipe silindiğinde comment'in silinmeşi için
    @PostMapping("/delete-comment-from-recipe-delete/{commentId}")
    public ResponseEntity<Boolean> deleteCommentFromRecipe(@PathVariable String recipeId);
}

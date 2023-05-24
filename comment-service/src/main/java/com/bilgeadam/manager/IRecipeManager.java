package com.bilgeadam.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "http://localhost:9092/api/v1/recipe",name = "commentToRecipe")
public interface IRecipeManager {
    @PostMapping("/save-comment-to-recipe/{commentId}/{recipeId}")
    public ResponseEntity<Boolean> saveRecipeCommentFromComment(@PathVariable String commentId,@PathVariable String recipeId);
}

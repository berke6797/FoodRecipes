package com.bilgeadam.manager;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "http://localhost:9092/api/v1/recipe",name = "commentToRecipe")
public interface IRecipeManager {
    @PostMapping("/save-comment-to-recipe/{commentId}/{recipeId}")
    public ResponseEntity<Boolean> saveRecipeCommentFromComment(@PathVariable String commentId,@PathVariable String recipeId);
    @DeleteMapping("/delete-comment-to-recipe/{recipeId}/{commentId}")
    public ResponseEntity<Boolean> deleteRecipeCommentFromComment(@PathVariable String recipeId, @PathVariable String commentId);
    @PostMapping("/save-point-to-recipe/{recipeId}/{pointId}")
    public ResponseEntity<Boolean> saveRecipePointFromPoint(@PathVariable String recipeId,@PathVariable String pointId);

}

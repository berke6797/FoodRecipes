package com.bilgeadam.controller;

import com.bilgeadam.dto.request.SaveRecipeRequestDto;
import com.bilgeadam.dto.request.UpdateRecipeRequestDto;
import com.bilgeadam.dto.response.UpdateRecipeResponseDto;
import com.bilgeadam.repository.entity.Recipe;
import com.bilgeadam.service.RecipeService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping("/save-recipe/{token}")
    public ResponseEntity<Boolean> saveRecipe(@PathVariable String token, @RequestBody SaveRecipeRequestDto dto){
        return ResponseEntity.ok(recipeService.saveRecipe(token,dto));
    }
    @PutMapping("/update-recipe/{token}")
    public ResponseEntity<UpdateRecipeResponseDto> updateRecipe(@PathVariable String token,@RequestBody UpdateRecipeRequestDto dto){
        return ResponseEntity.ok(recipeService.updateRecipe(token,dto));
    }
    @DeleteMapping("/delete-recipe/{token}/{recipeId}")
    public ResponseEntity<Boolean> deleteRecipe(@PathVariable String token,@PathVariable String recipeId){
        return ResponseEntity.ok(recipeService.deleteRecipe(token,recipeId));
    }
    @GetMapping("/find-all-recipe")
    public ResponseEntity<List<Recipe>> findAllRecipe(){
        return ResponseEntity.ok(recipeService.findAll());
    }

    @Hidden
    @PostMapping("/save-comment-to-recipe/{commentId}/{recipeId}")
    public ResponseEntity<Boolean> saveRecipeCommentFromComment(@PathVariable String commentId, @PathVariable String recipeId){
        return ResponseEntity.ok(recipeService.saveRecipeCommentFromComment(commentId,recipeId));
    }

    @Hidden
    @DeleteMapping("/delete-comment-to-recipe/{recipeId}/{commentId}")
    public ResponseEntity<Boolean> deleteRecipeCommentFromComment(@PathVariable String recipeId, @PathVariable String commentId){
        return ResponseEntity.ok(recipeService.deleteRecipeCommentFromComment(recipeId,commentId));
    }
    @Hidden
    @PostMapping("/save-point-to-recipe/{recipeId}/{pointId}")
    public ResponseEntity<Boolean> saveRecipePointFromPoint(@PathVariable String recipeId,@PathVariable String pointId){
        return ResponseEntity.ok(recipeService.saveRecipePointFromPoint(recipeId, pointId));
    }
    @Hidden
    @PostMapping("/delete-point-to-recipe/{pointId}/{recipeId}")
    public ResponseEntity<Boolean> deleteRecipePointFromPoint(@PathVariable String pointId,@PathVariable String recipeId){
        return ResponseEntity.ok(recipeService.deleteRecipePointFromPoint(pointId,recipeId));
    }


}

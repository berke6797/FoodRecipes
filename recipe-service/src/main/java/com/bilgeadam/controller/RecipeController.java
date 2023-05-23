package com.bilgeadam.controller;

import com.bilgeadam.dto.request.SaveRecipeRequestDto;
import com.bilgeadam.dto.request.UpdateRecipeRequestDto;
import com.bilgeadam.dto.response.UpdateRecipeResponseDto;
import com.bilgeadam.repository.entity.Category;
import com.bilgeadam.repository.entity.Recipe;
import com.bilgeadam.service.RecipeService;
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


}

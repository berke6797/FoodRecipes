package com.bilgeadam.controller;

import com.bilgeadam.dto.request.FindIngredientsNameRequestDto;
import com.bilgeadam.dto.request.SaveRecipeRequestDto;
import com.bilgeadam.dto.request.UpdateRecipeRequestDto;
import com.bilgeadam.dto.response.GetRecipeAndCategoryResponseDto;
import com.bilgeadam.dto.response.UpdateRecipeResponseDto;
import com.bilgeadam.repository.entity.Recipe;
import com.bilgeadam.service.RecipeService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.bilgeadam.constant.ApiUrls.*;
import java.util.List;
@RestController
@RequestMapping(RECIPE)
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeService recipeService;

    @PostMapping(SAVE_RECIPE+"/{token}")
    public ResponseEntity<Boolean> saveRecipe(@PathVariable String token, @RequestBody SaveRecipeRequestDto dto){
        return ResponseEntity.ok(recipeService.saveRecipe(token,dto));
    }
    @PutMapping(UPDATE_RECIPE+"/{token}")
    public ResponseEntity<UpdateRecipeResponseDto> updateRecipe(@PathVariable String token,@RequestBody UpdateRecipeRequestDto dto){
        return ResponseEntity.ok(recipeService.updateRecipe(token,dto));
    }
    @DeleteMapping(DELETE_RECIPE+"/{token}/{recipeId}")
    public ResponseEntity<Boolean> deleteRecipe(@PathVariable String token,@PathVariable String recipeId){
        return ResponseEntity.ok(recipeService.deleteRecipe(token,recipeId));
    }
    @GetMapping(FIND_ALL_RECIPE)
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
    @Operation(summary = "Kategoriye göre tarifler listelenecektir")
    @GetMapping(FIND_ALL_RECIPE_BY_CATEGORY_ID+"/{categoryId}")
    public ResponseEntity<List<Recipe>> filterRecipeListByCategory(@PathVariable String categoryId){
        return ResponseEntity.ok(recipeService.filterRecipeListByCategory(categoryId));
    }
    @Operation(summary = "Tarif adına göre tarifler listelenecektir")
    @GetMapping(FIND_ALL_RECIPES_BY_RECIPENAME+"/{recipeName}")
    public ResponseEntity<List<Recipe>> filterRecipeListByRecipeName(String recipeName){
        return ResponseEntity.ok(recipeService.filterRecipeListByRecipeName(recipeName));
    }
    @Operation(summary = "Malzeme içeriklerine göre tarifler listelenecektir")
    @GetMapping(FIND_ALL_RECIPES_BY_INGREDIENT_NAME)
    public ResponseEntity<List<Recipe>> filterRecipeListByIngredient(FindIngredientsNameRequestDto dto){
        return ResponseEntity.ok(recipeService.filterRecipeListByIngredient(dto));
    }
    @Operation(summary = "Tarifler kalorilerine göre artan şekilde sıralanmıştır")
    @GetMapping(FIND_ALL_ORDERED_RECIPES_BY_CALORIES)
    public ResponseEntity<List<Recipe>> orderByRecipesByCalories(){
        return ResponseEntity.ok(recipeService.orderByRecipesByCalories());
    }
    @Hidden
    @GetMapping("/get-recipe-and-category-id-for-recipe/{recipeId}")
    ResponseEntity<GetRecipeAndCategoryResponseDto> getRecipeAndCategoryId(@PathVariable String recipeId){
        return ResponseEntity.ok(recipeService.getRecipeAndCategoryId(recipeId));
    }

}

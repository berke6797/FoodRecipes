package com.bilgeadam.manager;

import com.bilgeadam.dto.response.GetRecipeAndCategoryResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "http://localhost:9092/api/v1/recipe",name = "userToRecipe")

public interface IRecipeManager {
    @GetMapping("/get-recipe-and-category-id-for-recipe/{recipeId}")
    ResponseEntity<GetRecipeAndCategoryResponseDto> getRecipeAndCategoryId(@PathVariable String recipeId);
}

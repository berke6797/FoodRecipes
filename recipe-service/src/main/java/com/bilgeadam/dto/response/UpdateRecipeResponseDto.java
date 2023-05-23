package com.bilgeadam.dto.response;

import com.bilgeadam.repository.entity.Ingredient;
import com.bilgeadam.repository.entity.NutritionalValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRecipeResponseDto {
    private String recipeName;
    private String type;
    private String preperationTime;
    private String cookingTime;
    private List<String> recipeSteps;
    private String photos;
    private List<Ingredient> ingredients;
    private NutritionalValue nutritionalValue;
    private List<String> categoryId;
}

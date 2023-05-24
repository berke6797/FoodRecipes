package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document
public class Recipe extends Base {
    @Id
    private String recipeId;
    private String recipeName;
    private String type;
    private String preperationTime;
    private String cookingTime;
    private List<String> recipeSteps;
    private String photos;
    private List<Ingredient> ingredients;
    private NutritionalValue nutritionalValue;
    private List<String> categoryId = new ArrayList<>();
    private List<String> commentId = new ArrayList<>();
    private List<String> pointId = new ArrayList<>();

}

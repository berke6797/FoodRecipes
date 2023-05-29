package com.bilgeadam.dto.request;

import com.bilgeadam.repository.entity.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindIngredientsNameRequestDto {
    List<String> ingredientNames;
}

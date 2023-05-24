package com.bilgeadam.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MakeCommentRequestDto {
    @NotBlank(message = "RecipeId boş bırakılamaz")
    String recipeId;
    String comment;

}

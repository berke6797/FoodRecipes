package com.bilgeadam.mapper;

import com.bilgeadam.dto.request.SaveRecipeRequestDto;
import com.bilgeadam.dto.request.UpdateRecipeRequestDto;
import com.bilgeadam.dto.response.UpdateRecipeResponseDto;
import com.bilgeadam.repository.entity.Recipe;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRecipeMapper {
    IRecipeMapper INSTANCE= Mappers.getMapper(IRecipeMapper.class);

    Recipe fromSaveRecipeRequestDtoToRecipe(final SaveRecipeRequestDto saveRecipeRequestDto);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Recipe fromUpdateRecipeRequestDtoToRecipe(final UpdateRecipeRequestDto updateRecipeRequestDto, @MappingTarget Recipe recipe);
    UpdateRecipeResponseDto fromRecipeToUpdateRecipeResponseDto(final Recipe recipe);
}

package com.bilgeadam.service;

import com.bilgeadam.dto.request.SaveRecipeRequestDto;
import com.bilgeadam.dto.request.UpdateRecipeRequestDto;
import com.bilgeadam.dto.response.UpdateRecipeResponseDto;
import com.bilgeadam.mapper.IRecipeMapper;
import com.bilgeadam.repository.IRecipeRepository;
import com.bilgeadam.repository.entity.Recipe;
import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService extends ServiceManager<Recipe, String> {
    private final IRecipeRepository recipeRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public RecipeService(IRecipeRepository recipeRepository, JwtTokenProvider jwtTokenProvider) {
        super(recipeRepository);
        this.recipeRepository = recipeRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    public Boolean saveRecipe(String token, SaveRecipeRequestDto dto) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RuntimeException("Bu işlemi sadece ADMIN rolüne sahip kullanıcılar gerçekleştirebilir");
        }
        save(IRecipeMapper.INSTANCE.fromSaveRecipeRequestDtoToRecipe(dto));
        return true;
    }
    public UpdateRecipeResponseDto updateRecipe(String token, UpdateRecipeRequestDto dto) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RuntimeException("Bu işlemi sadece ADMIN rolüne sahip kullanıcılar gerçekleştirebilir");
        }
        Recipe recipe = IRecipeMapper.INSTANCE.fromUpdateRecipeRequestDtoToRecipe(dto);
        save(recipe);
        UpdateRecipeResponseDto updateRecipeResponseDto = IRecipeMapper.INSTANCE.fromRecipeToUpdateRecipeResponseDto(recipe);
        return updateRecipeResponseDto;
    }
    public Boolean deleteRecipe(String token, String recipeId) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RuntimeException("Bu işlemi sadece ADMIN rolüne sahip kullanıcılar gerçekleştirebilir");
        }
        recipeRepository.deleteById(recipeId);
        return true;
    }

    public List<Recipe> sortAllRecipe

}

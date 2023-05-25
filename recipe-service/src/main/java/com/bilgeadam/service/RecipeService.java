package com.bilgeadam.service;

import com.bilgeadam.dto.request.SaveRecipeRequestDto;
import com.bilgeadam.dto.request.UpdateRecipeRequestDto;
import com.bilgeadam.dto.response.UpdateRecipeResponseDto;
import com.bilgeadam.manager.ICommentManager;
import com.bilgeadam.mapper.IRecipeMapper;
import com.bilgeadam.repository.IRecipeRepository;
import com.bilgeadam.repository.entity.Recipe;
import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecipeService extends ServiceManager<Recipe, String> {
    private final IRecipeRepository recipeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ICommentManager commentManager;

    public RecipeService(IRecipeRepository recipeRepository, JwtTokenProvider jwtTokenProvider, ICommentManager commentManager) {
        super(recipeRepository);
        this.recipeRepository = recipeRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.commentManager = commentManager;
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
        Recipe recipe = findById(dto.getRecipeId()).get();
        IRecipeMapper.INSTANCE.fromUpdateRecipeRequestDtoToRecipe(dto,recipe);
        save(recipe);
        UpdateRecipeResponseDto updateRecipeResponseDto = IRecipeMapper.INSTANCE.fromRecipeToUpdateRecipeResponseDto(recipe);
        return updateRecipeResponseDto;
    }

    public Boolean deleteRecipe(String token, String recipeId) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RuntimeException("Bu işlemi sadece ADMIN rolüne sahip kullanıcılar gerçekleştirebilir");
        }
        Optional<Recipe> recipe= findById(recipeId);
        if (recipe.isEmpty()){
            throw new RuntimeException("Böyle bir tarif bulunmamaktadır.");
        }
        recipeRepository.deleteById(recipeId);
        commentManager.deleteCommentFromRecipe(recipeId);
        return true;
    }


    public Boolean saveRecipeCommentFromComment(String commentId,String recipeId) {
        Optional<Recipe> recipe=recipeRepository.findById(recipeId);
        if (recipe.isEmpty()){
            throw new RuntimeException("Böyle bir recipe bulunmamaktadır");
        }
        recipe.get().getCommentId().add(commentId);
        update(recipe.get());
        return true;
    }


    public Boolean deleteRecipeCommentFromComment(String recipeId,String commentId) {
        Optional<Recipe> recipe= recipeRepository.findById(recipeId);
        if (recipe.isEmpty()){
            throw new RuntimeException("Böyle bir recipe bulunmamaktadır");
        }
        recipe.get().getCommentId().remove(commentId);
        update(recipe.get());
        return true;
    }



}

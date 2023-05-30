package com.bilgeadam.service;

import com.bilgeadam.dto.request.FindIngredientsNameRequestDto;
import com.bilgeadam.dto.request.SaveRecipeRequestDto;
import com.bilgeadam.dto.request.UpdateRecipeRequestDto;
import com.bilgeadam.dto.response.GetRecipeAndCategoryResponseDto;
import com.bilgeadam.dto.response.GetUserWithFavoriCategory;
import com.bilgeadam.dto.response.UpdateRecipeResponseDto;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.RecipeManagerException;
import com.bilgeadam.manager.ICommentManager;
import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.mapper.IRecipeMapper;
import com.bilgeadam.rabbitmq.model.FavoriteCategoriesMailModel;
import com.bilgeadam.rabbitmq.producer.FavoriteCategoriesMailProducer;
import com.bilgeadam.repository.IRecipeRepository;
import com.bilgeadam.repository.entity.Recipe;
import com.bilgeadam.repository.enums.ERole;
import com.bilgeadam.utility.JwtTokenProvider;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService extends ServiceManager<Recipe, String> {
    private final IRecipeRepository recipeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ICommentManager commentManager;
    private final IUserManager userManager;
    private final FavoriteCategoriesMailProducer favoriteCategoriesMailProducer;

    public RecipeService(IRecipeRepository recipeRepository, JwtTokenProvider jwtTokenProvider, ICommentManager commentManager, IUserManager userManager, FavoriteCategoriesMailProducer favoriteCategoriesMailProducer) {
        super(recipeRepository);
        this.recipeRepository = recipeRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.commentManager = commentManager;
        this.userManager = userManager;
        this.favoriteCategoriesMailProducer = favoriteCategoriesMailProducer;
    }

    public Boolean saveRecipe(String token, SaveRecipeRequestDto dto) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RecipeManagerException(ErrorType.ROLE_ERROR);
        }
        save(IRecipeMapper.INSTANCE.fromSaveRecipeRequestDtoToRecipe(dto));
        Set<GetUserWithFavoriCategory> getUserCategory = userManager.getUserWithFavoriCategory(dto.getCategoryId()).getBody();
        getUserCategory.forEach(user -> {
            favoriteCategoriesMailProducer.sendMailForFavoriteCategory(FavoriteCategoriesMailModel.builder()
                    .recipeName(dto.getRecipeName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build());
        });
        return true;

    }

    public UpdateRecipeResponseDto updateRecipe(String token, UpdateRecipeRequestDto dto) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RecipeManagerException(ErrorType.ROLE_ERROR);
        }
        Recipe recipe = findById(dto.getRecipeId()).get();
        IRecipeMapper.INSTANCE.fromUpdateRecipeRequestDtoToRecipe(dto, recipe);
        save(recipe);
        UpdateRecipeResponseDto updateRecipeResponseDto = IRecipeMapper.INSTANCE.fromRecipeToUpdateRecipeResponseDto(recipe);
        return updateRecipeResponseDto;
    }

    public Boolean deleteRecipe(String token, String recipeId) {
        Optional<String> role = jwtTokenProvider.getRoleFromToken(token);
        if (!role.get().equals(ERole.ADMIN.toString())) {
            throw new RecipeManagerException(ErrorType.ROLE_ERROR);
        }
        Optional<Recipe> recipe = findById(recipeId);
        if (recipe.isEmpty()) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        recipeRepository.deleteById(recipeId);
        commentManager.deleteCommentFromRecipe(recipeId);
        return true;
    }


    public List<Recipe> filterRecipeListByCategory(String categoryId) {
        List<Recipe> recipes = recipeRepository.findAllByCategoryId(categoryId);
        if (recipes.size() == 0) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        recipes.stream().filter(x -> x.equals(categoryId)).collect(Collectors.toList());
        return recipes;
    }

    public List<Recipe> filterRecipeListByIngredient(FindIngredientsNameRequestDto dto) {
        List<Recipe> recipes = findAll();
        Set<Recipe> ingredientsList = new HashSet<>();
        if (dto.getIngredientNames().isEmpty()) {
            return recipes;
        }
        dto.getIngredientNames().forEach(ingredientName -> {
            recipes.forEach(recipe -> {
                recipe.getIngredients().forEach(ingredient -> {
                    if (ingredient.getIngredientName().equals(ingredientName)) {
                        ingredientsList.add(recipe);
                    }
                });
            });
        });
        List<Recipe> filteredRecipeList = new ArrayList<>(ingredientsList);
        return filteredRecipeList;
    }

    public Boolean saveRecipeCommentFromComment(String commentId, String recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        recipe.get().getCommentId().add(commentId);
        update(recipe.get());
        return true;
    }

    public Boolean deleteRecipeCommentFromComment(String recipeId, String commentId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        recipe.get().getCommentId().remove(commentId);
        update(recipe.get());
        return true;
    }

    public Boolean saveRecipePointFromPoint(String recipeId, String pointId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        recipe.get().getPointId().add(pointId);
        update(recipe.get());
        return true;
    }

    public Boolean deleteRecipePointFromPoint(String pointId, String recipeId) {
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        recipe.get().getPointId().remove(pointId);
        update(recipe.get());
        return true;
    }

    public List<Recipe> filterRecipeListByRecipeName(String recipeName) {
        List<Recipe> recipes = recipeRepository.findAllByRecipeNameIgnoreCase(recipeName.toLowerCase());
        if (recipes.size() == 0)
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        recipes.stream().filter(x -> x.equals(recipeName.toLowerCase())).collect(Collectors.toList());
        return recipes;
    }

    public List<Recipe> orderByRecipesByCalories() {
        List<Recipe> recipes = recipeRepository.findAll();
        if (recipes.isEmpty()) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        Comparator<Recipe> comparator = Comparator.comparing(x -> x.getNutritionalValue().getCalorie());
        Collections.sort(recipes, comparator);
        return recipes;
    }

    public GetRecipeAndCategoryResponseDto getRecipeAndCategoryId(String recipeId) {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(recipeId);
        if (optionalRecipe.isEmpty()) {
            throw new RecipeManagerException(ErrorType.RECIPE_NOT_FOUND);
        }
        List<String> categoryIdList = optionalRecipe.get().getCategoryId();
        return GetRecipeAndCategoryResponseDto.builder().recipeId(recipeId).categoryId(categoryIdList).build();
    }
}

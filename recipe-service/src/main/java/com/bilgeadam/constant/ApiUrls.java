package com.bilgeadam.constant;

public class ApiUrls {

    public static final String VERSION = "api/v1";
    public static final String CATEGORY = VERSION + "/category";
    public static final String CREATE_CATEGORY = "/create-category";
    public static final String UPDATE_CATEGORY = "/update-category";
    public static final String DELETE_CATEGORY = "/delete-category";
    public static final String FIND_ALL_CATEGORY = "/find-all-category";

    public static final String RECIPE = VERSION + "/recipe";
    public static final String SAVE_RECIPE = "/save-recipe";
    public static final String UPDATE_RECIPE = "/update-recipe";
    public static final String FIND_ALL_RECIPE = "/find-all-recipe";
    public static final String DELETE_RECIPE = "/delete-recipe";
    public static final String FIND_ALL_RECIPE_BY_CATEGORY_ID= "/find-all-recipes-by-categoryId";
    public static final String FIND_ALL_RECIPES_BY_RECIPENAME="/find-all-recipes-by-recipeName" ;
    public static final String FIND_ALL_RECIPES_BY_INGREDIENT_NAME= "/find-all-recipes-by-ingredient-name";
    public static final String FIND_ALL_ORDERED_RECIPES_BY_CALORIES="/find-all-ordered-recipes-by-calories";
}

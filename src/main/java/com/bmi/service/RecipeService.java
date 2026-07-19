package com.bmi.service;

import com.bmi.dao.RecipeCategoryDAO;
import com.bmi.dao.RecipeDAO;
import com.bmi.dao.IngredientDAO;
import com.bmi.model.Ingredient;
import com.bmi.model.Recipe;
import com.bmi.model.RecipeCategory;

import java.util.List;

/**
 * 食谱服务层
 */
public class RecipeService {
    private RecipeDAO recipeDAO = new RecipeDAO();
    private RecipeCategoryDAO categoryDAO = new RecipeCategoryDAO();
    private IngredientDAO ingredientDAO = new IngredientDAO();

    // 食谱操作
    public List<Recipe> findAllRecipes() {
        return recipeDAO.findAll();
    }

    public List<Recipe> searchRecipes(String keyword, int categoryId) {
        return recipeDAO.search(keyword, categoryId);
    }

    public List<Recipe> findBySuitableBmi(String bmiRange) {
        return recipeDAO.findBySuitableBmi(bmiRange);
    }

    public Recipe findRecipeById(int id) {
        return recipeDAO.findById(id);
    }

    public boolean addRecipe(Recipe recipe) {
        return recipeDAO.add(recipe);
    }

    public boolean updateRecipe(Recipe recipe) {
        return recipeDAO.update(recipe);
    }

    public boolean deleteRecipe(int id) {
        return recipeDAO.delete(id);
    }

    public int countRecipes() {
        return recipeDAO.count();
    }

    // 分类操作
    public List<RecipeCategory> findAllCategories() {
        return categoryDAO.findAll();
    }

    public RecipeCategory findCategoryById(int id) {
        return categoryDAO.findById(id);
    }

    public boolean addCategory(RecipeCategory category) {
        return categoryDAO.add(category);
    }

    public boolean updateCategory(RecipeCategory category) {
        return categoryDAO.update(category);
    }

    public boolean deleteCategory(int id) {
        return categoryDAO.delete(id);
    }

    public int countCategories() {
        return categoryDAO.count();
    }

    // 材料操作
    public List<Ingredient> findAllIngredients() {
        return ingredientDAO.findAll();
    }

    public List<Ingredient> searchIngredients(String keyword) {
        return ingredientDAO.search(keyword);
    }

    public Ingredient findIngredientById(int id) {
        return ingredientDAO.findById(id);
    }

    public boolean addIngredient(Ingredient ingredient) {
        return ingredientDAO.add(ingredient);
    }

    public boolean updateIngredient(Ingredient ingredient) {
        return ingredientDAO.update(ingredient);
    }

    public boolean deleteIngredient(int id) {
        return ingredientDAO.delete(id);
    }

    public int countIngredients() {
        return ingredientDAO.count();
    }
}

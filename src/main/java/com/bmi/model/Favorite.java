package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 收藏模型
 */
public class Favorite {
    private int id;
    private int userId;
    private int recipeId;
    private String recipeName;
    private String categoryName;
    private double calories;
    private Date createTime;

    public Favorite() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}

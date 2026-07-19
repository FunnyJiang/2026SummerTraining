package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 食谱信息模型
 */
public class Recipe {
    private int id;
    private int categoryId;
    private String categoryName;
    private String name;
    private String description;
    private double calories;
    private double protein;
    private double fat;
    private double carbs;
    private String suitableBmi;
    private String imageUrl;
    private Date createTime;

    public Recipe() {}

    public Recipe(int categoryId, String name, String description, double calories,
                  double protein, double fat, double carbs, String suitableBmi) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
        this.suitableBmi = suitableBmi;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public String getSuitableBmi() { return suitableBmi; }
    public void setSuitableBmi(String suitableBmi) { this.suitableBmi = suitableBmi; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}

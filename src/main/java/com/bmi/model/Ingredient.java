package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 材料信息模型
 */
public class Ingredient {
    private int id;
    private String name;
    private String description;
    private double calories;
    private String unit;
    private String category;
    private Date createTime;

    public Ingredient() {}

    public Ingredient(String name, String description, double calories, String unit, String category) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.unit = unit;
        this.category = category;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}

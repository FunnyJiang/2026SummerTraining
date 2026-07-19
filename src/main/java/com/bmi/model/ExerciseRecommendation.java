package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 运动推荐模型
 */
public class ExerciseRecommendation {
    private int id;
    private String name;
    private String description;
    private int duration;       // 建议时长(分钟)
    private String frequency;   // 建议频率
    private double caloriesBurned;  // 消耗热量(千卡/小时)
    private String suitableBmi; // 适合BMI范围
    private String difficulty;  // 难度: 简单/中等/困难
    private String category;    // 有氧运动/力量训练/柔韧拉伸/综合训练
    private Date createTime;

    public ExerciseRecommendation() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public double getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(double caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public String getSuitableBmi() { return suitableBmi; }
    public void setSuitableBmi(String suitableBmi) { this.suitableBmi = suitableBmi; }
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd").format(createTime);
    }

    /**
     * 获取难度对应的颜色
     */
    public String getDifficultyColor() {
        if ("简单".equals(difficulty)) return "#4CAF50";
        if ("中等".equals(difficulty)) return "#FF9800";
        if ("困难".equals(difficulty)) return "#F44336";
        return "#9E9E9E";
    }
}

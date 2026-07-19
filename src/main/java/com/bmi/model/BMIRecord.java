package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BMI记录模型
 */
public class BMIRecord {
    private int id;
    private int userId;
    private double height;
    private double weight;
    private double bmi;
    private String assessment;
    private String recommendation;
    private Date createTime;

    public BMIRecord() {}

    public BMIRecord(int userId, double height, double weight, double bmi,
                     String assessment, String recommendation) {
        this.userId = userId;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.assessment = assessment;
        this.recommendation = recommendation;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }
    public String getAssessment() { return assessment; }
    public void setAssessment(String assessment) { this.assessment = assessment; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}

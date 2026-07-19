package com.bmi.service;

import com.bmi.dao.BMIRecordDAO;
import com.bmi.dao.RecipeDAO;
import com.bmi.model.BMIRecord;
import com.bmi.model.Recipe;
import com.bmi.util.UIUtil;

import java.util.List;

/**
 * BMI服务层
 */
public class BMIService {
    private BMIRecordDAO bmiRecordDAO = new BMIRecordDAO();
    private RecipeDAO recipeDAO = new RecipeDAO();

    /**
     * 计算BMI
     */
    public double calculateBMI(double height, double weight) {
        return UIUtil.calculateBMI(height, weight);
    }

    /**
     * 获取BMI评估
     */
    public String getAssessment(double bmi) {
        return UIUtil.getBMIAssessment(bmi);
    }

    /**
     * 获取膳食建议
     */
    public String getDietRecommendation(double bmi) {
        return UIUtil.getBMIDietRecommendation(bmi);
    }

    /**
     * 保存BMI记录
     */
    public boolean saveRecord(BMIRecord record) {
        return bmiRecordDAO.add(record);
    }

    /**
     * 查询用户BMI记录
     */
    public List<BMIRecord> findRecordsByUserId(int userId) {
        return bmiRecordDAO.findByUserId(userId);
    }

    /**
     * 删除BMI记录
     */
    public boolean deleteRecord(int id) {
        return bmiRecordDAO.delete(id);
    }

    /**
     * 根据BMI获取推荐食谱
     */
    public List<Recipe> getRecommendedRecipes(double bmi) {
        String assessment = UIUtil.getBMIAssessment(bmi);
        String suitableBmi = "";
        if (bmi < 18.5) suitableBmi = "<18.5";
        else if (bmi < 24.0) suitableBmi = "18.5-24.9";
        else if (bmi < 28.0) suitableBmi = "25-29.9";
        else suitableBmi = ">=30";

        List<Recipe> recipes = recipeDAO.findBySuitableBmi(suitableBmi);
        if (recipes.isEmpty()) {
            recipes = recipeDAO.findAll();
        }
        return recipes;
    }

    public int countAllRecords() {
        return bmiRecordDAO.countAll();
    }
}

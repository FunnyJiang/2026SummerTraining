package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 每日膳食摄入记录模型
 * 记录用户某一天所有餐次的合计营养摄入
 */
public class DietRecord {
    private int id;
    private int userId;
    private Date recordDate;     // 摄入日期
    private double carbs;        // 碳水化合物 (g)
    private double fat;          // 脂肪 (g)
    private double protein;      // 蛋白质 (g)
    private double calories;     // 热量 (大卡)
    private double fiber;        // 膳食纤维 (g)
    private double cholesterol;  // 胆固醇 (mmol)
    private double water;        // 饮水量 (ml)
    private String note;         // 备注
    private Date createTime;     // 录入时间

    public DietRecord() {}

    public DietRecord(int userId, Date recordDate, double carbs, double fat, double protein,
                      double calories, double fiber, double cholesterol, double water, String note) {
        this.userId = userId;
        this.recordDate = recordDate;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
        this.calories = calories;
        this.fiber = fiber;
        this.cholesterol = cholesterol;
        this.water = water;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Date getRecordDate() { return recordDate; }
    public void setRecordDate(Date recordDate) { this.recordDate = recordDate; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getCalories() { return calories; }
    public void setCalories(double calories) { this.calories = calories; }
    public double getFiber() { return fiber; }
    public void setFiber(double fiber) { this.fiber = fiber; }
    public double getCholesterol() { return cholesterol; }
    public void setCholesterol(double cholesterol) { this.cholesterol = cholesterol; }
    public double getWater() { return water; }
    public void setWater(double water) { this.water = water; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getRecordDateStr() {
        if (recordDate == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd").format(recordDate);
    }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(createTime);
    }
}

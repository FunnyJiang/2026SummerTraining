package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 食谱健康安排模型
 */
public class RecipeSchedule {
    private int id;
    private int recipeId;
    private String recipeName;
    private Integer userId;
    private String userName;
    private String mealType;
    private Date scheduleDate;
    private Date periodStart;
    private Date periodEnd;
    private String targetBmiRange;
    private String notes;
    private Date createTime;

    public RecipeSchedule() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getRecipeId() { return recipeId; }
    public void setRecipeId(int recipeId) { this.recipeId = recipeId; }
    public String getRecipeName() { return recipeName; }
    public void setRecipeName(String recipeName) { this.recipeName = recipeName; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public Date getScheduleDate() { return scheduleDate; }
    public void setScheduleDate(Date scheduleDate) { this.scheduleDate = scheduleDate; }
    public Date getPeriodStart() { return periodStart; }
    public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }
    public Date getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(Date periodEnd) { this.periodEnd = periodEnd; }
    public String getTargetBmiRange() { return targetBmiRange; }
    public void setTargetBmiRange(String targetBmiRange) { this.targetBmiRange = targetBmiRange; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getScheduleDateStr() {
        if (scheduleDate == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd").format(scheduleDate);
    }

    public String getPeriodStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String start = periodStart == null ? "" : sdf.format(periodStart);
        String end = periodEnd == null ? "" : sdf.format(periodEnd);
        if (start.isEmpty() && end.isEmpty()) return "";
        return start + " ~ " + end;
    }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}

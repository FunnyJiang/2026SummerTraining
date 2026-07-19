package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 食谱分类模型
 */
public class RecipeCategory {
    private int id;
    private String name;
    private String description;
    private Date createTime;

    public RecipeCategory() {}

    public RecipeCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }

    @Override
    public String toString() {
        return name;
    }
}

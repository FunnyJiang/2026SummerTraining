package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 健康知识库模型
 */
public class HealthTip {
    private int id;
    private String title;
    private String content;
    private String category;  // BMI知识/营养知识/运动知识/心理健康/饮食误区
    private String author;
    private int views;
    private Date createTime;

    public HealthTip() {}

    public HealthTip(String title, String content, String category, String author) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.author = author;
        this.views = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd").format(createTime);
    }
}

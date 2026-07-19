package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 论坛帖子模型
 */
public class ForumPost {
    private int id;
    private int userId;
    private String userName;
    private String title;
    private String content;
    private String category;  // BMI健康讨论/减脂经验/增肌分享/营养知识/综合讨论
    private int views;
    private int likes;
    private int isHot;        // 是否热门: 1是 0否
    private Date createTime;

    public ForumPost() {}

    public ForumPost(int userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.category = "综合讨论";
        this.views = 0;
        this.likes = 0;
        this.isHot = 0;
    }

    public ForumPost(int userId, String title, String content, String category) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.views = 0;
        this.likes = 0;
        this.isHot = 0;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }
    public int getIsHot() { return isHot; }
    public void setIsHot(int isHot) { this.isHot = isHot; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}

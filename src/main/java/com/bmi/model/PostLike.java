package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 帖子点赞模型
 */
public class PostLike {
    private int id;
    private int postId;
    private int userId;
    private Date createTime;

    public PostLike() {}

    public PostLike(int postId, int userId) {
        this.postId = postId;
        this.userId = userId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}

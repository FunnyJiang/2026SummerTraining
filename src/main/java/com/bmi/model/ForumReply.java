package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 论坛回复模型
 */
public class ForumReply {
    private int id;
    private int postId;
    private int userId;
    private String userName;
    private String content;
    private Date createTime;

    public ForumReply() {}

    public ForumReply(int postId, int userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }
}

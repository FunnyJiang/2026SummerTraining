package com.bmi.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 用户模型
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String realName;
    private String gender;
    private int age;
    private double height;
    private double weight;
    private String phone;
    private String email;
    private String role;
    private int status;
    private Date createTime;

    public User() {}

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = 1;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getCreateTimeStr() {
        if (createTime == null) return "";
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }

    public String getStatusStr() {
        return status == 1 ? "启用" : "禁用";
    }

    public String getRoleStr() {
        return "admin".equals(role) ? "管理员" : "普通用户";
    }

    public double getBMI() {
        if (height <= 0) return 0;
        double h = height / 100.0;
        return Math.round(weight / (h * h) * 100.0) / 100.0;
    }
}

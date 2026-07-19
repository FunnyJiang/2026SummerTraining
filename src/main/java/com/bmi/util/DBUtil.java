package com.bmi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库工具类 - 管理数据库连接
 * 
 * 重要：每次操作都获取新连接，操作完成后关闭连接。
 * 不使用单例连接模式，避免连接被意外关闭后无法恢复的问题。
 */
public class DBUtil {
    // 数据库连接配置 - 请根据你的MySQL配置修改
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/bmi_health_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf8&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "mysql070103";

    /**
     * 获取数据库连接 - 每次调用都创建新连接
     */
    public static Connection getConnection() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            // 常见原因: lib/mysql-connector-j.jar 未加入 classpath (IDEA 项目依赖未刷新 / Maven 依赖坐标错误)
            System.err.println("MySQL驱动加载失败: " + DRIVER);
            System.err.println("可能原因: lib/mysql-connector-j.jar 不在 classpath 中,或 pom.xml 依赖坐标错误。");
            System.err.println("建议: 1) IDEA 中右键 pom.xml -> Maven -> Reload project; 2) 确认依赖为 com.mysql:mysql-connector-j:8.0.33");
            e.printStackTrace();
        } catch (SQLException e) {
            // 常见原因: MySQL 服务未启动 / 端口错误 / 用户名或密码错误 / 数据库未创建
            System.err.println("数据库连接失败: " + e.getMessage());
            System.err.println("可能原因: 1) MySQL 服务未启动; 2) 用户名/密码错误; 3) 数据库 bmi_health_db 未初始化; 4) 端口非 3306");
        }
        return null;
    }

    /**
     * 关闭资源 - 按顺序关闭 ResultSet, PreparedStatement, Connection
     */
    public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) { /* ignore */ }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) { /* ignore */ }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) { /* ignore */ }
    }

    public static void close(PreparedStatement ps, Connection conn) {
        close(null, ps, conn);
    }

    public static void close(Connection conn) {
        close(null, null, conn);
    }

    /**
     * 测试数据库连接
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null) {
                System.out.println("数据库连接成功！");
                conn.close();
                return true;
            }
        } catch (SQLException e) {
            System.err.println("数据库连接测试失败: " + e.getMessage());
        } finally {
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { /* ignore */ }
            }
        }
        return false;
    }
}

package com.bmi.dao;

import com.bmi.model.DietRecord;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 膳食摄入记录数据访问对象
 */
public class DietRecordDAO {

    private static final String CREATE_TABLE_SQL =
        "CREATE TABLE IF NOT EXISTS diet_records (" +
        "id INT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID', " +
        "user_id INT NOT NULL COMMENT '用户ID', " +
        "record_date DATE NOT NULL COMMENT '摄入日期', " +
        "carbs DOUBLE DEFAULT 0 COMMENT '碳水化合物(g)', " +
        "fat DOUBLE DEFAULT 0 COMMENT '脂肪(g)', " +
        "protein DOUBLE DEFAULT 0 COMMENT '蛋白质(g)', " +
        "calories DOUBLE DEFAULT 0 COMMENT '热量(大卡)', " +
        "fiber DOUBLE DEFAULT 0 COMMENT '膳食纤维(g)', " +
        "cholesterol DOUBLE DEFAULT 0 COMMENT '胆固醇(mmol)', " +
        "water DOUBLE DEFAULT 0 COMMENT '饮水量(ml)', " +
        "note VARCHAR(200) COMMENT '备注', " +
        "create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间', " +
        "UNIQUE KEY uk_user_date (user_id, record_date), " +
        "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE " +
        ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='膳食摄入记录表'";

    public DietRecordDAO() {
        ensureTableExists();
    }

    private void ensureTableExists() {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = DBUtil.getConnection();
            if (conn == null) return;
            stmt = conn.createStatement();
            stmt.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            // 表已存在且结构一致时会报错, 这里仅打印避免阻塞
            System.err.println("DietRecordDAO 自动建表异常: " + e.getMessage());
        } finally {
            try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * 添加一条膳食摄入记录
     */
    public boolean add(DietRecord r) {
        String sql = "INSERT INTO diet_records (user_id, record_date, carbs, fat, protein, calories, " +
                "fiber, cholesterol, water, note) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, r.getUserId());
            ps.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(r.getRecordDate()));
            ps.setDouble(3, r.getCarbs());
            ps.setDouble(4, r.getFat());
            ps.setDouble(5, r.getProtein());
            ps.setDouble(6, r.getCalories());
            ps.setDouble(7, r.getFiber());
            ps.setDouble(8, r.getCholesterol());
            ps.setDouble(9, r.getWater());
            ps.setString(10, r.getNote());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("DietRecordDAO.add 失败: " + e.getMessage(), e);
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /**
     * 按 id 更新一条膳食记录
     */
    public boolean update(DietRecord r) {
        String sql = "UPDATE diet_records SET carbs=?, fat=?, protein=?, calories=?, " +
                "fiber=?, cholesterol=?, water=?, note=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, r.getCarbs());
            ps.setDouble(2, r.getFat());
            ps.setDouble(3, r.getProtein());
            ps.setDouble(4, r.getCalories());
            ps.setDouble(5, r.getFiber());
            ps.setDouble(6, r.getCholesterol());
            ps.setDouble(7, r.getWater());
            ps.setString(8, r.getNote());
            ps.setInt(9, r.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("DietRecordDAO.update 失败: " + e.getMessage(), e);
        } finally {
            DBUtil.close(ps, conn);
        }
    }

    /**
     * 按用户+日期查找记录
     */
    public DietRecord findByDate(int userId, java.util.Date date) {
        String sql = "SELECT * FROM diet_records WHERE user_id=? AND record_date=? LIMIT 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(date));
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    /**
     * 查询某用户的所有膳食记录（按日期降序）
     */
    public List<DietRecord> findByUserId(int userId) {
        List<DietRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM diet_records WHERE user_id = ? ORDER BY record_date DESC, id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 查询某用户最近N天的膳食记录（按日期升序）
     */
    public List<DietRecord> findRecentByUserId(int userId, int days) {
        List<DietRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM diet_records WHERE user_id = ? " +
                "ORDER BY record_date DESC, id DESC LIMIT ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, days);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        // 升序返回, 便于图表从左到右按时间绘制
        java.util.Collections.reverse(list);
        return list;
    }

    /**
     * 删除一条记录
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM diet_records WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 检查某用户某天是否已有记录
     */
    public boolean existsByDate(int userId, java.util.Date date) {
        String sql = "SELECT COUNT(*) FROM diet_records WHERE user_id = ? AND record_date = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(date));
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return false;
    }

    private DietRecord mapRow(ResultSet rs) throws SQLException {
        DietRecord r = new DietRecord();
        r.setId(rs.getInt("id"));
        r.setUserId(rs.getInt("user_id"));
        r.setRecordDate(rs.getDate("record_date"));
        r.setCarbs(rs.getDouble("carbs"));
        r.setFat(rs.getDouble("fat"));
        r.setProtein(rs.getDouble("protein"));
        r.setCalories(rs.getDouble("calories"));
        r.setFiber(rs.getDouble("fiber"));
        r.setCholesterol(rs.getDouble("cholesterol"));
        r.setWater(rs.getDouble("water"));
        r.setNote(rs.getString("note"));
        r.setCreateTime(rs.getTimestamp("create_time"));
        return r;
    }
}

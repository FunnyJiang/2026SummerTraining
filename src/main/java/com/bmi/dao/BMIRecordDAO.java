package com.bmi.dao;

import com.bmi.model.BMIRecord;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BMI记录数据访问对象
 */
public class BMIRecordDAO {

    /**
     * 查询用户的BMI记录
     */
    public List<BMIRecord> findByUserId(int userId) {
        List<BMIRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM bmi_records WHERE user_id = ? ORDER BY id DESC";
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
     * 添加BMI记录
     */
    public boolean add(BMIRecord record) {
        String sql = "INSERT INTO bmi_records (user_id, height, weight, bmi, assessment, recommendation) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, record.getUserId());
            ps.setDouble(2, record.getHeight());
            ps.setDouble(3, record.getWeight());
            ps.setDouble(4, record.getBmi());
            ps.setString(5, record.getAssessment());
            ps.setString(6, record.getRecommendation());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 删除BMI记录
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM bmi_records WHERE id = ?";
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
     * 统计所有BMI记录数
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM bmi_records";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return 0;
    }

    private BMIRecord mapRow(ResultSet rs) throws SQLException {
        BMIRecord record = new BMIRecord();
        record.setId(rs.getInt("id"));
        record.setUserId(rs.getInt("user_id"));
        record.setHeight(rs.getDouble("height"));
        record.setWeight(rs.getDouble("weight"));
        record.setBmi(rs.getDouble("bmi"));
        record.setAssessment(rs.getString("assessment"));
        record.setRecommendation(rs.getString("recommendation"));
        record.setCreateTime(rs.getTimestamp("create_time"));
        return record;
    }
}

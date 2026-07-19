package com.bmi.dao;

import com.bmi.model.ExerciseRecommendation;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 运动推荐数据访问对象
 */
public class ExerciseDAO {

    public List<ExerciseRecommendation> findAll() {
        List<ExerciseRecommendation> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_recommendations ORDER BY id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
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

    public List<ExerciseRecommendation> findBySuitableBmi(String bmiRange) {
        List<ExerciseRecommendation> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_recommendations WHERE suitable_bmi = ? ORDER BY id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bmiRange);
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

    public List<ExerciseRecommendation> findByCategory(String category) {
        List<ExerciseRecommendation> list = new ArrayList<>();
        String sql = "SELECT * FROM exercise_recommendations WHERE category = ? ORDER BY id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, category);
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

    public ExerciseRecommendation findById(int id) {
        String sql = "SELECT * FROM exercise_recommendations WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    public boolean add(ExerciseRecommendation ex) {
        String sql = "INSERT INTO exercise_recommendations (name, description, duration, frequency, calories_burned, suitable_bmi, difficulty, category) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, ex.getName());
            ps.setString(2, ex.getDescription());
            ps.setInt(3, ex.getDuration());
            ps.setString(4, ex.getFrequency());
            ps.setDouble(5, ex.getCaloriesBurned());
            ps.setString(6, ex.getSuitableBmi());
            ps.setString(7, ex.getDifficulty());
            ps.setString(8, ex.getCategory());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean update(ExerciseRecommendation ex) {
        String sql = "UPDATE exercise_recommendations SET name=?, description=?, duration=?, frequency=?, calories_burned=?, suitable_bmi=?, difficulty=?, category=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, ex.getName());
            ps.setString(2, ex.getDescription());
            ps.setInt(3, ex.getDuration());
            ps.setString(4, ex.getFrequency());
            ps.setDouble(5, ex.getCaloriesBurned());
            ps.setString(6, ex.getSuitableBmi());
            ps.setString(7, ex.getDifficulty());
            ps.setString(8, ex.getCategory());
            ps.setInt(9, ex.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM exercise_recommendations WHERE id = ?";
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

    public int count() {
        String sql = "SELECT COUNT(*) FROM exercise_recommendations";
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

    private ExerciseRecommendation mapRow(ResultSet rs) throws SQLException {
        ExerciseRecommendation ex = new ExerciseRecommendation();
        ex.setId(rs.getInt("id"));
        ex.setName(rs.getString("name"));
        ex.setDescription(rs.getString("description"));
        ex.setDuration(rs.getInt("duration"));
        ex.setFrequency(rs.getString("frequency"));
        ex.setCaloriesBurned(rs.getDouble("calories_burned"));
        ex.setSuitableBmi(rs.getString("suitable_bmi"));
        ex.setDifficulty(rs.getString("difficulty"));
        ex.setCategory(rs.getString("category"));
        ex.setCreateTime(rs.getTimestamp("create_time"));
        return ex;
    }
}

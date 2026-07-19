package com.bmi.dao;

import com.bmi.model.RecipeSchedule;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 食谱健康安排数据访问对象
 */
public class RecipeScheduleDAO {

    public List<RecipeSchedule> findAll() {
        List<RecipeSchedule> list = new ArrayList<>();
        String sql = "SELECT s.*, r.name AS recipe_name, u.username AS user_name " +
                "FROM recipe_schedules s " +
                "LEFT JOIN recipes r ON s.recipe_id = r.id " +
                "LEFT JOIN users u ON s.user_id = u.id " +
                "ORDER BY s.id DESC";
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

    /**
     * 查询用户的食谱安排
     */
    public List<RecipeSchedule> findByUserId(int userId) {
        List<RecipeSchedule> list = new ArrayList<>();
        String sql = "SELECT s.*, r.name AS recipe_name, u.username AS user_name " +
                "FROM recipe_schedules s " +
                "LEFT JOIN recipes r ON s.recipe_id = r.id " +
                "LEFT JOIN users u ON s.user_id = u.id " +
                "WHERE s.user_id = ? OR s.user_id IS NULL ORDER BY s.id DESC";
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

    public boolean add(RecipeSchedule schedule) {
        String sql = "INSERT INTO recipe_schedules (recipe_id, user_id, meal_type, schedule_date, " +
                "period_start, period_end, target_bmi_range, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, schedule.getRecipeId());
            if (schedule.getUserId() != null) {
                ps.setInt(2, schedule.getUserId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, schedule.getMealType());
            if (schedule.getScheduleDate() != null) {
                ps.setDate(4, new java.sql.Date(schedule.getScheduleDate().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            if (schedule.getPeriodStart() != null) {
                ps.setDate(5, new java.sql.Date(schedule.getPeriodStart().getTime()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            if (schedule.getPeriodEnd() != null) {
                ps.setDate(6, new java.sql.Date(schedule.getPeriodEnd().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            ps.setString(7, schedule.getTargetBmiRange());
            ps.setString(8, schedule.getNotes());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean update(RecipeSchedule schedule) {
        String sql = "UPDATE recipe_schedules SET recipe_id=?, user_id=?, meal_type=?, schedule_date=?, " +
                "period_start=?, period_end=?, target_bmi_range=?, notes=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, schedule.getRecipeId());
            if (schedule.getUserId() != null) {
                ps.setInt(2, schedule.getUserId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, schedule.getMealType());
            if (schedule.getScheduleDate() != null) {
                ps.setDate(4, new java.sql.Date(schedule.getScheduleDate().getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            if (schedule.getPeriodStart() != null) {
                ps.setDate(5, new java.sql.Date(schedule.getPeriodStart().getTime()));
            } else {
                ps.setNull(5, Types.DATE);
            }
            if (schedule.getPeriodEnd() != null) {
                ps.setDate(6, new java.sql.Date(schedule.getPeriodEnd().getTime()));
            } else {
                ps.setNull(6, Types.DATE);
            }
            ps.setString(7, schedule.getTargetBmiRange());
            ps.setString(8, schedule.getNotes());
            ps.setInt(9, schedule.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM recipe_schedules WHERE id = ?";
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

    private RecipeSchedule mapRow(ResultSet rs) throws SQLException {
        RecipeSchedule schedule = new RecipeSchedule();
        schedule.setId(rs.getInt("id"));
        schedule.setRecipeId(rs.getInt("recipe_id"));
        schedule.setRecipeName(rs.getString("recipe_name"));
        int userId = rs.getInt("user_id");
        if (rs.wasNull()) {
            schedule.setUserId(null);
            schedule.setUserName("通用");
        } else {
            schedule.setUserId(userId);
            schedule.setUserName(rs.getString("user_name"));
        }
        schedule.setMealType(rs.getString("meal_type"));
        schedule.setScheduleDate(rs.getDate("schedule_date"));
        schedule.setPeriodStart(rs.getDate("period_start"));
        schedule.setPeriodEnd(rs.getDate("period_end"));
        schedule.setTargetBmiRange(rs.getString("target_bmi_range"));
        schedule.setNotes(rs.getString("notes"));
        schedule.setCreateTime(rs.getTimestamp("create_time"));
        return schedule;
    }
}

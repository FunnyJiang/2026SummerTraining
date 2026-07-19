package com.bmi.dao;

import com.bmi.model.Ingredient;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 材料信息数据访问对象
 */
public class IngredientDAO {

    public List<Ingredient> findAll() {
        return search(null);
    }

    public List<Ingredient> search(String keyword) {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingredients WHERE 1=1";
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND (name LIKE ? OR category LIKE ?)";
        }
        sql += " ORDER BY id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            if (keyword != null && !keyword.trim().isEmpty()) {
                String kw = "%" + keyword + "%";
                ps.setString(1, kw);
                ps.setString(2, kw);
            }
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

    public Ingredient findById(int id) {
        String sql = "SELECT * FROM ingredients WHERE id = ?";
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

    public boolean add(Ingredient ingredient) {
        String sql = "INSERT INTO ingredients (name, description, calories, unit, category) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, ingredient.getName());
            ps.setString(2, ingredient.getDescription());
            ps.setDouble(3, ingredient.getCalories());
            ps.setString(4, ingredient.getUnit());
            ps.setString(5, ingredient.getCategory());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean update(Ingredient ingredient) {
        String sql = "UPDATE ingredients SET name=?, description=?, calories=?, unit=?, category=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, ingredient.getName());
            ps.setString(2, ingredient.getDescription());
            ps.setDouble(3, ingredient.getCalories());
            ps.setString(4, ingredient.getUnit());
            ps.setString(5, ingredient.getCategory());
            ps.setInt(6, ingredient.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM ingredients WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM ingredients";
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

    private Ingredient mapRow(ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getInt("id"));
        ingredient.setName(rs.getString("name"));
        ingredient.setDescription(rs.getString("description"));
        ingredient.setCalories(rs.getDouble("calories"));
        ingredient.setUnit(rs.getString("unit"));
        ingredient.setCategory(rs.getString("category"));
        ingredient.setCreateTime(rs.getTimestamp("create_time"));
        return ingredient;
    }
}

package com.bmi.dao;

import com.bmi.model.Favorite;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏数据访问对象
 */
public class FavoriteDAO {

    /**
     * 查询用户的收藏列表
     */
    public List<Favorite> findByUserId(int userId) {
        List<Favorite> list = new ArrayList<>();
        String sql = "SELECT f.*, r.name AS recipe_name, r.calories, c.name AS category_name " +
                "FROM favorites f " +
                "LEFT JOIN recipes r ON f.recipe_id = r.id " +
                "LEFT JOIN recipe_categories c ON r.category_id = c.id " +
                "WHERE f.user_id = ? ORDER BY f.id DESC";
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
     * 添加收藏
     */
    public boolean add(int userId, int recipeId) {
        String sql = "INSERT IGNORE INTO favorites (user_id, recipe_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, recipeId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 删除收藏
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM favorites WHERE id = ?";
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
     * 检查是否已收藏
     */
    public boolean isFavorited(int userId, int recipeId) {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND recipe_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, recipeId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return false;
    }

    private Favorite mapRow(ResultSet rs) throws SQLException {
        Favorite fav = new Favorite();
        fav.setId(rs.getInt("id"));
        fav.setUserId(rs.getInt("user_id"));
        fav.setRecipeId(rs.getInt("recipe_id"));
        fav.setRecipeName(rs.getString("recipe_name"));
        fav.setCategoryName(rs.getString("category_name"));
        fav.setCalories(rs.getDouble("calories"));
        fav.setCreateTime(rs.getTimestamp("create_time"));
        return fav;
    }
}

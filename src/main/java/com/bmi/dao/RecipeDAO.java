package com.bmi.dao;

import com.bmi.model.Recipe;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 食谱信息数据访问对象
 */
public class RecipeDAO {

    public List<Recipe> findAll() {
        return search(null, 0);
    }

    /**
     * 搜索食谱
     * @param keyword 关键词(名称)
     * @param categoryId 分类ID(0=全部)
     */
    public List<Recipe> search(String keyword, int categoryId) {
        List<Recipe> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT r.*, c.name AS category_name FROM recipes r " +
            "LEFT JOIN recipe_categories c ON r.category_id = c.id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND r.name LIKE ?");
            params.add("%" + keyword + "%");
        }
        if (categoryId > 0) {
            sql.append(" AND r.category_id = ?");
            params.add(categoryId);
        }
        sql.append(" ORDER BY r.id DESC");
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
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

    /**
     * 根据适合的BMI范围查询食谱
     */
    public List<Recipe> findBySuitableBmi(String bmiRange) {
        List<Recipe> list = new ArrayList<>();
        String sql = "SELECT r.*, c.name AS category_name FROM recipes r " +
                "LEFT JOIN recipe_categories c ON r.category_id = c.id " +
                "WHERE r.suitable_bmi = ? OR r.suitable_bmi LIKE ? ORDER BY r.id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bmiRange);
            ps.setString(2, "%" + bmiRange + "%");
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

    public Recipe findById(int id) {
        String sql = "SELECT r.*, c.name AS category_name FROM recipes r " +
                "LEFT JOIN recipe_categories c ON r.category_id = c.id WHERE r.id = ?";
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

    public boolean add(Recipe recipe) {
        String sql = "INSERT INTO recipes (category_id, name, description, calories, protein, fat, carbs, suitable_bmi, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, recipe.getCategoryId());
            ps.setString(2, recipe.getName());
            ps.setString(3, recipe.getDescription());
            ps.setDouble(4, recipe.getCalories());
            ps.setDouble(5, recipe.getProtein());
            ps.setDouble(6, recipe.getFat());
            ps.setDouble(7, recipe.getCarbs());
            ps.setString(8, recipe.getSuitableBmi());
            ps.setString(9, recipe.getImageUrl());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean update(Recipe recipe) {
        String sql = "UPDATE recipes SET category_id=?, name=?, description=?, calories=?, protein=?, fat=?, carbs=?, " +
                "suitable_bmi=?, image_url=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, recipe.getCategoryId());
            ps.setString(2, recipe.getName());
            ps.setString(3, recipe.getDescription());
            ps.setDouble(4, recipe.getCalories());
            ps.setDouble(5, recipe.getProtein());
            ps.setDouble(6, recipe.getFat());
            ps.setDouble(7, recipe.getCarbs());
            ps.setString(8, recipe.getSuitableBmi());
            ps.setString(9, recipe.getImageUrl());
            ps.setInt(10, recipe.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM recipes";
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

    private Recipe mapRow(ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getInt("id"));
        recipe.setCategoryId(rs.getInt("category_id"));
        recipe.setCategoryName(rs.getString("category_name"));
        recipe.setName(rs.getString("name"));
        recipe.setDescription(rs.getString("description"));
        recipe.setCalories(rs.getDouble("calories"));
        recipe.setProtein(rs.getDouble("protein"));
        recipe.setFat(rs.getDouble("fat"));
        recipe.setCarbs(rs.getDouble("carbs"));
        recipe.setSuitableBmi(rs.getString("suitable_bmi"));
        recipe.setImageUrl(rs.getString("image_url"));
        recipe.setCreateTime(rs.getTimestamp("create_time"));
        return recipe;
    }
}

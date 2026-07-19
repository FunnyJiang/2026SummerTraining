package com.bmi.dao;

import com.bmi.model.HealthTip;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 健康知识库数据访问对象
 */
public class HealthTipDAO {

    public List<HealthTip> findAll() {
        List<HealthTip> list = new ArrayList<>();
        String sql = "SELECT * FROM health_tips ORDER BY id DESC";
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

    public List<HealthTip> findByCategory(String category) {
        List<HealthTip> list = new ArrayList<>();
        String sql = "SELECT * FROM health_tips WHERE category = ? ORDER BY id DESC";
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

    public List<HealthTip> search(String keyword) {
        List<HealthTip> list = new ArrayList<>();
        String sql = "SELECT * FROM health_tips WHERE title LIKE ? OR content LIKE ? ORDER BY id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
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

    public HealthTip findById(int id) {
        String sql = "SELECT * FROM health_tips WHERE id = ?";
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

    public boolean add(HealthTip tip) {
        String sql = "INSERT INTO health_tips (title, content, category, author) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, tip.getTitle());
            ps.setString(2, tip.getContent());
            ps.setString(3, tip.getCategory());
            ps.setString(4, tip.getAuthor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean update(HealthTip tip) {
        String sql = "UPDATE health_tips SET title=?, content=?, category=?, author=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, tip.getTitle());
            ps.setString(2, tip.getContent());
            ps.setString(3, tip.getCategory());
            ps.setString(4, tip.getAuthor());
            ps.setInt(5, tip.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM health_tips WHERE id = ?";
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

    public boolean incrementViews(int id) {
        String sql = "UPDATE health_tips SET views = views + 1 WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM health_tips";
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

    private HealthTip mapRow(ResultSet rs) throws SQLException {
        HealthTip tip = new HealthTip();
        tip.setId(rs.getInt("id"));
        tip.setTitle(rs.getString("title"));
        tip.setContent(rs.getString("content"));
        tip.setCategory(rs.getString("category"));
        tip.setAuthor(rs.getString("author"));
        tip.setViews(rs.getInt("views"));
        tip.setCreateTime(rs.getTimestamp("create_time"));
        return tip;
    }
}

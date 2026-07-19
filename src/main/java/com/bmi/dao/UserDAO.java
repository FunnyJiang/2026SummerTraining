package com.bmi.dao;

import com.bmi.model.User;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象
 */
public class UserDAO {

    /**
     * 用户登录验证
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    /**
     * 检查用户名是否存在
     */
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return false;
    }

    /**
     * 注册用户
     */
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, real_name, gender, age, height, weight, phone, email, role, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRealName());
            ps.setString(4, user.getGender());
            ps.setInt(5, user.getAge());
            ps.setDouble(6, user.getHeight());
            ps.setDouble(7, user.getWeight());
            ps.setString(8, user.getPhone());
            ps.setString(9, user.getEmail());
            ps.setString(10, user.getRole());
            ps.setInt(11, 1);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 查询所有用户
     */
    public List<User> findAll() {
        return findAll(null);
    }

    /**
     * 按关键词查询用户
     */
    public List<User> findAll(String keyword) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE 1=1";
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " AND (username LIKE ? OR real_name LIKE ? OR phone LIKE ?)";
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
                ps.setString(3, kw);
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
     * 根据ID查询用户
     */
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    /**
     * 添加用户
     */
    public boolean add(User user) {
        return register(user);
    }

    /**
     * 更新用户
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username=?, password=?, real_name=?, gender=?, age=?, height=?, weight=?, " +
                "phone=?, email=?, role=?, status=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRealName());
            ps.setString(4, user.getGender());
            ps.setInt(5, user.getAge());
            ps.setDouble(6, user.getHeight());
            ps.setDouble(7, user.getWeight());
            ps.setString(8, user.getPhone());
            ps.setString(9, user.getEmail());
            ps.setString(10, user.getRole());
            ps.setInt(11, user.getStatus());
            ps.setInt(12, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 更新个人信息（不含用户名和角色）
     */
    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET password=?, real_name=?, gender=?, age=?, height=?, weight=?, " +
                "phone=?, email=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getRealName());
            ps.setString(3, user.getGender());
            ps.setInt(4, user.getAge());
            ps.setDouble(5, user.getHeight());
            ps.setDouble(6, user.getWeight());
            ps.setString(7, user.getPhone());
            ps.setString(8, user.getEmail());
            ps.setInt(9, user.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 删除用户
     */
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";
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
     * 切换用户状态
     */
    public boolean toggleStatus(int id) {
        String sql = "UPDATE users SET status = IF(status = 1, 0, 1) WHERE id = ?";
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
     * 统计用户数量
     */
    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE role = 'user'";
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

    /**
     * 映射结果集到User对象
     */
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRealName(rs.getString("real_name"));
        user.setGender(rs.getString("gender"));
        user.setAge(rs.getInt("age"));
        user.setHeight(rs.getDouble("height"));
        user.setWeight(rs.getDouble("weight"));
        user.setPhone(rs.getString("phone"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getInt("status"));
        user.setCreateTime(rs.getTimestamp("create_time"));
        return user;
    }
}

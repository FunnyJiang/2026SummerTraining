package com.bmi.dao;

import com.bmi.model.ForumPost;
import com.bmi.model.ForumReply;
import com.bmi.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 论坛数据访问对象
 */
public class ForumDAO {

    /**
     * 查询所有帖子
     */
    public List<ForumPost> findAllPosts() {
        List<ForumPost> list = new ArrayList<>();
        String sql = "SELECT p.*, u.username AS user_name FROM forum_posts p " +
                "LEFT JOIN users u ON p.user_id = u.id ORDER BY p.id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapPostRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 搜索帖子
     */
    public List<ForumPost> searchPosts(String keyword) {
        List<ForumPost> list = new ArrayList<>();
        String sql = "SELECT p.*, u.username AS user_name FROM forum_posts p " +
                "LEFT JOIN users u ON p.user_id = u.id " +
                "WHERE p.title LIKE ? OR p.content LIKE ? ORDER BY p.id DESC";
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
                list.add(mapPostRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 根据ID查询帖子
     */
    public ForumPost findPostById(int id) {
        String sql = "SELECT p.*, u.username AS user_name FROM forum_posts p " +
                "LEFT JOIN users u ON p.user_id = u.id WHERE p.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) return mapPostRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    /**
     * 添加帖子（含分类字段）
     */
    public boolean addPost(ForumPost post) {
        String sql = "INSERT INTO forum_posts (user_id, title, content, category) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getTitle());
            ps.setString(3, post.getContent());
            ps.setString(4, post.getCategory() != null ? post.getCategory() : "综合讨论");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 更新帖子点赞数（+1 或 -1）
     */
    public boolean updatePostLikes(int postId, int delta) {
        String sql = "UPDATE forum_posts SET likes = likes + ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, delta);
            ps.setInt(2, postId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 删除帖子
     */
    public boolean deletePost(int id) {
        String sql = "DELETE FROM forum_posts WHERE id = ?";
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
     * 增加浏览量
     */
    public boolean incrementViews(int postId) {
        String sql = "UPDATE forum_posts SET views = views + 1 WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 查询帖子回复
     */
    public List<ForumReply> findRepliesByPostId(int postId) {
        List<ForumReply> list = new ArrayList<>();
        String sql = "SELECT r.*, u.username AS user_name FROM forum_replies r " +
                "LEFT JOIN users u ON r.user_id = u.id " +
                "WHERE r.post_id = ? ORDER BY r.id ASC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, postId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapReplyRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    /**
     * 添加回复
     */
    public boolean addReply(ForumReply reply) {
        String sql = "INSERT INTO forum_replies (post_id, user_id, content) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, reply.getPostId());
            ps.setInt(2, reply.getUserId());
            ps.setString(3, reply.getContent());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(ps, conn);
        }
        return false;
    }

    /**
     * 删除回复
     */
    public boolean deleteReply(int id) {
        String sql = "DELETE FROM forum_replies WHERE id = ?";
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

    private ForumPost mapPostRow(ResultSet rs) throws SQLException {
        ForumPost post = new ForumPost();
        post.setId(rs.getInt("id"));
        post.setUserId(rs.getInt("user_id"));
        post.setUserName(rs.getString("user_name"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setCategory(rs.getString("category"));
        post.setViews(rs.getInt("views"));
        post.setLikes(rs.getInt("likes"));
        post.setIsHot(rs.getInt("is_hot"));
        post.setCreateTime(rs.getTimestamp("create_time"));
        return post;
    }

    private ForumReply mapReplyRow(ResultSet rs) throws SQLException {
        ForumReply reply = new ForumReply();
        reply.setId(rs.getInt("id"));
        reply.setPostId(rs.getInt("post_id"));
        reply.setUserId(rs.getInt("user_id"));
        reply.setUserName(rs.getString("user_name"));
        reply.setContent(rs.getString("content"));
        reply.setCreateTime(rs.getTimestamp("create_time"));
        return reply;
    }
}

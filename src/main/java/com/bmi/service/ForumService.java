package com.bmi.service;

import com.bmi.dao.FavoriteDAO;
import com.bmi.dao.ForumDAO;
import com.bmi.dao.RecipeScheduleDAO;
import com.bmi.model.*;

import java.util.List;

/**
 * 论坛与收藏服务层
 */
public class ForumService {
    private ForumDAO forumDAO = new ForumDAO();
    private FavoriteDAO favoriteDAO = new FavoriteDAO();
    private RecipeScheduleDAO scheduleDAO = new RecipeScheduleDAO();

    // 论坛帖子
    public List<ForumPost> findAllPosts() {
        return forumDAO.findAllPosts();
    }

    public List<ForumPost> searchPosts(String keyword) {
        return forumDAO.searchPosts(keyword);
    }

    public ForumPost findPostById(int id) {
        return forumDAO.findPostById(id);
    }

    public boolean addPost(ForumPost post) {
        return forumDAO.addPost(post);
    }

    public boolean deletePost(int id) {
        return forumDAO.deletePost(id);
    }

    public boolean incrementViews(int postId) {
        return forumDAO.incrementViews(postId);
    }

    /**
     * 更新帖子点赞数（+1 或 -1）
     */
    public boolean updatePostLikes(int postId, int delta) {
        return forumDAO.updatePostLikes(postId, delta);
    }

    // 论坛回复
    public List<ForumReply> findRepliesByPostId(int postId) {
        return forumDAO.findRepliesByPostId(postId);
    }

    public boolean addReply(ForumReply reply) {
        return forumDAO.addReply(reply);
    }

    public boolean deleteReply(int id) {
        return forumDAO.deleteReply(id);
    }

    // 收藏
    public List<Favorite> findFavoritesByUserId(int userId) {
        return favoriteDAO.findByUserId(userId);
    }

    public boolean addFavorite(int userId, int recipeId) {
        return favoriteDAO.add(userId, recipeId);
    }

    public boolean deleteFavorite(int id) {
        return favoriteDAO.delete(id);
    }

    public boolean isFavorited(int userId, int recipeId) {
        return favoriteDAO.isFavorited(userId, recipeId);
    }

    // 食谱安排
    public List<RecipeSchedule> findAllSchedules() {
        return scheduleDAO.findAll();
    }

    public List<RecipeSchedule> findSchedulesByUserId(int userId) {
        return scheduleDAO.findByUserId(userId);
    }

    public boolean addSchedule(RecipeSchedule schedule) {
        return scheduleDAO.add(schedule);
    }

    public boolean updateSchedule(RecipeSchedule schedule) {
        return scheduleDAO.update(schedule);
    }

    public boolean deleteSchedule(int id) {
        return scheduleDAO.delete(id);
    }
}

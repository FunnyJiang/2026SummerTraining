package com.bmi.service;

import com.bmi.dao.UserDAO;
import com.bmi.model.User;

import java.util.List;

/**
 * 用户服务层
 */
public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User login(String username, String password) {
        return userDAO.login(username, password);
    }

    public boolean register(User user) {
        if (userDAO.isUsernameExists(user.getUsername())) {
            return false;
        }
        return userDAO.register(user);
    }

    public List<User> findAllUsers() {
        return userDAO.findAll();
    }

    public List<User> searchUsers(String keyword) {
        return userDAO.findAll(keyword);
    }

    public User findById(int id) {
        return userDAO.findById(id);
    }

    public boolean addUser(User user) {
        if (userDAO.isUsernameExists(user.getUsername())) {
            return false;
        }
        return userDAO.add(user);
    }

    public boolean updateUser(User user) {
        return userDAO.update(user);
    }

    public boolean updateProfile(User user) {
        return userDAO.updateProfile(user);
    }

    public boolean deleteUser(int id) {
        return userDAO.delete(id);
    }

    public boolean toggleStatus(int id) {
        return userDAO.toggleStatus(id);
    }

    public boolean isUsernameExists(String username) {
        return userDAO.isUsernameExists(username);
    }

    public int countUsers() {
        return userDAO.countUsers();
    }
}

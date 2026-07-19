package com.bmi.ui.admin;

import com.bmi.model.User;
import com.bmi.service.BMIService;
import com.bmi.service.RecipeService;
import com.bmi.service.UserService;
import com.bmi.util.DBUtil;
import com.bmi.util.UIUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 系统管理面板
 */
public class SystemManagePanel extends JPanel {
    private User currentUser;
    private UserService userService = new UserService();
    private RecipeService recipeService = new RecipeService();
    private BMIService bmiService = new BMIService();

    public SystemManagePanel(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.USER_BG);

        // 标题
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        JLabel titleLabel = new JLabel("系统管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 内容区
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIUtil.USER_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // 数据库状态卡片
        JPanel dbPanel = new JPanel(new BorderLayout());
        dbPanel.setBackground(Color.WHITE);
        dbPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel dbTitle = new JLabel("数据库状态");
        dbTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        dbPanel.add(dbTitle, BorderLayout.NORTH);

        JPanel dbInfoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        dbInfoPanel.setBackground(Color.WHITE);
        dbInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel lblStatus = new JLabel("● 连接状态: 测试中...");
        lblStatus.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        JLabel lblDBName = new JLabel("数据库名: bmi_health_db");
        lblDBName.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        dbInfoPanel.add(lblStatus);
        dbInfoPanel.add(lblDBName);
        dbPanel.add(dbInfoPanel, BorderLayout.CENTER);

        JButton btnTestConn = UIUtil.createPrimaryButton("测试连接");
        btnTestConn.setPreferredSize(new Dimension(100, 36));
        btnTestConn.addActionListener(e -> {
            if (DBUtil.testConnection()) {
                lblStatus.setText("● 连接状态: 正常");
                lblStatus.setForeground(UIUtil.SUCCESS_COLOR);
                UIUtil.showInfo(this, "数据库连接正常！");
            } else {
                lblStatus.setText("● 连接状态: 异常");
                lblStatus.setForeground(UIUtil.DANGER_COLOR);
                UIUtil.showError(this, "数据库连接失败！");
            }
        });
        dbPanel.add(btnTestConn, BorderLayout.EAST);

        // 用户登录注册管理说明
        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel loginTitle = new JLabel("用户登录与注册管理");
        loginTitle.setFont(new Font("微软雅黑", Font.BOLD, 16));
        loginPanel.add(loginTitle, BorderLayout.NORTH);

        JPanel loginInfoPanel = new JPanel();
        loginInfoPanel.setLayout(new BoxLayout(loginInfoPanel, BoxLayout.Y_AXIS));
        loginInfoPanel.setBackground(Color.WHITE);
        loginInfoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        String[] loginInfos = {
            "✓ 用户登录: 用户在登录页面输入用户名和密码进行身份验证",
            "✓ 用户注册: 新用户可通过注册页面填写个人信息创建账号",
            "✓ 账号状态: 管理员可在「用户管理」中启用或禁用用户账号",
            "✓ 角色权限: 系统区分管理员(admin)和普通用户(user)两种角色",
            "✓ 默认管理员: 用户名 admin / 密码 admin123",
            "✓ 默认用户: 用户名 user1 / 密码 123456"
        };
        for (String info : loginInfos) {
            JLabel label = new JLabel(info);
            label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            label.setForeground(UIUtil.USER_TEXT_SECONDARY);
            loginInfoPanel.add(label);
            loginInfoPanel.add(Box.createVerticalStrut(8));
        }
        loginPanel.add(loginInfoPanel, BorderLayout.CENTER);

        // 系统统计
        JPanel statPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statPanel.setBackground(UIUtil.USER_BG);

        statPanel.add(createInfoCard("注册用户数", String.valueOf(userService.countUsers()), "👥"));
        statPanel.add(createInfoCard("食谱数量", String.valueOf(recipeService.countRecipes()), "📝"));
        statPanel.add(createInfoCard("食谱分类", String.valueOf(recipeService.countCategories()), "📂"));
        statPanel.add(createInfoCard("材料种类", String.valueOf(recipeService.countIngredients()), "🥗"));
        statPanel.add(createInfoCard("BMI评估次数", String.valueOf(bmiService.countAllRecords()), "📊"));
        statPanel.add(createInfoCard("数据库表数", "10", "🗄️"));

        // 组装
        contentPanel.add(dbPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(loginPanel);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(statPanel);

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createInfoCard(String title, String value, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);

        JLabel t = new JLabel(title);
        t.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        t.setForeground(UIUtil.USER_TEXT_LIGHT);

        JLabel v = new JLabel(value);
        v.setFont(new Font("微软雅黑", Font.BOLD, 24));
        v.setForeground(UIUtil.USER_PRIMARY);

        left.add(t);
        left.add(Box.createVerticalStrut(5));
        left.add(v);

        JLabel i = new JLabel(icon);
        i.setFont(new Font("微软雅黑", Font.PLAIN, 28));

        card.add(left, BorderLayout.CENTER);
        card.add(i, BorderLayout.EAST);
        return card;
    }
}

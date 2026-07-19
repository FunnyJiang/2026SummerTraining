package com.bmi.ui.admin;

import com.bmi.model.User;
import com.bmi.service.BMIService;
import com.bmi.service.RecipeService;
import com.bmi.service.UserService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import java.awt.*;

/**
 * 管理员首页/仪表盘 - 统计数据实时刷新
 */
public class AdminHomePanel extends JPanel {
    private User currentUser;
    private UserService userService = new UserService();
    private RecipeService recipeService = new RecipeService();
    private BMIService bmiService = new BMIService();

    // ★ 保留统计数值标签的引用，以便实时更新
    private JLabel lblUserCount;
    private JLabel lblRecipeCount;
    private JLabel lblIngredientCount;
    private JLabel lblBmiCount;
    private JPanel cardsPanel;
    private JPanel centerPanel;

    public AdminHomePanel(User user) {
        this.currentUser = user;
        initUI();
        refreshData(); // ★ 初始化时加载最新数据
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.USER_BG);

        // 顶部欢迎
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIUtil.USER_PRIMARY);
        topPanel.setPreferredSize(new Dimension(0, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel welcomeLabel = new JLabel("欢迎回来，" +
                (currentUser.getRealName() != null ? currentUser.getRealName() : currentUser.getUsername()) + "！");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);

        JLabel dateLabel = new JLabel("今天是 " +
                new java.text.SimpleDateFormat("yyyy年MM月dd日 EEEE").format(new java.util.Date()));
        dateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        dateLabel.setForeground(UIUtil.USER_PRIMARY_LIGHT);

        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(dateLabel, BorderLayout.EAST);

        // ★ 统计卡片 - 使用可刷新的标签引用
        cardsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardsPanel.setBackground(UIUtil.USER_BG);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));

        // 创建统计卡片，保留数值标签引用
        lblUserCount = new JLabel("0");
        lblUserCount.setFont(new Font("微软雅黑", Font.BOLD, 32));
        lblUserCount.setForeground(UIUtil.PRIMARY_COLOR);

        lblRecipeCount = new JLabel("0");
        lblRecipeCount.setFont(new Font("微软雅黑", Font.BOLD, 32));
        lblRecipeCount.setForeground(UIUtil.SUCCESS_COLOR);

        lblIngredientCount = new JLabel("0");
        lblIngredientCount.setFont(new Font("微软雅黑", Font.BOLD, 32));
        lblIngredientCount.setForeground(UIUtil.ACCENT_COLOR);

        lblBmiCount = new JLabel("0");
        lblBmiCount.setFont(new Font("微软雅黑", Font.BOLD, 32));
        lblBmiCount.setForeground(UIUtil.WARNING_COLOR);

        cardsPanel.add(createStatCard("用户总数", lblUserCount, "👥", UIUtil.PRIMARY_COLOR));
        cardsPanel.add(createStatCard("食谱数量", lblRecipeCount, "📝", UIUtil.SUCCESS_COLOR));
        cardsPanel.add(createStatCard("材料种类", lblIngredientCount, "🥗", UIUtil.ACCENT_COLOR));
        cardsPanel.add(createStatCard("BMI评估次数", lblBmiCount, "📊", UIUtil.WARNING_COLOR));

        // 系统介绍
        JPanel introPanel = new JPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.Y_AXIS));
        introPanel.setBackground(Color.WHITE);
        introPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel introTitle = new JLabel("系统功能概览");
        introTitle.setFont(new Font("微软雅黑", Font.BOLD, 18));
        introTitle.setForeground(UIUtil.USER_TEXT);
        introPanel.add(introTitle);
        introPanel.add(Box.createVerticalStrut(15));

        String[] features = {
            "📊  BMI体质评估与预测 - 根据身高体重计算BMI指数，提供健康评估和膳食建议",
            "👥  用户管理 - 管理系统用户，支持增删改查和状态控制",
            "📝  食谱信息管理 - 管理食谱信息，含热量、蛋白质、脂肪、碳水等营养数据",
            "📂  食谱分类管理 - 管理食谱分类（低脂食谱、高蛋白食谱、均衡膳食等）",
            "📅  食谱健康安排 - 为用户制定周期性食谱安排，包含时间周期和餐次",
            "🥗  材料信息管理 - 管理食材信息，含热量和分类",
            "💬  美食论坛 - 用户交流健康饮食经验的社区平台"
        };

        for (String feature : features) {
            JLabel label = new JLabel(feature);
            label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            label.setForeground(UIUtil.USER_TEXT_SECONDARY);
            introPanel.add(label);
            introPanel.add(Box.createVerticalStrut(10));
        }

        // 底部面板
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(UIUtil.USER_BG);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 25, 25));
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(introPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, JLabel valueLabel, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        titleLabel.setForeground(UIUtil.USER_TEXT_LIGHT);

        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(5));
        leftPanel.add(valueLabel);  // ★ 使用共享的标签引用

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("微软雅黑", Font.PLAIN, 36));

        card.add(leftPanel, BorderLayout.CENTER);
        card.add(iconLabel, BorderLayout.EAST);

        return card;
    }

    /**
     * ★ 从数据库获取最新统计数据并更新显示
     */
    public void refreshData() {
        lblUserCount.setText(String.valueOf(userService.countUsers()));
        lblRecipeCount.setText(String.valueOf(recipeService.countRecipes()));
        lblIngredientCount.setText(String.valueOf(recipeService.countIngredients()));
        lblBmiCount.setText(String.valueOf(bmiService.countAllRecords()));
    }
}

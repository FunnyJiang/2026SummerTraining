package com.bmi.ui.admin;

import com.bmi.model.User;
import com.bmi.ui.LoginFrame;
import com.bmi.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 管理员主界面 - 与用户端统一的现代化侧边栏
 */
public class AdminMainFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private JButton currentBtn;
    private java.util.List<JButton> menuButtons = new java.util.ArrayList<JButton>();

    public AdminMainFrame(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("BMI体质评估与预测系统 - 管理员端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtil.USER_BG);

        // ====== 侧边栏（与用户端统一风格：白色底、绿色强调） ======
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIUtil.USER_SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(230, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, UIUtil.USER_BORDER));

        // Logo区
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        logoPanel.setBackground(UIUtil.USER_SIDEBAR_BG);
        logoPanel.setPreferredSize(new Dimension(230, 70));
        logoPanel.setMaximumSize(new Dimension(230, 70));
        logoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent logoIcon = UIUtil.createUserLogoIcon(28);
        logoPanel.add(logoIcon);
        JLabel logoText = new JLabel("BMI 管理");
        logoText.setFont(new Font("微软雅黑", Font.BOLD, 20));
        logoText.setForeground(UIUtil.USER_TEXT);
        logoPanel.add(logoText);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(20));

        // 导航菜单
        String[] menuItems = {"首页", "个人中心", "用户管理", "食谱分类管理", "食谱信息管理",
                "食谱健康安排", "材料信息管理", "美食论坛", "系统管理"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = createMenuButton(menuItems[i], i == 0);
            final int index = i;
            btn.addActionListener(e -> switchPanel(index, btn));
            menuButtons.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());

        // 底部用户卡片
        JPanel userCard = new JPanel(new BorderLayout(12, 0));
        userCard.setBackground(UIUtil.USER_PRIMARY_LIGHT);
        userCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UIUtil.USER_BORDER),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
        ));
        userCard.setMaximumSize(new Dimension(230, 86));
        userCard.setPreferredSize(new Dimension(230, 86));

        JPanel avatar = UIUtil.createAvatarPanel(
            currentUser.getRealName() != null ? currentUser.getRealName() : currentUser.getUsername(),
            UIUtil.USER_PRIMARY, 42);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setOpaque(false);
        JLabel nameLabel = new JLabel(currentUser.getRealName() != null ? currentUser.getRealName() : currentUser.getUsername());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 14));
        nameLabel.setForeground(UIUtil.USER_TEXT);
        JLabel roleLabel = new JLabel("管理员");
        roleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        roleLabel.setForeground(UIUtil.USER_TEXT_SECONDARY);
        namePanel.add(nameLabel);
        namePanel.add(Box.createVerticalStrut(3));
        namePanel.add(roleLabel);

        userCard.add(avatar, BorderLayout.WEST);
        userCard.add(namePanel, BorderLayout.CENTER);

        // 退出按钮
        JButton btnLogout = new JButton("退出");
        btnLogout.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btnLogout.setForeground(UIUtil.USER_TEXT_SECONDARY);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { btnLogout.setForeground(UIUtil.DANGER_COLOR); }
            @Override
            public void mouseExited(MouseEvent e) { btnLogout.setForeground(UIUtil.USER_TEXT_SECONDARY); }
        });
        btnLogout.addActionListener(e -> {
            if (UIUtil.showConfirm(this, "确定要退出登录吗？")) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        userCard.add(btnLogout, BorderLayout.EAST);
        sidebar.add(userCard);

        // 内容面板
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(UIUtil.USER_BG);

        contentPanel.add(new AdminHomePanel(currentUser), "首页");
        contentPanel.add(new AdminPersonalPanel(currentUser, this), "个人中心");
        contentPanel.add(new UserManagePanel(), "用户管理");
        contentPanel.add(new RecipeCategoryPanel(), "食谱分类管理");
        contentPanel.add(new RecipeManagePanel(), "食谱信息管理");
        contentPanel.add(new RecipeSchedulePanel(), "食谱健康安排");
        contentPanel.add(new IngredientPanel(), "材料信息管理");
        contentPanel.add(new AdminForumPanel(currentUser), "美食论坛");
        contentPanel.add(new SystemManagePanel(currentUser), "系统管理");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        // 默认选中首页
        switchPanel(0, menuButtons.get(0));
    }

    /**
     * 创建侧边栏菜单按钮（与用户端统一风格）
     */
    private JButton createMenuButton(String text, boolean selected) {
        final JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (currentBtn == this) {
                    g2.setColor(UIUtil.USER_SIDEBAR_SELECTED);
                    g2.fillRoundRect(10, 2, getWidth() - 20, getHeight() - 4, 10, 10);
                    g2.setColor(UIUtil.USER_PRIMARY);
                    g2.fillRoundRect(10, 10, 4, getHeight() - 20, 3, 3);
                } else if (getModel().isRollover()) {
                    g2.setColor(UIUtil.USER_SIDEBAR_HOVER);
                    g2.fillRoundRect(10, 2, getWidth() - 20, getHeight() - 4, 10, 10);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("微软雅黑", Font.BOLD, 15));
        btn.setForeground(selected ? UIUtil.USER_PRIMARY : UIUtil.USER_SIDEBAR_TEXT);
        btn.setBackground(UIUtil.USER_SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 0));
        btn.setMaximumSize(new Dimension(230, 46));
        btn.setPreferredSize(new Dimension(230, 46));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        return btn;
    }

    private void switchPanel(int index, JButton btn) {
        String[] names = {"首页", "个人中心", "用户管理", "食谱分类管理", "食谱信息管理",
                "食谱健康安排", "材料信息管理", "美食论坛", "系统管理"};
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, names[index]);

        // 刷新数据
        if (index == 0) {
            for (Component c : contentPanel.getComponents()) {
                if (c instanceof AdminHomePanel) { ((AdminHomePanel) c).refreshData(); break; }
            }
        }
        if (index == 2) {
            for (Component c : contentPanel.getComponents()) {
                if (c instanceof UserManagePanel) { ((UserManagePanel) c).refreshData(); break; }
            }
        }

        // 更新选中状态
        if (currentBtn != null) {
            currentBtn.setForeground(UIUtil.USER_SIDEBAR_TEXT);
        }
        if (btn != null) {
            btn.setForeground(UIUtil.USER_PRIMARY);
            currentBtn = btn;
        }
        for (JButton b : menuButtons) {
            b.repaint();
        }
    }

    public void refreshAllData() {
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof AdminHomePanel) { ((AdminHomePanel) c).refreshData(); break; }
        }
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof UserManagePanel) { ((UserManagePanel) c).refreshData(); break; }
        }
    }

    public void refreshPersonalPanel() {
        contentPanel.remove(1);
        contentPanel.add(new AdminPersonalPanel(currentUser, this), "个人中心", 1);
    }
}

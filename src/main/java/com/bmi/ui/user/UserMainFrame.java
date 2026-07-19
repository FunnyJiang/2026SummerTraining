package com.bmi.ui.user;

import com.bmi.model.User;
import com.bmi.service.UserService;
import com.bmi.ui.LoginFrame;
import com.bmi.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 用户主界面 - 新版简洁侧边栏（参考截图风格）
 */
public class UserMainFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private JButton currentBtn;
    private final java.util.List<JButton> menuButtons = new java.util.ArrayList<>();

    public UserMainFrame(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("BMI体质评估与预测系统 - 用户端");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtil.USER_BG);

        // ====== 侧边栏 ======
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
        JLabel logoText = new JLabel("BMI 健康");
        logoText.setFont(new Font("微软雅黑", Font.BOLD, 20));
        logoText.setForeground(UIUtil.USER_TEXT);
        logoPanel.add(logoText);
        sidebar.add(logoPanel);
        sidebar.add(Box.createVerticalStrut(20));

        // 导航菜单
        String[] menuItems = {"首页", "BMI评估", "膳食摄入分析", "我的收藏", "美食论坛", "个人中心"};
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
        JLabel roleLabel = new JLabel("普通用户");
        roleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        roleLabel.setForeground(UIUtil.USER_TEXT_SECONDARY);
        namePanel.add(nameLabel);
        namePanel.add(Box.createVerticalStrut(3));
        namePanel.add(roleLabel);

        userCard.add(avatar, BorderLayout.WEST);
        userCard.add(namePanel, BorderLayout.CENTER);
        sidebar.add(userCard);

        // ====== 内容面板 ======
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(UIUtil.USER_BG);

        UserHomePanel dashboard = new UserHomePanel(currentUser);
        contentPanel.add(dashboard, "dashboard");
        contentPanel.add(new DietIntakePanel(currentUser), "膳食摄入分析");
        contentPanel.add(new FavoritePanel(currentUser), "我的收藏");
        contentPanel.add(new UserForumPanel(currentUser), "美食论坛");
        contentPanel.add(new UserPersonalPanel(currentUser, this), "个人中心");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
        switchPanel(0, menuButtons.get(0));
    }

    /**
     * 创建侧边栏菜单按钮
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
        String[] cardNames = {"dashboard", "dashboard", "膳食摄入分析", "我的收藏", "美食论坛", "个人中心"};
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, cardNames[index]);

        if (index == 0 || index == 1) {
            reloadCurrentUserFromDB();
            for (Component c : contentPanel.getComponents()) {
                if (c instanceof UserHomePanel) { ((UserHomePanel) c).refreshData(); break; }
            }
        }
        if (index == 5) {
            reloadCurrentUserFromDB();
            for (Component c : contentPanel.getComponents()) {
                if (c instanceof UserPersonalPanel) { ((UserPersonalPanel) c).reloadFromDB(); break; }
            }
        }

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

    private void reloadCurrentUserFromDB() {
        User freshUser = new UserService().findById(currentUser.getId());
        if (freshUser != null) currentUser = freshUser;
    }

    public void refreshHomePanel() {
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof UserHomePanel) { ((UserHomePanel) c).refreshData(); break; }
        }
    }

    public void refreshPersonalPanel() {
        for (Component c : contentPanel.getComponents()) {
            if (c instanceof UserPersonalPanel) { ((UserPersonalPanel) c).refreshBMI(); break; }
        }
    }
}

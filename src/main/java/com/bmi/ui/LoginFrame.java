package com.bmi.ui;

import com.bmi.model.User;
import com.bmi.service.UserService;
import com.bmi.ui.admin.AdminMainFrame;
import com.bmi.ui.user.UserMainFrame;
import com.bmi.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * 登录窗口 - 现代化设计
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;
    private UserService userService = new UserService();

    public LoginFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("BMI体质评估与预测系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(480, 640);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIUtil.BG_COLOR);

        // 主容器
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(UIUtil.BG_COLOR);

        // ====== 顶部渐变区 ======
        JPanel headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIUtil.PRIMARY_COLOR,
                    0, getHeight(), UIUtil.PRIMARY_LIGHT
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 30, 0, 0);
                g2.dispose();
            }
        };
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(480, 220));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 30, 40));

        // Logo图标 - 使用自定义可绘制图形，避免emoji字体缺失显示方框
        JLabel logoIcon = new JLabel(new HealthIcon(56, Color.WHITE));
        logoIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel titleLabel = new JLabel("BMI体质评估与预测系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 4, 0));

        JLabel subtitleLabel = new JLabel("含膳食推荐 · 健康管理 · 营养指导", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(200, 240, 235));

        JPanel headerContent = new JPanel(new BorderLayout());
        headerContent.setOpaque(false);
        headerContent.add(logoIcon, BorderLayout.NORTH);
        headerContent.add(titleLabel, BorderLayout.CENTER);
        headerContent.add(subtitleLabel, BorderLayout.SOUTH);
        headerPanel.add(headerContent, BorderLayout.CENTER);

        // ====== 表单区（整体居中） ======
        JPanel formOuter = new JPanel(new GridBagLayout());
        formOuter.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(35, 50, 30, 50));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 用户名标签
        JLabel lblUsername = new JLabel("用户名", SwingConstants.CENTER);
        lblUsername.setFont(new Font("微软雅黑", Font.BOLD, 13));
        lblUsername.setForeground(UIUtil.TEXT_SECONDARY);
        lblUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblUsername.setMaximumSize(new Dimension(300, 20));

        txtUsername = UIUtil.createTextField(20);
        txtUsername.setMaximumSize(new Dimension(300, 42));
        txtUsername.setPreferredSize(new Dimension(300, 42));
        txtUsername.setText("admin");
        txtUsername.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 密码标签
        JLabel lblPassword = new JLabel("密码", SwingConstants.CENTER);
        lblPassword.setFont(new Font("微软雅黑", Font.BOLD, 13));
        lblPassword.setForeground(UIUtil.TEXT_SECONDARY);
        lblPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPassword.setMaximumSize(new Dimension(300, 20));
        lblPassword.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        txtPassword = UIUtil.createPasswordField(20);
        txtPassword.setMaximumSize(new Dimension(300, 42));
        txtPassword.setPreferredSize(new Dimension(300, 42));
        txtPassword.setText("admin123");
        txtPassword.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 按钮行
        JPanel btnRow = new JPanel(new BorderLayout());
        btnRow.setOpaque(false);
        btnRow.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        btnLogin = UIUtil.createPrimaryButton("登 录");
        btnLogin.setPreferredSize(new Dimension(180, 42));

        btnRegister = UIUtil.createOutlineButton("注 册", UIUtil.PRIMARY_COLOR);
        btnRegister.setPreferredSize(new Dimension(180, 42));

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnLogin);
        btnPanel.add(btnRegister);
        btnRow.add(btnPanel, BorderLayout.CENTER);

        // 提示信息
        JLabel hintLabel = new JLabel("<html><body style='font-size:12px;color:#9e9e9e; text-align:center;'>" +
                "管理员: admin / admin123 &nbsp;&nbsp;|&nbsp;&nbsp; 用户: user1 / 123456</body></html>");
        hintLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        hintLabel.setForeground(UIUtil.TEXT_LIGHT);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintLabel.setMaximumSize(new Dimension(300, 40));
        hintLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // 测试连接按钮
        JButton btnTest = new JButton("测试数据库连接");
        btnTest.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btnTest.setForeground(UIUtil.PRIMARY_COLOR);
        btnTest.setBorderPainted(false);
        btnTest.setContentAreaFilled(false);
        btnTest.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTest.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTest.setMaximumSize(new Dimension(300, 30));
        btnTest.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        btnTest.addActionListener(e -> {
            if (com.bmi.util.DBUtil.testConnection()) {
                UIUtil.showInfo(this, "数据库连接成功！");
            } else {
                UIUtil.showError(this, "数据库连接失败，请检查MySQL是否启动！");
            }
        });

        formPanel.add(lblUsername);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(txtUsername);
        formPanel.add(lblPassword);
        formPanel.add(Box.createVerticalStrut(6));
        formPanel.add(txtPassword);
        formPanel.add(btnRow);
        formPanel.add(hintLabel);
        formPanel.add(btnTest);

        formOuter.add(formPanel);

        // ====== 底部 ======
        JLabel footerLabel = new JLabel("© 2026 BMI健康管理系统  All Rights Reserved", SwingConstants.CENTER);
        footerLabel.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        footerLabel.setForeground(UIUtil.TEXT_LIGHT);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        container.add(headerPanel, BorderLayout.NORTH);
        container.add(formOuter, BorderLayout.CENTER);
        container.add(footerLabel, BorderLayout.SOUTH);

        add(container);

        // 事件
        btnLogin.addActionListener(e -> doLogin());
        btnRegister.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) doLogin();
            }
        });
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            UIUtil.showError(this, "请输入用户名和密码！");
            return;
        }

        User user = userService.login(username, password);
        if (user == null) {
            UIUtil.showError(this, "用户名或密码错误，或账号已被禁用！");
            return;
        }

        if ("admin".equals(user.getRole())) {
            new AdminMainFrame(user).setVisible(true);
        } else {
            new UserMainFrame(user).setVisible(true);
        }
        dispose();
    }

    public static void main(String[] args) {
        UIUtil.setGlobalFont();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    /**
     * 自定义健康图标 - 使用Java2D绘制，避免依赖字体emoji
     * 图形为：圆角正方形背景中一个简洁的“人形+天平/秤”组合符号
     */
    static class HealthIcon implements Icon {
        private int size;
        private Color color;

        public HealthIcon(int size, Color color) {
            this.size = size;
            this.color = color;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int pad = 4;
            int s = size - pad * 2;

            // 外圆角方框
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawRoundRect(x + pad, y + pad, s, s, 14, 14);

            // 内部天平横梁
            int cx = x + size / 2;
            int cy = y + size / 2 - 2;
            int beamHalf = s / 4;
            g2.drawLine(cx - beamHalf, cy - 4, cx + beamHalf, cy - 4);

            // 中心竖线
            g2.drawLine(cx, cy - 4, cx, cy + 6);

            // 左右两个托盘（弧线+竖线）
            int trayW = 8;
            int trayH = 5;
            g2.drawArc(cx - beamHalf - trayW / 2, cy - 4, trayW, trayH, 0, 180);
            g2.drawArc(cx + beamHalf - trayW / 2, cy - 4, trayW, trayH, 0, 180);

            // 底座
            g2.drawLine(cx - 6, cy + 10, cx + 6, cy + 10);
            g2.drawLine(cx, cy + 6, cx, cy + 10);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return size;
        }

        @Override
        public int getIconHeight() {
            return size;
        }
    }
}

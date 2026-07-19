package com.bmi.ui.user;

import com.bmi.model.BMIRecord;
import com.bmi.model.Recipe;
import com.bmi.model.User;
import com.bmi.service.BMIService;
import com.bmi.service.RecipeService;
import com.bmi.service.UserService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户首页 - 新版仪表盘（参考截图风格）
 * 集成BMI计算器、BMI结果、膳食建议、历史记录与推荐食谱
 */
public class UserHomePanel extends JPanel {
    private User currentUser;
    private BMIService bmiService = new BMIService();
    private RecipeService recipeService = new RecipeService();
    private UserService userService = new UserService();

    private JTextField txtHeight, txtWeight, txtAge;
    private JComboBox<String> cmbGender;
    private JLabel lblBMIValue, lblBMIStatus;
    private JTextArea txtDietAdvice;
    private JPanel historyListPanel;
    private JPanel recipeCardsPanel;
    private JPanel bmiResultPanel;
    private JPanel scorePanel;
    private JPanel scalePanel;
    private JPanel bottomResultPanel;

    public UserHomePanel(User user) {
        this.currentUser = user;
        initUI();
        refreshData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.USER_BG);

        // 顶部标题栏
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 仪表盘主体
        JPanel dashboard = new JPanel(new GridBagLayout());
        dashboard.setBackground(UIUtil.USER_BG);
        dashboard.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 18, 18);
        gbc.fill = GridBagConstraints.BOTH;

        // 左上：BMI计算器
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.36; gbc.weighty = 0.55;
        dashboard.add(createBMICalculatorCard(), gbc);

        // 右上：BMI结果
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.64; gbc.weighty = 0.55;
        gbc.insets = new Insets(0, 0, 18, 0);
        dashboard.add(createBMIResultCard(), gbc);

        // 左下：历史记录
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.36; gbc.weighty = 0.45;
        gbc.insets = new Insets(0, 0, 0, 18);
        dashboard.add(createHistoryCard(), gbc);

        // 右下：为您推荐
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.64; gbc.weighty = 0.45;
        gbc.insets = new Insets(0, 0, 0, 0);
        dashboard.add(createRecommendationsCard(), gbc);

        JScrollPane scroll = new JScrollPane(dashboard);
        scroll.setBorder(null);
        scroll.setBackground(UIUtil.USER_BG);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    // ====== 顶部标题栏 ======
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtil.USER_BORDER),
            BorderFactory.createEmptyBorder(18, 28, 18, 28)
        ));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        JLabel title = new JLabel("BMI 评估与膳食推荐");
        title.setFont(new Font("微软雅黑", Font.BOLD, 22));
        title.setForeground(UIUtil.USER_TEXT);
        JLabel subtitle = new JLabel(new SimpleDateFormat("yyyy年M月d日").format(new Date()) + " · 根据体测数据智能推荐膳食");
        subtitle.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        subtitle.setForeground(UIUtil.USER_TEXT_SECONDARY);
        left.add(title);
        left.add(Box.createVerticalStrut(4));
        left.add(subtitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        JButton btnToday = UIUtil.createUserOutlineButton("今日", UIUtil.USER_TEXT_SECONDARY);
        btnToday.setPreferredSize(new Dimension(80, 36));
        btnToday.addActionListener(e -> refreshData());
        JButton btnSave = UIUtil.createUserPrimaryButton("保存记录");
        btnSave.setPreferredSize(new Dimension(110, 36));
        btnSave.addActionListener(e -> calculateAndSave());
        right.add(btnToday);
        right.add(btnSave);

        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.EAST);
        return panel;
    }

    // ====== BMI计算器卡片 ======
    private JPanel createBMICalculatorCard() {
        JPanel card = UIUtil.createUserCardPanel(16);
        card.setLayout(new BorderLayout(0, 18));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel title = new JLabel("BMI 计算器");
        title.setFont(new Font("微软雅黑", Font.BOLD, 16));
        title.setForeground(UIUtil.USER_TEXT);
        JLabel hint = new JLabel("输入体测数据");
        hint.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        hint.setForeground(UIUtil.USER_TEXT_LIGHT);
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(hint, BorderLayout.EAST);
        card.add(titleRow, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);
        gbc.weightx = 1;

        // 身高
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblHeight = new JLabel("身高 (cm)");
        lblHeight.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblHeight.setForeground(UIUtil.USER_TEXT_SECONDARY);
        form.add(lblHeight, gbc);
        gbc.gridy = 1;
        txtHeight = UIUtil.createUserTextField(10);
        txtHeight.setText(currentUser.getHeight() > 0 ? String.valueOf((int) currentUser.getHeight()) : "");
        form.add(txtHeight, gbc);

        // 体重
        gbc.gridy = 2;
        JLabel lblWeight = new JLabel("体重 (kg)");
        lblWeight.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblWeight.setForeground(UIUtil.USER_TEXT_SECONDARY);
        form.add(lblWeight, gbc);
        gbc.gridy = 3;
        txtWeight = UIUtil.createUserTextField(10);
        txtWeight.setText(currentUser.getWeight() > 0 ? String.valueOf((int) currentUser.getWeight()) : "");
        form.add(txtWeight, gbc);

        // 性别 + 年龄
        gbc.gridy = 4;
        JPanel genderAgeRow = new JPanel(new GridLayout(1, 2, 12, 0));
        genderAgeRow.setOpaque(false);

        JPanel genderPanel = new JPanel(new GridBagLayout());
        genderPanel.setOpaque(false);
        GridBagConstraints g2 = new GridBagConstraints();
        g2.fill = GridBagConstraints.HORIZONTAL;
        g2.weightx = 1; g2.gridx = 0; g2.gridy = 0;
        JLabel lblGender = new JLabel("性别");
        lblGender.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblGender.setForeground(UIUtil.USER_TEXT_SECONDARY);
        genderPanel.add(lblGender, g2);
        g2.gridy = 1;
        cmbGender = new JComboBox<>(new String[]{"男", "女"});
        cmbGender.setSelectedItem("男".equals(currentUser.getGender()) ? "男" : "女");
        cmbGender.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cmbGender.setBackground(Color.WHITE);
        cmbGender.setBorder(BorderFactory.createLineBorder(UIUtil.USER_BORDER));
        cmbGender.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setFont(new Font("微软雅黑", Font.PLAIN, 14));
                return this;
            }
        });
        genderPanel.add(cmbGender, g2);
        genderAgeRow.add(genderPanel);

        JPanel agePanel = new JPanel(new GridBagLayout());
        agePanel.setOpaque(false);
        GridBagConstraints g3 = new GridBagConstraints();
        g3.fill = GridBagConstraints.HORIZONTAL;
        g3.weightx = 1; g3.gridx = 0; g3.gridy = 0;
        JLabel lblAge = new JLabel("年龄");
        lblAge.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        lblAge.setForeground(UIUtil.USER_TEXT_SECONDARY);
        agePanel.add(lblAge, g3);
        g3.gridy = 1;
        txtAge = UIUtil.createUserTextField(10);
        txtAge.setText(String.valueOf(currentUser.getAge()));
        agePanel.add(txtAge, g3);
        genderAgeRow.add(agePanel);

        form.add(genderAgeRow, gbc);

        // 按钮
        gbc.gridy = 5; gbc.insets = new Insets(8, 0, 0, 0);
        JButton btnCalc = UIUtil.createUserPrimaryButton("开始评估");
        btnCalc.addActionListener(e -> calculateAndSave());
        form.add(btnCalc, gbc);

        card.add(form, BorderLayout.CENTER);
        return card;
    }

    // ====== BMI结果卡片 ======
    private JPanel createBMIResultCard() {
        JPanel card = UIUtil.createUserCardPanel(16);
        card.setLayout(new BorderLayout(0, 18));

        JLabel title = new JLabel("您的 BMI");
        title.setFont(new Font("微软雅黑", Font.BOLD, 16));
        title.setForeground(UIUtil.USER_TEXT);
        card.add(title, BorderLayout.NORTH);

        // 上半部：数值 + 健康评分
        bmiResultPanel = new JPanel(new BorderLayout(20, 0));
        bmiResultPanel.setOpaque(false);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        lblBMIValue = new JLabel("--");
        lblBMIValue.setFont(new Font("微软雅黑", Font.BOLD, 52));
        lblBMIValue.setForeground(UIUtil.USER_PRIMARY);
        lblBMIValue.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblBMIStatus = new JLabel("请输入身高体重");
        lblBMIStatus.setFont(new Font("微软雅黑", Font.BOLD, 14));
        lblBMIStatus.setForeground(UIUtil.USER_TEXT_SECONDARY);
        lblBMIStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(lblBMIValue);
        left.add(Box.createVerticalStrut(8));
        left.add(lblBMIStatus);

        bmiResultPanel.add(left, BorderLayout.WEST);
        scorePanel = UIUtil.createHealthScorePanel(0, 0);
        scorePanel.setPreferredSize(new Dimension(100, 100));
        bmiResultPanel.add(scorePanel, BorderLayout.EAST);

        card.add(bmiResultPanel, BorderLayout.CENTER);

        // 下半部：BMI色条 + 膳食建议
        bottomResultPanel = new JPanel();
        bottomResultPanel.setLayout(new BoxLayout(bottomResultPanel, BoxLayout.Y_AXIS));
        bottomResultPanel.setOpaque(false);

        scalePanel = UIUtil.createBMIScaleBlocks(0);
        scalePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        scalePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomResultPanel.add(scalePanel);
        bottomResultPanel.add(Box.createVerticalStrut(18));

        JLabel adviceTitle = new JLabel("膳食建议");
        adviceTitle.setFont(new Font("微软雅黑", Font.BOLD, 14));
        adviceTitle.setForeground(UIUtil.USER_TEXT);
        adviceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomResultPanel.add(adviceTitle);
        bottomResultPanel.add(Box.createVerticalStrut(8));

        txtDietAdvice = new JTextArea("完成BMI评估后将显示个性化膳食建议。");
        txtDietAdvice.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        txtDietAdvice.setForeground(UIUtil.USER_TEXT_SECONDARY);
        txtDietAdvice.setLineWrap(true);
        txtDietAdvice.setWrapStyleWord(true);
        txtDietAdvice.setEditable(false);
        txtDietAdvice.setOpaque(false);
        txtDietAdvice.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        txtDietAdvice.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtDietAdvice.setRows(8);
        txtDietAdvice.setColumns(40);
        txtDietAdvice.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        bottomResultPanel.add(txtDietAdvice);

        card.add(bottomResultPanel, BorderLayout.SOUTH);
        return card;
    }

    // ====== 历史记录卡片 ======
    private JPanel createHistoryCard() {
        JPanel card = UIUtil.createUserCardPanel(16);
        card.setLayout(new BorderLayout(0, 12));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JLabel title = new JLabel("历史记录");
        title.setFont(new Font("微软雅黑", Font.BOLD, 16));
        title.setForeground(UIUtil.USER_TEXT);
        JLabel viewAll = new JLabel("查看全部");
        viewAll.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        viewAll.setForeground(UIUtil.USER_PRIMARY);
        viewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showAllHistoryDialog();
            }
        });
        titleRow.add(title, BorderLayout.WEST);
        titleRow.add(viewAll, BorderLayout.EAST);
        card.add(titleRow, BorderLayout.NORTH);

        historyListPanel = new JPanel();
        historyListPanel.setLayout(new BoxLayout(historyListPanel, BoxLayout.Y_AXIS));
        historyListPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(historyListPanel);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        card.add(scroll, BorderLayout.CENTER);

        return card;
    }

    // ====== 推荐食谱卡片 ======
    private JPanel createRecommendationsCard() {
        JPanel card = UIUtil.createUserCardPanel(16);
        card.setLayout(new BorderLayout(0, 12));

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setOpaque(false);
        JPanel titleLeft = new JPanel();
        titleLeft.setLayout(new BoxLayout(titleLeft, BoxLayout.Y_AXIS));
        titleLeft.setOpaque(false);
        JLabel title = new JLabel("为您推荐");
        title.setFont(new Font("微软雅黑", Font.BOLD, 16));
        title.setForeground(UIUtil.USER_TEXT);
        JLabel subtitle = new JLabel("基于您的BMI提供均衡膳食方案");
        subtitle.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        subtitle.setForeground(UIUtil.USER_TEXT_SECONDARY);
        titleLeft.add(title);
        titleLeft.add(Box.createVerticalStrut(2));
        titleLeft.add(subtitle);
        titleRow.add(titleLeft, BorderLayout.WEST);

        JLabel filter = new JLabel("均衡 · 低脂");
        filter.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        filter.setForeground(UIUtil.USER_PRIMARY);
        filter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        titleRow.add(filter, BorderLayout.EAST);
        card.add(titleRow, BorderLayout.NORTH);

        recipeCardsPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        recipeCardsPanel.setOpaque(false);
        card.add(recipeCardsPanel, BorderLayout.CENTER);

        return card;
    }

    // ====== 小食谱卡片（截图风格） ======
    private JPanel createRecipeCardSmall(Recipe r) {
        Color foodColor = UIUtil.getFoodColor(r.getName());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);

        // 上半：彩色图标区
        JPanel top = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(foodColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        top.setOpaque(false);
        top.setPreferredSize(new Dimension(0, 120));
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JComponent icon = new JComponent() {
            {
                setPreferredSize(new Dimension(48, 48));
                setOpaque(false);
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(w / 4, h / 3, w / 2, h / 2, 0, 180);
                g2.drawLine(w / 4, h / 3 + h / 4, w * 3 / 4, h / 3 + h / 4);
                g2.drawArc(w / 2 - 8, h / 5, 6, 10, 45, 100);
                g2.drawArc(w / 2 + 2, h / 5, 6, 10, 45, 100);
                g2.dispose();
            }
        };
        top.add(icon);

        // 下半：信息区
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        bottom.setOpaque(true);

        JLabel name = new JLabel(r.getName());
        name.setFont(new Font("微软雅黑", Font.BOLD, 14));
        name.setForeground(UIUtil.USER_TEXT);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel cal = new JLabel((int) r.getCalories() + " 千卡");
        cal.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        cal.setForeground(UIUtil.USER_TEXT_SECONDARY);
        cal.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel macros = new JLabel(String.format("蛋白质 %.0fg · 脂肪 %.0fg · 碳水 %.0fg", r.getProtein(), r.getFat(), r.getCarbs()));
        macros.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        macros.setForeground(UIUtil.USER_TEXT_LIGHT);
        macros.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottom.add(name);
        bottom.add(Box.createVerticalStrut(4));
        bottom.add(cal);
        bottom.add(Box.createVerticalStrut(2));
        bottom.add(macros);

        card.add(top);
        card.add(bottom);
        return card;
    }

    // ====== 历史记录行 ======
    private JPanel createHistoryRow(BMIRecord r) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtil.USER_BORDER),
            BorderFactory.createEmptyBorder(12, 0, 12, 0)
        ));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        JLabel date = new JLabel(new SimpleDateFormat("MM月dd日").format(r.getCreateTime()));
        date.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        date.setForeground(UIUtil.USER_TEXT);
        JLabel bmi = new JLabel("BMI " + String.format("%.1f", r.getBmi()));
        bmi.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        bmi.setForeground(UIUtil.USER_TEXT_SECONDARY);
        left.add(date);
        left.add(Box.createVerticalStrut(2));
        left.add(bmi);

        Color statusBg = UIUtil.getBMIColor(r.getBmi());
        JLabel badge = UIUtil.createUserStatusBadge(r.getAssessment(), statusBg, Color.WHITE);
        badge.setPreferredSize(new Dimension(56, 24));

        row.add(left, BorderLayout.WEST);
        row.add(badge, BorderLayout.EAST);
        return row;
    }

    // ====== 计算并保存BMI ======
    private void calculateAndSave() {
        String heightStr = txtHeight.getText().trim();
        String weightStr = txtWeight.getText().trim();
        String ageStr = txtAge.getText().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            UIUtil.showError(this, "请输入身高和体重！");
            return;
        }
        double height, weight;
        try {
            height = Double.parseDouble(heightStr);
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            UIUtil.showError(this, "请输入有效的数字！");
            return;
        }
        if (height <= 0 || weight <= 0) {
            UIUtil.showError(this, "身高和体重必须大于0！");
            return;
        }

        double bmi = UIUtil.calculateBMI(height, weight);
        String assessment = UIUtil.getBMIAssessment(bmi);
        String recommendation = UIUtil.getBMIDietRecommendation(bmi);

        BMIRecord record = new BMIRecord(currentUser.getId(), height, weight, bmi, assessment, recommendation);
        bmiService.saveRecord(record);

        currentUser.setHeight(height);
        currentUser.setWeight(weight);
        currentUser.setGender((String) cmbGender.getSelectedItem());
        try {
            currentUser.setAge(ageStr.isEmpty() ? 0 : Integer.parseInt(ageStr));
        } catch (NumberFormatException ignored) {}
        userService.updateProfile(currentUser);

        refreshData();
        UIUtil.showInfo(this, "BMI评估完成，记录已保存！");
    }

    // ====== 刷新数据 ======
    public void refreshData() {
        updateBMIDisplay();
        loadHistory();
        loadRecommendations();
    }

    private void updateBMIDisplay() {
        double bmi = currentUser.getBMI();
        if (bmi > 0) {
            lblBMIValue.setText(String.format("%.1f", bmi));
            lblBMIValue.setForeground(UIUtil.getBMIColor(bmi));
            String assessment = UIUtil.getBMIAssessment(bmi);
            lblBMIStatus.setText("● " + assessment);
            lblBMIStatus.setForeground(UIUtil.getBMIColor(bmi));
            txtDietAdvice.setText(UIUtil.getBMIDietRecommendation(bmi));
        } else {
            lblBMIValue.setText("--");
            lblBMIValue.setForeground(UIUtil.USER_PRIMARY);
            lblBMIStatus.setText("请输入身高体重");
            lblBMIStatus.setForeground(UIUtil.USER_TEXT_SECONDARY);
            txtDietAdvice.setText("完成BMI评估后将显示个性化膳食建议。");
        }

        // 刷新BMI状态条（高亮当前区间）
        if (bottomResultPanel != null && scalePanel != null) {
            int index = -1;
            for (int i = 0; i < bottomResultPanel.getComponentCount(); i++) {
                if (bottomResultPanel.getComponent(i) == scalePanel) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                bottomResultPanel.remove(scalePanel);
                scalePanel = UIUtil.createBMIScaleBlocks(bmi);
                scalePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
                scalePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                bottomResultPanel.add(scalePanel, index);
                bottomResultPanel.revalidate();
                bottomResultPanel.repaint();
            }
        }

        // 刷新健康评分圆环
        if (scorePanel != null) {
            bmiResultPanel.remove(scorePanel);
        }
        int score = computeHealthScore(bmi);
        scorePanel = UIUtil.createHealthScorePanel(score, bmi);
        scorePanel.setPreferredSize(new Dimension(100, 100));
        bmiResultPanel.add(scorePanel, BorderLayout.EAST);
        bmiResultPanel.revalidate();
        bmiResultPanel.repaint();
    }

    private int computeHealthScore(double bmi) {
        if (bmi <= 0) return 0;
        if (bmi < 18.5) return 65;
        if (bmi < 24.0) return 90;
        if (bmi < 28.0) return 75;
        return 55;
    }

    private void loadHistory() {
        historyListPanel.removeAll();
        List<BMIRecord> records = bmiService.findRecordsByUserId(currentUser.getId());
        if (records == null || records.isEmpty()) {
            JLabel empty = new JLabel("暂无历史记录", SwingConstants.CENTER);
            empty.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            empty.setForeground(UIUtil.USER_TEXT_LIGHT);
            historyListPanel.add(empty);
        } else {
            int count = 0;
            for (BMIRecord r : records) {
                if (count++ >= 5) break;
                historyListPanel.add(createHistoryRow(r));
            }
        }
        historyListPanel.revalidate();
        historyListPanel.repaint();
    }

    private void showAllHistoryDialog() {
        Window win = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(win instanceof Frame ? (Frame) win : null, "全部历史记录", true);
        dialog.setSize(700, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(0, 0));

        String[] columns = {"ID", "身高(cm)", "体重(kg)", "BMI值", "评估结果", "评估时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(model);
        table.setRowHeight(32);
        table.getTableHeader().setBackground(new Color(240, 248, 255));
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        table.setSelectionBackground(new Color(200, 220, 240));

        List<BMIRecord> records = bmiService.findRecordsByUserId(currentUser.getId());
        if (records == null || records.isEmpty()) {
            model.addRow(new Object[]{"", "", "", "", "暂无历史记录", ""});
        } else {
            for (BMIRecord r : records) {
                model.addRow(new Object[]{
                    r.getId(), r.getHeight(), r.getWeight(), String.format("%.2f", r.getBmi()),
                    r.getAssessment(), r.getCreateTimeStr()
                });
            }
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        dialog.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JButton btnClose = UIUtil.createUserPrimaryButton("关闭");
        btnClose.setPreferredSize(new Dimension(80, 32));
        btnClose.addActionListener(e -> dialog.dispose());
        bottom.add(btnClose);
        dialog.add(bottom, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void loadRecommendations() {
        recipeCardsPanel.removeAll();
        double bmi = currentUser.getBMI();
        List<Recipe> recipes;
        if (bmi > 0) {
            recipes = bmiService.getRecommendedRecipes(bmi);
        } else {
            recipes = recipeService.searchRecipes(null, 0);
        }
        if (recipes == null || recipes.isEmpty()) {
            JLabel empty = new JLabel("暂无推荐食谱", SwingConstants.CENTER);
            empty.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            empty.setForeground(UIUtil.USER_TEXT_LIGHT);
            recipeCardsPanel.add(empty);
        } else {
            int count = 0;
            for (Recipe r : recipes) {
                if (count++ >= 3) break;
                recipeCardsPanel.add(createRecipeCardSmall(r));
            }
        }
        recipeCardsPanel.revalidate();
        recipeCardsPanel.repaint();
    }
}

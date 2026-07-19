package com.bmi.ui.user;

import com.bmi.model.BMIRecord;
import com.bmi.model.Recipe;
import com.bmi.model.User;
import com.bmi.service.BMIService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * BMI计算与评估面板 - 核心功能
 */
public class BMICalculatorPanel extends JPanel {
    private User currentUser;
    private BMIService bmiService = new BMIService();
    private JTextField txtHeight, txtWeight;
    private JLabel lblBMIResult, lblAssessment, lblRecommendation;
    private JTextArea txtRecommendation;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel resultPanel;
    private JFrame parentFrame;  // ★ 保留父窗口引用，用于通知刷新

    public BMICalculatorPanel(User user, JFrame frame) {
        this.currentUser = user;
        this.parentFrame = frame;  // ★ 保存父窗口引用
        initUI();
        loadHistory();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.BG_COLOR);

        // 标题
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        JLabel titleLabel = new JLabel("BMI体质评估与预测");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 上部：计算区
        JPanel calcPanel = new JPanel(new BorderLayout());
        calcPanel.setBackground(UIUtil.BG_COLOR);
        calcPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        // 输入区
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        inputPanel.add(UIUtil.createLabel("身高 (cm):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtHeight = UIUtil.createTextField(10);
        txtHeight.setText(currentUser.getHeight() > 0 ? String.valueOf(currentUser.getHeight()) : "");
        inputPanel.add(txtHeight, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(UIUtil.createLabel("体重 (kg):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtWeight = UIUtil.createTextField(10);
        txtWeight.setText(currentUser.getWeight() > 0 ? String.valueOf(currentUser.getWeight()) : "");
        inputPanel.add(txtWeight, gbc);

        gbc.gridx = 4; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JButton btnCalc = UIUtil.createPrimaryButton("开始评估");
        btnCalc.addActionListener(e -> calculateBMI());
        inputPanel.add(btnCalc, gbc);

        // 结果区
        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(Color.WHITE);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        resultPanel.setVisible(false);

        lblBMIResult = new JLabel();
        lblBMIResult.setFont(new Font("微软雅黑", Font.BOLD, 36));
        lblBMIResult.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblAssessment = new JLabel();
        lblAssessment.setFont(new Font("微软雅黑", Font.BOLD, 18));
        lblAssessment.setAlignmentX(Component.LEFT_ALIGNMENT);

        // BMI参考表
        JPanel refPanel = new JPanel(new GridLayout(4, 1, 2, 2));
        refPanel.setBackground(Color.WHITE);
        refPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        refPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        String[][] bmiRanges = {
            {"BMI < 18.5", "偏瘦", "体重不足"},
            {"18.5 ≤ BMI < 24", "正常", "体重正常"},
            {"24 ≤ BMI < 28", "偏胖", "超重"},
            {"BMI ≥ 28", "肥胖", "需要减重"}
        };
        Color[] bmiColors = {UIUtil.BMI_UNDERWEIGHT, UIUtil.BMI_NORMAL, UIUtil.BMI_OVERWEIGHT, UIUtil.BMI_OBESE};
        for (int i = 0; i < 4; i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(bmiColors[i]);
            row.setPreferredSize(new Dimension(500, 30));
            JLabel label = new JLabel("  " + bmiRanges[i][0] + "  →  " + bmiRanges[i][1] + " (" + bmiRanges[i][2] + ")");
            label.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            label.setForeground(Color.WHITE);
            row.add(label, BorderLayout.CENTER);
            refPanel.add(row);
        }

        JLabel recTitle = new JLabel("膳食建议:");
        recTitle.setFont(new Font("微软雅黑", Font.BOLD, 15));
        recTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtRecommendation = UIUtil.createTextArea();
        txtRecommendation.setEditable(false);
        txtRecommendation.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        txtRecommendation.setBackground(new Color(248, 250, 252));
        txtRecommendation.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // 让文本框按内容自动撑高 (5 条建议都能完整显示, 无需滚动)
        txtRecommendation.setRows(12);
        txtRecommendation.setColumns(50);
        txtRecommendation.setLineWrap(true);
        txtRecommendation.setWrapStyleWord(true);
        // 不用 JScrollPane, 直接显示完整内容
        JPanel recWrap = new JPanel(new BorderLayout());
        recWrap.setBackground(new Color(248, 250, 252));
        recWrap.setBorder(BorderFactory.createLineBorder(new Color(220, 224, 230), 1));
        recWrap.add(txtRecommendation, BorderLayout.CENTER);
        recWrap.setAlignmentX(Component.LEFT_ALIGNMENT);

        resultPanel.add(lblBMIResult);
        resultPanel.add(Box.createVerticalStrut(5));
        resultPanel.add(lblAssessment);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(refPanel);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(recTitle);
        resultPanel.add(Box.createVerticalStrut(5));
        resultPanel.add(recWrap);

        calcPanel.add(inputPanel, BorderLayout.NORTH);
        calcPanel.add(resultPanel, BorderLayout.CENTER);

        // 下部：历史记录
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(UIUtil.BG_COLOR);
        historyPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JPanel histTitlePanel = new JPanel(new BorderLayout());
        histTitlePanel.setBackground(Color.WHITE);
        histTitlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));
        JLabel histTitle = new JLabel("BMI评估历史记录");
        histTitle.setFont(new Font("微软雅黑", Font.BOLD, 15));
        histTitlePanel.add(histTitle, BorderLayout.WEST);

        JButton btnDelete = UIUtil.createDangerButton("删除记录");
        btnDelete.setPreferredSize(new Dimension(90, 32));
        btnDelete.addActionListener(e -> deleteRecord());
        histTitlePanel.add(btnDelete, BorderLayout.EAST);

        String[] columns = {"ID", "身高(cm)", "体重(kg)", "BMI值", "评估结果", "评估时间"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.getTableHeader().setBackground(new Color(240, 248, 255));
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        table.setSelectionBackground(new Color(200, 220, 240));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        tableScroll.setPreferredSize(new Dimension(0, 180));

        historyPanel.add(histTitlePanel, BorderLayout.NORTH);
        historyPanel.add(tableScroll, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(calcPanel, BorderLayout.CENTER);
        add(historyPanel, BorderLayout.SOUTH);
    }

    private void calculateBMI() {
        String heightStr = txtHeight.getText().trim();
        String weightStr = txtWeight.getText().trim();

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

        double bmi = bmiService.calculateBMI(height, weight);
        String assessment = bmiService.getAssessment(bmi);
        String recommendation = bmiService.getDietRecommendation(bmi);

        // 显示结果
        resultPanel.setVisible(true);
        lblBMIResult.setText("您的BMI指数: " + bmi);
        lblBMIResult.setForeground(UIUtil.getBMIColor(bmi));
        lblAssessment.setText("评估结果: " + assessment);
        lblAssessment.setForeground(UIUtil.getBMIColor(bmi));
        txtRecommendation.setText(recommendation);

        // 保存记录
        BMIRecord record = new BMIRecord(currentUser.getId(), height, weight, bmi, assessment, recommendation);
        bmiService.saveRecord(record);

        // 更新用户身高体重
        currentUser.setHeight(height);
        currentUser.setWeight(weight);
        new com.bmi.service.UserService().updateProfile(currentUser);

        // ★ 通知首页和个人中心刷新
        if (parentFrame instanceof UserMainFrame) {
            ((UserMainFrame) parentFrame).refreshHomePanel();
            ((UserMainFrame) parentFrame).refreshPersonalPanel();
        }

        loadHistory();
        UIUtil.showInfo(this, "BMI评估完成！已保存记录。");
    }

    private void loadHistory() {
        tableModel.setRowCount(0);
        List<BMIRecord> records = bmiService.findRecordsByUserId(currentUser.getId());
        for (BMIRecord r : records) {
            tableModel.addRow(new Object[]{
                r.getId(), r.getHeight(), r.getWeight(), r.getBmi(),
                r.getAssessment(), r.getCreateTimeStr()
            });
        }
    }

    private void deleteRecord() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UIUtil.showError(this, "请选择要删除的记录！");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        if (UIUtil.showConfirm(this, "确定要删除这条记录吗？")) {
            if (bmiService.deleteRecord(id)) {
                UIUtil.showInfo(this, "删除成功！");
                loadHistory();
            } else {
                UIUtil.showError(this, "删除失败！");
            }
        }
    }
}

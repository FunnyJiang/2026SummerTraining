package com.bmi.ui.admin;

import com.bmi.model.Recipe;
import com.bmi.model.RecipeSchedule;
import com.bmi.model.User;
import com.bmi.service.ForumService;
import com.bmi.service.RecipeService;
import com.bmi.service.UserService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 食谱健康安排管理面板
 */
public class RecipeSchedulePanel extends JPanel {
    private ForumService forumService = new ForumService();
    private RecipeService recipeService = new RecipeService();
    private UserService userService = new UserService();
    private JTable table;
    private DefaultTableModel tableModel;

    public RecipeSchedulePanel() {
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.USER_BG);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        JLabel titleLabel = new JLabel("食谱健康安排管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 工具栏
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JButton btnRefresh = UIUtil.createPrimaryButton("刷新");
        btnRefresh.setPreferredSize(new Dimension(80, 36));
        btnRefresh.addActionListener(e -> loadData());
        toolbar.add(btnRefresh);

        JButton btnAdd = UIUtil.createSuccessButton("添加安排");
        btnAdd.setPreferredSize(new Dimension(100, 36));
        btnAdd.addActionListener(e -> showScheduleDialog(null));
        toolbar.add(btnAdd);

        JButton btnEdit = UIUtil.createPrimaryButton("编辑");
        btnEdit.setPreferredSize(new Dimension(80, 36));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要编辑的安排！"); return; }
            editSchedule(row);
        });
        toolbar.add(btnEdit);

        JButton btnDelete = UIUtil.createDangerButton("删除");
        btnDelete.setPreferredSize(new Dimension(80, 36));
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要删除的安排！"); return; }
            deleteSchedule(row);
        });
        toolbar.add(btnDelete);

        // 表格
        String[] columns = {"ID", "食谱名称", "适用用户", "餐次", "安排日期", "时间周期", "目标BMI范围", "备注"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(UIUtil.USER_PRIMARY_LIGHT);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        table.setSelectionBackground(new Color(200, 230, 201));
        table.setSelectionForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIUtil.USER_BG);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(toolbar, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<RecipeSchedule> list = forumService.findAllSchedules();
        for (RecipeSchedule s : list) {
            tableModel.addRow(new Object[]{
                s.getId(), s.getRecipeName(), s.getUserName(),
                s.getMealType(), s.getScheduleDateStr(), s.getPeriodStr(),
                s.getTargetBmiRange(), s.getNotes()
            });
        }
    }

    private void showScheduleDialog(RecipeSchedule schedule) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                schedule == null ? "添加食谱安排" : "编辑食谱安排", true);
        dialog.setSize(480, 550);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 食谱选择
        JComboBox<Recipe> cmbRecipe = new JComboBox<>();
        for (Recipe r : recipeService.findAllRecipes()) {
            cmbRecipe.addItem(r);
        }

        // 用户选择
        JComboBox<User> cmbUser = new JComboBox<>();
        User generalUser = new User();
        generalUser.setId(0);
        generalUser.setUsername("通用(所有用户)");
        cmbUser.addItem(generalUser);
        for (User u : userService.findAllUsers()) {
            if ("user".equals(u.getRole())) cmbUser.addItem(u);
        }

        // 餐次
        String[] mealTypes = {"早餐", "午餐", "晚餐", "加餐"};
        JComboBox<String> cmbMealType = new JComboBox<>(mealTypes);

        // 日期选择
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner spnDate = new JSpinner(dateModel);
        spnDate.setEditor(new JSpinner.DateEditor(spnDate, "yyyy-MM-dd"));

        SpinnerDateModel startModel = new SpinnerDateModel();
        JSpinner spnStart = new JSpinner(startModel);
        spnStart.setEditor(new JSpinner.DateEditor(spnStart, "yyyy-MM-dd"));

        SpinnerDateModel endModel = new SpinnerDateModel();
        JSpinner spnEnd = new JSpinner(endModel);
        spnEnd.setEditor(new JSpinner.DateEditor(spnEnd, "yyyy-MM-dd"));

        // BMI范围
        String[] bmiRanges = {"<18.5", "18.5-24.9", "25-29.9", ">=30", "不限"};
        JComboBox<String> cmbBmi = new JComboBox<>(bmiRanges);

        JTextField txtNotes = UIUtil.createTextField(18);

        if (schedule != null) {
            for (int i = 0; i < cmbRecipe.getItemCount(); i++) {
                if (cmbRecipe.getItemAt(i).getId() == schedule.getRecipeId()) {
                    cmbRecipe.setSelectedIndex(i); break;
                }
            }
            if (schedule.getUserId() != null) {
                for (int i = 0; i < cmbUser.getItemCount(); i++) {
                    if (cmbUser.getItemAt(i).getId() == schedule.getUserId()) {
                        cmbUser.setSelectedIndex(i); break;
                    }
                }
            }
            cmbMealType.setSelectedItem(schedule.getMealType());
            if (schedule.getScheduleDate() != null) spnDate.setValue(schedule.getScheduleDate());
            if (schedule.getPeriodStart() != null) spnStart.setValue(schedule.getPeriodStart());
            if (schedule.getPeriodEnd() != null) spnEnd.setValue(schedule.getPeriodEnd());
            cmbBmi.setSelectedItem(schedule.getTargetBmiRange());
            txtNotes.setText(schedule.getNotes());
        }

        String[] labels = {"食谱 *:", "适用用户:", "餐次:", "安排日期:", "周期开始:", "周期结束:", "目标BMI:", "备注:"};
        JComponent[] comps = {cmbRecipe, cmbUser, cmbMealType, spnDate, spnStart, spnEnd, cmbBmi, txtNotes};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(comps[i], gbc);
        }

        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSave = UIUtil.createPrimaryButton("保存");
        JButton btnCancel = new JButton("取消");
        btnCancel.setPreferredSize(new Dimension(80, 36));
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, gbc);

        btnSave.addActionListener(e -> {
            if (cmbRecipe.getSelectedItem() == null) {
                UIUtil.showError(dialog, "请选择食谱！");
                return;
            }
            RecipeSchedule s = schedule != null ? schedule : new RecipeSchedule();
            s.setRecipeId(((Recipe) cmbRecipe.getSelectedItem()).getId());
            User selectedUser = (User) cmbUser.getSelectedItem();
            s.setUserId(selectedUser.getId() > 0 ? selectedUser.getId() : null);
            s.setMealType((String) cmbMealType.getSelectedItem());
            s.setScheduleDate((java.util.Date) spnDate.getValue());
            s.setPeriodStart((java.util.Date) spnStart.getValue());
            s.setPeriodEnd((java.util.Date) spnEnd.getValue());
            s.setTargetBmiRange((String) cmbBmi.getSelectedItem());
            s.setNotes(txtNotes.getText().trim());

            boolean success = schedule != null ? forumService.updateSchedule(s) : forumService.addSchedule(s);
            if (success) {
                UIUtil.showInfo(dialog, "保存成功！");
                dialog.dispose();
                loadData();
            } else {
                UIUtil.showError(dialog, "保存失败！");
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void editSchedule(int row) {
        List<RecipeSchedule> list = forumService.findAllSchedules();
        if (row < list.size()) {
            showScheduleDialog(list.get(row));
        }
    }

    private void deleteSchedule(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        if (UIUtil.showConfirm(this, "确定要删除这条食谱安排吗？")) {
            if (forumService.deleteSchedule(id)) {
                UIUtil.showInfo(this, "删除成功！");
                loadData();
            } else {
                UIUtil.showError(this, "删除失败！");
            }
        }
    }
}

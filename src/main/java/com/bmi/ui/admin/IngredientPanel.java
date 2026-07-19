package com.bmi.ui.admin;

import com.bmi.model.Ingredient;
import com.bmi.service.RecipeService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 材料信息管理面板
 */
public class IngredientPanel extends JPanel {
    private RecipeService recipeService = new RecipeService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public IngredientPanel() {
        initUI();
        loadData(null);
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
        JLabel titleLabel = new JLabel("材料信息管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 工具栏
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        toolbar.add(UIUtil.createLabel("搜索:"));
        txtSearch = UIUtil.createTextField(15);
        toolbar.add(txtSearch);

        JButton btnSearch = UIUtil.createPrimaryButton("搜索");
        btnSearch.setPreferredSize(new Dimension(80, 36));
        btnSearch.addActionListener(e -> loadData(txtSearch.getText().trim()));
        toolbar.add(btnSearch);

        JButton btnAdd = UIUtil.createSuccessButton("添加材料");
        btnAdd.setPreferredSize(new Dimension(100, 36));
        btnAdd.addActionListener(e -> showIngredientDialog(null));
        toolbar.add(btnAdd);

        JButton btnEdit = UIUtil.createPrimaryButton("编辑");
        btnEdit.setPreferredSize(new Dimension(80, 36));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要编辑的材料！"); return; }
            editIngredient(row);
        });
        toolbar.add(btnEdit);

        JButton btnDelete = UIUtil.createDangerButton("删除");
        btnDelete.setPreferredSize(new Dimension(80, 36));
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要删除的材料！"); return; }
            deleteIngredient(row);
        });
        toolbar.add(btnDelete);

        // 表格
        String[] columns = {"ID", "材料名称", "分类", "热量(千卡/100g)", "单位", "描述", "创建时间"};
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

    private void loadData(String keyword) {
        tableModel.setRowCount(0);
        List<Ingredient> list = recipeService.searchIngredients(keyword);
        for (Ingredient ing : list) {
            tableModel.addRow(new Object[]{
                ing.getId(), ing.getName(), ing.getCategory(),
                ing.getCalories(), ing.getUnit(), ing.getDescription(),
                ing.getCreateTimeStr()
            });
        }
    }

    private void showIngredientDialog(Ingredient ingredient) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                ingredient == null ? "添加材料" : "编辑材料", true);
        dialog.setSize(420, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = UIUtil.createTextField(18);
        String[] categories = {"蔬菜", "肉类", "蛋类", "乳制品", "豆制品", "谷物", "干货", "水果", "调味品", "其他"};
        JComboBox<String> cmbCategory = new JComboBox<>(categories);
        JTextField txtCalories = UIUtil.createTextField(18);
        JTextField txtUnit = UIUtil.createTextField(18);
        txtUnit.setText("g");
        JTextField txtDesc = UIUtil.createTextField(18);

        if (ingredient != null) {
            txtName.setText(ingredient.getName());
            cmbCategory.setSelectedItem(ingredient.getCategory());
            txtCalories.setText(String.valueOf(ingredient.getCalories()));
            txtUnit.setText(ingredient.getUnit());
            txtDesc.setText(ingredient.getDescription());
        }

        String[] labels = {"材料名称 *:", "分类:", "热量(千卡/100g):", "单位:", "描述:"};
        JComponent[] comps = {txtName, cmbCategory, txtCalories, txtUnit, txtDesc};

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
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                UIUtil.showError(dialog, "材料名称不能为空！");
                return;
            }
            try {
                Ingredient ing = ingredient != null ? ingredient : new Ingredient();
                ing.setName(name);
                ing.setCategory((String) cmbCategory.getSelectedItem());
                ing.setCalories(txtCalories.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtCalories.getText().trim()));
                ing.setUnit(txtUnit.getText().trim());
                ing.setDescription(txtDesc.getText().trim());

                boolean success = ingredient != null ? recipeService.updateIngredient(ing) : recipeService.addIngredient(ing);
                if (success) {
                    UIUtil.showInfo(dialog, "保存成功！");
                    dialog.dispose();
                    loadData(txtSearch.getText().trim());
                } else {
                    UIUtil.showError(dialog, "保存失败！");
                }
            } catch (NumberFormatException ex) {
                UIUtil.showError(dialog, "热量请输入数字！");
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void editIngredient(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        Ingredient ing = recipeService.findIngredientById(id);
        if (ing != null) showIngredientDialog(ing);
    }

    private void deleteIngredient(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        if (UIUtil.showConfirm(this, "确定要删除材料「" + name + "」吗？")) {
            if (recipeService.deleteIngredient(id)) {
                UIUtil.showInfo(this, "删除成功！");
                loadData(txtSearch.getText().trim());
            } else {
                UIUtil.showError(this, "删除失败！");
            }
        }
    }
}

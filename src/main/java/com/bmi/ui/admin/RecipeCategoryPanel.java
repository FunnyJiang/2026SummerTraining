package com.bmi.ui.admin;

import com.bmi.model.RecipeCategory;
import com.bmi.service.RecipeService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 食谱分类管理面板
 */
public class RecipeCategoryPanel extends JPanel {
    private RecipeService recipeService = new RecipeService();
    private JTable table;
    private DefaultTableModel tableModel;

    public RecipeCategoryPanel() {
        initUI();
        loadData();
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
        JLabel titleLabel = new JLabel("食谱分类管理");
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

        JButton btnAdd = UIUtil.createSuccessButton("添加分类");
        btnAdd.setPreferredSize(new Dimension(100, 36));
        btnAdd.addActionListener(e -> showCategoryDialog(null));
        toolbar.add(btnAdd);

        JButton btnEdit = UIUtil.createPrimaryButton("编辑");
        btnEdit.setPreferredSize(new Dimension(80, 36));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要编辑的分类！"); return; }
            editCategory(row);
        });
        toolbar.add(btnEdit);

        JButton btnDelete = UIUtil.createDangerButton("删除");
        btnDelete.setPreferredSize(new Dimension(80, 36));
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要删除的分类！"); return; }
            deleteCategory(row);
        });
        toolbar.add(btnDelete);

        // 表格
        String[] columns = {"ID", "分类名称", "描述", "创建时间"};
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
        List<RecipeCategory> list = recipeService.findAllCategories();
        for (RecipeCategory c : list) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getDescription(), c.getCreateTimeStr()});
        }
    }

    private void showCategoryDialog(RecipeCategory category) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                category == null ? "添加分类" : "编辑分类", true);
        dialog.setSize(420, 250);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = UIUtil.createTextField(18);
        JTextField txtDesc = UIUtil.createTextField(18);

        if (category != null) {
            txtName.setText(category.getName());
            txtDesc.setText(category.getDescription());
        }

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(UIUtil.createLabel("分类名称 *:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(UIUtil.createLabel("描述:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(txtDesc, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
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
                UIUtil.showError(dialog, "分类名称不能为空！");
                return;
            }
            RecipeCategory c = category != null ? category : new RecipeCategory();
            c.setName(name);
            c.setDescription(txtDesc.getText().trim());

            boolean success = category != null ? recipeService.updateCategory(c) : recipeService.addCategory(c);
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

    private void editCategory(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        RecipeCategory c = recipeService.findCategoryById(id);
        if (c != null) showCategoryDialog(c);
    }

    private void deleteCategory(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        if (UIUtil.showConfirm(this, "删除分类「" + name + "」将同时删除该分类下的所有食谱，确定删除吗？")) {
            if (recipeService.deleteCategory(id)) {
                UIUtil.showInfo(this, "删除成功！");
                loadData();
            } else {
                UIUtil.showError(this, "删除失败！");
            }
        }
    }
}

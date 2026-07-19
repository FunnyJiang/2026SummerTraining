package com.bmi.ui.admin;

import com.bmi.model.Recipe;
import com.bmi.model.RecipeCategory;
import com.bmi.service.RecipeService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 食谱信息管理面板
 */
public class RecipeManagePanel extends JPanel {
    private RecipeService recipeService = new RecipeService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JComboBox<RecipeCategory> cmbCategory;

    public RecipeManagePanel() {
        initUI();
        loadData(null, 0);
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
        JLabel titleLabel = new JLabel("食谱信息管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 工具栏
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        toolbar.add(UIUtil.createLabel("分类:"));
        cmbCategory = new JComboBox<>();
        cmbCategory.setPreferredSize(new Dimension(130, 32));
        loadCategories();
        toolbar.add(cmbCategory);

        toolbar.add(UIUtil.createLabel("名称:"));
        txtSearch = UIUtil.createTextField(12);
        toolbar.add(txtSearch);

        JButton btnSearch = UIUtil.createPrimaryButton("搜索");
        btnSearch.setPreferredSize(new Dimension(80, 36));
        btnSearch.addActionListener(e -> {
            int catId = cmbCategory.getSelectedIndex() > 0 ?
                ((RecipeCategory) cmbCategory.getSelectedItem()).getId() : 0;
            loadData(txtSearch.getText().trim(), catId);
        });
        toolbar.add(btnSearch);

        JButton btnAdd = UIUtil.createSuccessButton("添加食谱");
        btnAdd.setPreferredSize(new Dimension(100, 36));
        btnAdd.addActionListener(e -> showRecipeDialog(null));
        toolbar.add(btnAdd);

        JButton btnEdit = UIUtil.createPrimaryButton("编辑");
        btnEdit.setPreferredSize(new Dimension(80, 36));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要编辑的食谱！"); return; }
            editRecipe(row);
        });
        toolbar.add(btnEdit);

        JButton btnDelete = UIUtil.createDangerButton("删除");
        btnDelete.setPreferredSize(new Dimension(80, 36));
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要删除的食谱！"); return; }
            deleteRecipe(row);
        });
        toolbar.add(btnDelete);

        // 表格
        String[] columns = {"ID", "名称", "分类", "热量(千卡)", "蛋白质(g)", "脂肪(g)", "碳水(g)", "适合BMI", "描述"};
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

    private void loadCategories() {
        cmbCategory.removeAllItems();
        RecipeCategory allCat = new RecipeCategory();
        allCat.setId(0);
        allCat.setName("全部分类");
        cmbCategory.addItem(allCat);
        for (RecipeCategory c : recipeService.findAllCategories()) {
            cmbCategory.addItem(c);
        }
    }

    private void loadData(String keyword, int categoryId) {
        tableModel.setRowCount(0);
        List<Recipe> list = recipeService.searchRecipes(keyword, categoryId);
        for (Recipe r : list) {
            tableModel.addRow(new Object[]{
                r.getId(), r.getName(), r.getCategoryName(),
                r.getCalories(), r.getProtein(), r.getFat(), r.getCarbs(),
                r.getSuitableBmi(), r.getDescription()
            });
        }
    }

    private void showRecipeDialog(Recipe recipe) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                recipe == null ? "添加食谱" : "编辑食谱", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<RecipeCategory> cmbCat = new JComboBox<>();
        for (RecipeCategory c : recipeService.findAllCategories()) {
            cmbCat.addItem(c);
        }

        JTextField txtName = UIUtil.createTextField(18);
        JTextField txtCalories = UIUtil.createTextField(18);
        JTextField txtProtein = UIUtil.createTextField(18);
        JTextField txtFat = UIUtil.createTextField(18);
        JTextField txtCarbs = UIUtil.createTextField(18);
        JTextField txtSuitBmi = UIUtil.createTextField(18);
        JTextArea txtDesc = UIUtil.createTextArea();

        String[] bmiOptions = {"<18.5", "18.5-24.9", "25-29.9", ">=30", "不限"};
        JComboBox<String> cmbBmi = new JComboBox<>(bmiOptions);

        if (recipe != null) {
            txtName.setText(recipe.getName());
            txtCalories.setText(String.valueOf(recipe.getCalories()));
            txtProtein.setText(String.valueOf(recipe.getProtein()));
            txtFat.setText(String.valueOf(recipe.getFat()));
            txtCarbs.setText(String.valueOf(recipe.getCarbs()));
            txtDesc.setText(recipe.getDescription());
            for (int i = 0; i < cmbCat.getItemCount(); i++) {
                if (cmbCat.getItemAt(i).getId() == recipe.getCategoryId()) {
                    cmbCat.setSelectedIndex(i); break;
                }
            }
        }

        String[] labels = {"分类 *:", "名称 *:", "热量(千卡):", "蛋白质(g):", "脂肪(g):", "碳水(g):", "适合BMI:"};
        JComponent[] comps = {cmbCat, txtName, txtCalories, txtProtein, txtFat, txtCarbs, cmbBmi};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            panel.add(comps[i], gbc);
        }

        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0;
        panel.add(new JLabel("描述:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        panel.add(new JScrollPane(txtDesc), gbc);
        gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnSave = UIUtil.createPrimaryButton("保存");
        JButton btnCancel = new JButton("取消");
        btnCancel.setPreferredSize(new Dimension(80, 36));
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, gbc);

        btnSave.addActionListener(e -> {
            if (cmbCat.getSelectedItem() == null) {
                UIUtil.showError(dialog, "请选择分类！");
                return;
            }
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                UIUtil.showError(dialog, "食谱名称不能为空！");
                return;
            }

            try {
                Recipe r = recipe != null ? recipe : new Recipe();
                r.setCategoryId(((RecipeCategory) cmbCat.getSelectedItem()).getId());
                r.setName(name);
                r.setCalories(txtCalories.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtCalories.getText().trim()));
                r.setProtein(txtProtein.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtProtein.getText().trim()));
                r.setFat(txtFat.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtFat.getText().trim()));
                r.setCarbs(txtCarbs.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtCarbs.getText().trim()));
                r.setSuitableBmi((String) cmbBmi.getSelectedItem());
                r.setDescription(txtDesc.getText().trim());

                boolean success = recipe != null ? recipeService.updateRecipe(r) : recipeService.addRecipe(r);
                if (success) {
                    UIUtil.showInfo(dialog, "保存成功！");
                    dialog.dispose();
                    int catId = cmbCategory.getSelectedIndex() > 0 ?
                        ((RecipeCategory) cmbCategory.getSelectedItem()).getId() : 0;
                    loadData(txtSearch.getText().trim(), catId);
                } else {
                    UIUtil.showError(dialog, "保存失败！");
                }
            } catch (NumberFormatException ex) {
                UIUtil.showError(dialog, "营养数据请输入数字！");
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void editRecipe(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        Recipe r = recipeService.findRecipeById(id);
        if (r != null) showRecipeDialog(r);
    }

    private void deleteRecipe(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        if (UIUtil.showConfirm(this, "确定要删除食谱「" + name + "」吗？")) {
            if (recipeService.deleteRecipe(id)) {
                UIUtil.showInfo(this, "删除成功！");
                int catId = cmbCategory.getSelectedIndex() > 0 ?
                    ((RecipeCategory) cmbCategory.getSelectedItem()).getId() : 0;
                loadData(txtSearch.getText().trim(), catId);
            } else {
                UIUtil.showError(this, "删除失败！");
            }
        }
    }
}

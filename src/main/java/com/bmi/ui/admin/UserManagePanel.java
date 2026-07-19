package com.bmi.ui.admin;

import com.bmi.model.User;
import com.bmi.service.UserService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用户管理面板
 */
public class UserManagePanel extends JPanel {
    private UserService userService = new UserService();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public UserManagePanel() {
        initUI();
        loadData(null);
    }

    /**
     * ★ 外部调用刷新 - 切换到此面板或管理员/用户修改信息后自动刷新
     */
    public void refreshData() {
        loadData(txtSearch.getText().trim());
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
        JLabel titleLabel = new JLabel("用户管理");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 工具栏
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbarPanel.setBackground(Color.WHITE);
        toolbarPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        toolbarPanel.add(UIUtil.createLabel("搜索:"));
        txtSearch = UIUtil.createTextField(15);
        toolbarPanel.add(txtSearch);

        JButton btnSearch = UIUtil.createPrimaryButton("搜索");
        btnSearch.setPreferredSize(new Dimension(80, 36));
        btnSearch.addActionListener(e -> loadData(txtSearch.getText().trim()));
        toolbarPanel.add(btnSearch);

        JButton btnAdd = UIUtil.createSuccessButton("添加用户");
        btnAdd.setPreferredSize(new Dimension(100, 36));
        btnAdd.addActionListener(e -> showUserDialog(null));
        toolbarPanel.add(btnAdd);

        JButton btnEdit = UIUtil.createPrimaryButton("编辑");
        btnEdit.setPreferredSize(new Dimension(80, 36));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                UIUtil.showError(this, "请选择要编辑的用户！");
                return;
            }
            editUser(row);
        });
        toolbarPanel.add(btnEdit);

        JButton btnDelete = UIUtil.createDangerButton("删除");
        btnDelete.setPreferredSize(new Dimension(80, 36));
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                UIUtil.showError(this, "请选择要删除的用户！");
                return;
            }
            deleteUser(row);
        });
        toolbarPanel.add(btnDelete);

        JButton btnToggle = UIUtil.createButton("启用/禁用", UIUtil.WARNING_COLOR);
        btnToggle.setPreferredSize(new Dimension(100, 36));
        btnToggle.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                UIUtil.showError(this, "请选择要操作的用户！");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            if (userService.toggleStatus(id)) {
                UIUtil.showInfo(this, "状态切换成功！");
                loadData(txtSearch.getText().trim());
            } else {
                UIUtil.showError(this, "操作失败！");
            }
        });
        toolbarPanel.add(btnToggle);

        // 表格
        String[] columns = {"ID", "用户名", "真实姓名", "性别", "年龄", "身高(cm)", "体重(kg)", "BMI", "手机号", "角色", "状态", "注册时间"};
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

        // 组装
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(UIUtil.USER_BG);
        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(toolbarPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadData(String keyword) {
        tableModel.setRowCount(0);
        java.util.List<User> users = userService.searchUsers(keyword);
        for (User u : users) {
            double bmi = u.getBMI();
            String bmiStr = bmi > 0 ? String.valueOf(bmi) : "未设置";
            tableModel.addRow(new Object[]{
                u.getId(), u.getUsername(), u.getRealName(), u.getGender(),
                u.getAge(), u.getHeight(), u.getWeight(), bmiStr,
                u.getPhone(), u.getRoleStr(), u.getStatusStr(), u.getCreateTimeStr()
            });
        }
    }

    private void showUserDialog(User user) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                user == null ? "添加用户" : "编辑用户", true);
        dialog.setSize(450, 520);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtUsername = UIUtil.createTextField(18);
        JPasswordField txtPassword = UIUtil.createPasswordField(18);
        JTextField txtRealName = UIUtil.createTextField(18);
        JRadioButton rbMale = new JRadioButton("男", true);
        JRadioButton rbFemale = new JRadioButton("女");
        rbMale.setBackground(Color.WHITE); rbFemale.setBackground(Color.WHITE);
        ButtonGroup bg = new ButtonGroup(); bg.add(rbMale); bg.add(rbFemale);
        JTextField txtAge = UIUtil.createTextField(18);
        JTextField txtHeight = UIUtil.createTextField(18);
        JTextField txtWeight = UIUtil.createTextField(18);
        JTextField txtPhone = UIUtil.createTextField(18);
        JTextField txtEmail = UIUtil.createTextField(18);
        JRadioButton rbUser = new JRadioButton("普通用户", true);
        JRadioButton rbAdmin = new JRadioButton("管理员");
        rbUser.setBackground(Color.WHITE); rbAdmin.setBackground(Color.WHITE);
        ButtonGroup bgRole = new ButtonGroup(); bgRole.add(rbUser); bgRole.add(rbAdmin);

        if (user != null) {
            txtUsername.setText(user.getUsername());
            txtPassword.setText(user.getPassword());
            txtRealName.setText(user.getRealName());
            if ("女".equals(user.getGender())) rbFemale.setSelected(true);
            txtAge.setText(String.valueOf(user.getAge()));
            txtHeight.setText(String.valueOf(user.getHeight()));
            txtWeight.setText(String.valueOf(user.getWeight()));
            txtPhone.setText(user.getPhone());
            txtEmail.setText(user.getEmail());
            if ("admin".equals(user.getRole())) rbAdmin.setSelected(true);
            txtUsername.setEnabled(false);
        }

        String[] labels = {"用户名 *:", "密码 *:", "真实姓名:", "性别:", "年龄:", "身高(cm):", "体重(kg):", "手机号:", "邮箱:", "角色:"};
        JComponent[] comps = {txtUsername, txtPassword, txtRealName,
            createGenderPanel(rbMale, rbFemale), txtAge, txtHeight, txtWeight, txtPhone, txtEmail,
            createGenderPanel(rbUser, rbAdmin)};

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
        btnPanel.add(btnSave); btnPanel.add(btnCancel);
        panel.add(btnPanel, gbc);

        btnSave.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                UIUtil.showError(dialog, "用户名和密码不能为空！");
                return;
            }

            User u = user != null ? user : new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setRealName(txtRealName.getText().trim());
            u.setGender(rbMale.isSelected() ? "男" : "女");
            try {
                u.setAge(txtAge.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtAge.getText().trim()));
                u.setHeight(txtHeight.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtHeight.getText().trim()));
                u.setWeight(txtWeight.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtWeight.getText().trim()));
            } catch (NumberFormatException ex) {
                UIUtil.showError(dialog, "年龄、身高、体重请输入数字！");
                return;
            }
            u.setPhone(txtPhone.getText().trim());
            u.setEmail(txtEmail.getText().trim());
            u.setRole(rbAdmin.isSelected() ? "admin" : "user");
            u.setStatus(1);

            boolean success;
            if (user != null) {
                success = userService.updateUser(u);
            } else {
                success = userService.addUser(u);
            }

            if (success) {
                UIUtil.showInfo(dialog, "保存成功！");
                dialog.dispose();
                loadData(txtSearch.getText().trim());
            } else {
                UIUtil.showError(dialog, user != null ? "保存失败！" : "用户名已存在，添加失败！");
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private JPanel createGenderPanel(JRadioButton r1, JRadioButton r2) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(Color.WHITE);
        p.add(r1); p.add(Box.createHorizontalStrut(20)); p.add(r2);
        return p;
    }

    private void editUser(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        User user = userService.findById(id);
        if (user != null) {
            showUserDialog(user);
        }
    }

    private void deleteUser(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String username = (String) tableModel.getValueAt(row, 1);
        if ("admin".equals(username)) {
            UIUtil.showError(this, "不能删除管理员账户！");
            return;
        }
        if (UIUtil.showConfirm(this, "确定要删除用户 " + username + " 吗？")) {
            if (userService.deleteUser(id)) {
                UIUtil.showInfo(this, "删除成功！");
                loadData(txtSearch.getText().trim());
            } else {
                UIUtil.showError(this, "删除失败！");
            }
        }
    }
}

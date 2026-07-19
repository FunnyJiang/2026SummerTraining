package com.bmi.ui.user;

import com.bmi.model.User;
import com.bmi.service.UserService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * 用户个人中心面板 - BMI实时更新
 */
public class UserPersonalPanel extends JPanel {
    private User currentUser;
    private JFrame parentFrame;
    private UserService userService = new UserService();

    private JTextField txtRealName, txtAge, txtHeight, txtWeight, txtPhone, txtEmail;
    private JRadioButton rbMale, rbFemale;
    private JPasswordField txtOldPassword, txtNewPassword, txtConfirmPassword;
    private JLabel lblBMI;  // BMI标签，用于实时更新

    public UserPersonalPanel(User user, JFrame frame) {
        this.currentUser = user;
        this.parentFrame = frame;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.USER_BG);

        // 标题
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtil.USER_BORDER),
            BorderFactory.createEmptyBorder(18, 28, 18, 28)
        ));
        JLabel titleLabel = new JLabel("个人中心");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(UIUtil.USER_TEXT);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 内容区
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        contentPanel.add(UIUtil.createLabel("用户名"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        JLabel lblUsername = new JLabel(currentUser.getUsername());
        lblUsername.setFont(new Font("微软雅黑", Font.BOLD, 14));
        contentPanel.add(lblUsername, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        contentPanel.add(UIUtil.createLabel("角色"), gbc);
        gbc.gridx = 1;
        JLabel lblRole = new JLabel(currentUser.getRoleStr());
        lblRole.setFont(new Font("微软雅黑", Font.BOLD, 14));
        contentPanel.add(lblRole, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("真实姓名"), gbc);
        gbc.gridx = 1;
        txtRealName = UIUtil.createUserTextField(20);
        txtRealName.setText(currentUser.getRealName());
        contentPanel.add(txtRealName, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("性别"), gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.setBackground(Color.WHITE);
        rbMale = new JRadioButton("男", "男".equals(currentUser.getGender()));
        rbFemale = new JRadioButton("女", "女".equals(currentUser.getGender()));
        rbMale.setBackground(Color.WHITE);
        rbFemale.setBackground(Color.WHITE);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMale);
        bg.add(rbFemale);
        genderPanel.add(rbMale);
        genderPanel.add(Box.createHorizontalStrut(20));
        genderPanel.add(rbFemale);
        contentPanel.add(genderPanel, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("年龄"), gbc);
        gbc.gridx = 1;
        txtAge = UIUtil.createUserTextField(20);
        txtAge.setText(String.valueOf(currentUser.getAge()));
        contentPanel.add(txtAge, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("身高(cm)"), gbc);
        gbc.gridx = 1;
        txtHeight = UIUtil.createUserTextField(20);
        txtHeight.setText(currentUser.getHeight() > 0 ? String.valueOf(currentUser.getHeight()) : "");
        contentPanel.add(txtHeight, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("体重(kg)"), gbc);
        gbc.gridx = 1;
        txtWeight = UIUtil.createUserTextField(20);
        txtWeight.setText(currentUser.getWeight() > 0 ? String.valueOf(currentUser.getWeight()) : "");
        contentPanel.add(txtWeight, gbc);

        // ★ BMI标签 - 实时更新
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("当前BMI"), gbc);
        gbc.gridx = 1;
        lblBMI = new JLabel();
        updateBMILabel(); // 初始化BMI显示
        lblBMI.setFont(new Font("微软雅黑", Font.BOLD, 14));
        contentPanel.add(lblBMI, gbc);

        // ★ 为身高和体重输入框添加实时监听
        txtHeight.getDocument().addDocumentListener(new BMIDocumentListener());
        txtWeight.getDocument().addDocumentListener(new BMIDocumentListener());

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("手机号"), gbc);
        gbc.gridx = 1;
        txtPhone = UIUtil.createUserTextField(20);
        txtPhone.setText(currentUser.getPhone());
        contentPanel.add(txtPhone, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("邮箱"), gbc);
        gbc.gridx = 1;
        txtEmail = UIUtil.createUserTextField(20);
        txtEmail.setText(currentUser.getEmail());
        contentPanel.add(txtEmail, gbc);

        // 保存按钮
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JButton btnSave = UIUtil.createUserPrimaryButton("保存修改");
        btnSave.setPreferredSize(new Dimension(120, 40));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnSave);
        contentPanel.add(btnPanel, gbc);

        // 密码修改区
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        contentPanel.add(new JSeparator(), gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JLabel pwdTitle = new JLabel("修改密码");
        pwdTitle.setFont(new Font("微软雅黑", Font.BOLD, 15));
        contentPanel.add(pwdTitle, gbc);
        gbc.gridwidth = 1;

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("原密码"), gbc);
        gbc.gridx = 1;
        txtOldPassword = UIUtil.createUserPasswordField(20);
        contentPanel.add(txtOldPassword, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("新密码"), gbc);
        gbc.gridx = 1;
        txtNewPassword = UIUtil.createUserPasswordField(20);
        contentPanel.add(txtNewPassword, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row;
        contentPanel.add(UIUtil.createLabel("确认密码"), gbc);
        gbc.gridx = 1;
        txtConfirmPassword = UIUtil.createUserPasswordField(20);
        contentPanel.add(txtConfirmPassword, gbc);

        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JButton btnChangePwd = UIUtil.createUserPrimaryButton("修改密码");
        btnChangePwd.setPreferredSize(new Dimension(120, 40));
        JPanel pwdBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pwdBtnPanel.setBackground(Color.WHITE);
        pwdBtnPanel.add(btnChangePwd);
        contentPanel.add(pwdBtnPanel, gbc);

        btnSave.addActionListener(e -> {
            try {
                currentUser.setRealName(txtRealName.getText().trim());
                currentUser.setGender(rbMale.isSelected() ? "男" : "女");
                currentUser.setAge(txtAge.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtAge.getText().trim()));
                currentUser.setHeight(txtHeight.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtHeight.getText().trim()));
                currentUser.setWeight(txtWeight.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtWeight.getText().trim()));
                currentUser.setPhone(txtPhone.getText().trim());
                currentUser.setEmail(txtEmail.getText().trim());

                if (userService.updateProfile(currentUser)) {
                    UIUtil.showInfo(this, "个人信息修改成功！");
                    updateBMILabel(); // ★ 保存后也更新BMI
                    // ★ 同步刷新首页数据（BMI状态 + 推荐食谱 + 侧边栏用户名）
                    if (parentFrame instanceof UserMainFrame) {
                        ((UserMainFrame) parentFrame).refreshHomePanel();
                    }
                } else {
                    UIUtil.showError(this, "修改失败！");
                }
            } catch (NumberFormatException ex) {
                UIUtil.showError(this, "年龄、身高、体重请输入数字！");
            }
        });

        btnChangePwd.addActionListener(e -> {
            String oldPwd = new String(txtOldPassword.getPassword());
            String newPwd = new String(txtNewPassword.getPassword());
            String confirmPwd = new String(txtConfirmPassword.getPassword());

            if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
                UIUtil.showError(this, "请填写所有密码字段！");
                return;
            }
            if (!oldPwd.equals(currentUser.getPassword())) {
                UIUtil.showError(this, "原密码不正确！");
                return;
            }
            if (newPwd.length() < 6) {
                UIUtil.showError(this, "新密码至少6个字符！");
                return;
            }
            if (!newPwd.equals(confirmPwd)) {
                UIUtil.showError(this, "两次输入的新密码不一致！");
                return;
            }

            currentUser.setPassword(newPwd);
            if (userService.updateProfile(currentUser)) {
                UIUtil.showInfo(this, "密码修改成功！");
                txtOldPassword.setText("");
                txtNewPassword.setText("");
                txtConfirmPassword.setText("");
            } else {
                UIUtil.showError(this, "密码修改失败！");
            }
        });

        add(titlePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * ★ 根据当前输入框中的身高体重实时更新BMI标签
     */
    private void updateBMILabel() {
        String heightStr = txtHeight.getText().trim();
        String weightStr = txtWeight.getText().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            lblBMI.setText("未设置（请输入身高和体重）");
            lblBMI.setForeground(UIUtil.TEXT_LIGHT);
            return;
        }

        try {
            double height = Double.parseDouble(heightStr);
            double weight = Double.parseDouble(weightStr);
            if (height <= 0 || weight <= 0) {
                lblBMI.setText("未设置（身高体重需大于0）");
                lblBMI.setForeground(UIUtil.TEXT_LIGHT);
                return;
            }
            double bmi = weight / (height / 100.0 * height / 100.0);
            String assessment = UIUtil.getBMIAssessment(bmi);
            lblBMI.setText(String.format("%.2f (%s)", bmi, assessment));
            lblBMI.setForeground(UIUtil.getBMIColor(bmi));
        } catch (NumberFormatException e) {
            lblBMI.setText("未设置（请输入有效数字）");
            lblBMI.setForeground(UIUtil.TEXT_LIGHT);
        }
    }

    /**
     * ★ 外部刷新BMI标签（用于BMI评估后同步更新）
     */
    public void refreshBMI() {
        txtHeight.setText(currentUser.getHeight() > 0 ? String.valueOf(currentUser.getHeight()) : "");
        txtWeight.setText(currentUser.getWeight() > 0 ? String.valueOf(currentUser.getWeight()) : "");
        updateBMILabel();
    }

    /**
     * ★ 从数据库重新加载用户数据并刷新所有表单字段
     * （管理员在"用户管理"中修改了用户信息后，用户的"个人中心"需要同步显示最新数据）
     */
    public void reloadFromDB() {
        User freshUser = userService.findById(currentUser.getId());
        if (freshUser != null) {
            currentUser = freshUser;
            // 更新所有表单字段
            txtRealName.setText(currentUser.getRealName());
            if ("男".equals(currentUser.getGender())) {
                rbMale.setSelected(true);
            } else {
                rbFemale.setSelected(true);
            }
            txtAge.setText(String.valueOf(currentUser.getAge()));
            txtHeight.setText(currentUser.getHeight() > 0 ? String.valueOf(currentUser.getHeight()) : "");
            txtWeight.setText(currentUser.getWeight() > 0 ? String.valueOf(currentUser.getWeight()) : "");
            txtPhone.setText(currentUser.getPhone());
            txtEmail.setText(currentUser.getEmail());
            updateBMILabel();
        }
    }

    /**
     * ★ DocumentListener - 身高/体重输入变化时实时更新BMI
     */
    private class BMIDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) { updateBMILabel(); }
        @Override
        public void removeUpdate(DocumentEvent e) { updateBMILabel(); }
        @Override
        public void changedUpdate(DocumentEvent e) { updateBMILabel(); }
    }
}

package com.bmi.ui;

import com.bmi.model.User;
import com.bmi.service.UserService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 注册窗口
 */
public class RegisterFrame extends JFrame {
    private JTextField txtUsername, txtRealName, txtAge, txtHeight, txtWeight, txtPhone, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JRadioButton rbMale, rbFemale;
    private JButton btnRegister, btnBack;
    private UserService userService = new UserService();

    public RegisterFrame() {
        initUI();
    }

    private void initUI() {
        setTitle("用户注册 - BMI体质评估与预测系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 680);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtil.BG_COLOR);

        // 标题区
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIUtil.PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(520, 80));
        JLabel titleLabel = new JLabel("用 户 注 册", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        // 表单区
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        // 用户名
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(UIUtil.createLabel("用户名 *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtUsername = UIUtil.createTextField(20);
        formPanel.add(txtUsername, gbc);

        // 密码
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        formPanel.add(UIUtil.createLabel("密码 *"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        txtPassword = UIUtil.createPasswordField(20);
        formPanel.add(txtPassword, gbc);

        // 确认密码
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("确认密码 *"), gbc);
        gbc.gridx = 1;
        txtConfirmPassword = UIUtil.createPasswordField(20);
        formPanel.add(txtConfirmPassword, gbc);

        // 真实姓名
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("真实姓名"), gbc);
        gbc.gridx = 1;
        txtRealName = UIUtil.createTextField(20);
        formPanel.add(txtRealName, gbc);

        // 性别
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("性别"), gbc);
        gbc.gridx = 1;
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        genderPanel.setBackground(Color.WHITE);
        rbMale = new JRadioButton("男", true);
        rbFemale = new JRadioButton("女");
        rbMale.setBackground(Color.WHITE);
        rbFemale.setBackground(Color.WHITE);
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMale);
        bg.add(rbFemale);
        genderPanel.add(rbMale);
        genderPanel.add(Box.createHorizontalStrut(20));
        genderPanel.add(rbFemale);
        formPanel.add(genderPanel, gbc);

        // 年龄
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("年龄"), gbc);
        gbc.gridx = 1;
        txtAge = UIUtil.createTextField(20);
        formPanel.add(txtAge, gbc);

        // 身高
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("身高(cm)"), gbc);
        gbc.gridx = 1;
        txtHeight = UIUtil.createTextField(20);
        formPanel.add(txtHeight, gbc);

        // 体重
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("体重(kg)"), gbc);
        gbc.gridx = 1;
        txtWeight = UIUtil.createTextField(20);
        formPanel.add(txtWeight, gbc);

        // 手机号
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("手机号"), gbc);
        gbc.gridx = 1;
        txtPhone = UIUtil.createTextField(20);
        formPanel.add(txtPhone, gbc);

        // 邮箱
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        formPanel.add(UIUtil.createLabel("邮箱"), gbc);
        gbc.gridx = 1;
        txtEmail = UIUtil.createTextField(20);
        formPanel.add(txtEmail, gbc);

        // 按钮
        row++;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);
        btnRegister = UIUtil.createPrimaryButton("注 册");
        btnBack = new JButton("返回登录");
        btnBack.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btnBack.setForeground(UIUtil.PRIMARY_COLOR);
        btnBack.setBackground(Color.WHITE);
        btnBack.setBorder(BorderFactory.createLineBorder(UIUtil.PRIMARY_COLOR, 1));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setPreferredSize(new Dimension(100, 36));
        btnPanel.add(btnRegister);
        btnPanel.add(btnBack);
        formPanel.add(btnPanel, gbc);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doRegister();
            }
        });
        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void doRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String realName = txtRealName.getText().trim();
        String gender = rbMale.isSelected() ? "男" : "女";
        String ageStr = txtAge.getText().trim();
        String heightStr = txtHeight.getText().trim();
        String weightStr = txtWeight.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();

        // 验证
        if (username.isEmpty() || password.isEmpty()) {
            UIUtil.showError(this, "用户名和密码不能为空！");
            return;
        }
        if (username.length() < 3) {
            UIUtil.showError(this, "用户名至少3个字符！");
            return;
        }
        if (password.length() < 6) {
            UIUtil.showError(this, "密码至少6个字符！");
            return;
        }
        if (!password.equals(confirmPassword)) {
            UIUtil.showError(this, "两次输入的密码不一致！");
            return;
        }
        if (userService.isUsernameExists(username)) {
            UIUtil.showError(this, "该用户名已被注册！");
            return;
        }

        int age = 0;
        double height = 0, weight = 0;
        try {
            if (!ageStr.isEmpty()) age = Integer.parseInt(ageStr);
            if (!heightStr.isEmpty()) height = Double.parseDouble(heightStr);
            if (!weightStr.isEmpty()) weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            UIUtil.showError(this, "年龄、身高、体重请输入数字！");
            return;
        }

        User user = new User(username, password, "user");
        user.setRealName(realName);
        user.setGender(gender);
        user.setAge(age);
        user.setHeight(height);
        user.setWeight(weight);
        user.setPhone(phone);
        user.setEmail(email);

        if (userService.register(user)) {
            UIUtil.showInfo(this, "注册成功！请登录。");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            UIUtil.showError(this, "注册失败，请重试！");
        }
    }
}

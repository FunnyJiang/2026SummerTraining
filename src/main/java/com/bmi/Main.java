package com.bmi;

import com.bmi.ui.LoginFrame;
import com.bmi.util.DBUtil;
import com.bmi.util.UIUtil;

import javax.swing.*;

/**
 * BMI体质评估与预测系统 - 主入口
 *
 * 系统说明：
 * 1. 数据库: MySQL，请先执行 src/main/resources/sql/init.sql 初始化数据库
 * 2. 数据库配置: 修改 com.bmi.util.DBUtil 中的连接信息
 * 3. 默认管理员: admin / admin123
 * 4. 默认用户: user1 / 123456
 */
public class Main {
    public static void main(String[] args) {
        // ★ 在 AWT 初始化前开启全局文字抗锯齿 (解决 Swing 默认字体发虚问题)
        // awt.useSystemAAFontSettings=on 使用操作系统的字体平滑设置
        // swing.aatext=true 强制开启 Swing 文字抗锯齿
        System.setProperty("awt.useSystemAAFontSettings", "on");
        // 使用子像素抗锯齿 (LCD 屏更清晰)
        System.setProperty("awt.font.desktophints", "on");

        // 设置全局字体
        UIUtil.setGlobalFont();

        // 使用系统外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启动登录窗口
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);

            // 检测数据库连接，未连接时给出提示
            if (!DBUtil.testConnection()) {
                UIUtil.showError(loginFrame,
                    "数据库连接失败！\n\n请检查以下事项：\n" +
                    "1. MySQL服务是否已启动\n" +
                    "   → 右键'此电脑'→管理→服务→MySQL80→启动\n" +
                    "2. 数据库 bmi_health_db 是否已初始化\n" +
                    "   → 在MySQL中执行 init.sql\n" +
                    "3. DBUtil.java中的连接配置是否正确\n\n" +
                    "可点击登录界面的'测试数据库连接'按钮重新检测。");
            }
        });
    }
}

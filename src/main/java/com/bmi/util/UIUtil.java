package com.bmi.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * UI工具类 - 现代化UI样式和方法
 */
public class UIUtil {

    // ====== 现代化配色方案 ======
    // 主色：清新蓝绿
    public static final Color PRIMARY_COLOR = new Color(0, 121, 107);        // 蓝绿主色
    public static final Color PRIMARY_LIGHT = new Color(77, 182, 172);       // 浅蓝绿
    public static final Color PRIMARY_DARK = new Color(0, 94, 83);           // 深蓝绿
    public static final Color SECONDARY_COLOR = new Color(224, 247, 250);    // 极浅蓝绿背景
    public static final Color ACCENT_COLOR = new Color(255, 152, 0);         // 橙色强调

    // 背景色系
    public static final Color BG_COLOR = new Color(248, 250, 252);           // 主背景：极浅灰蓝
    public static final Color CARD_COLOR = Color.WHITE;                       // 卡片白
    // 侧边栏深浅交替 (实际取值与图一致)
    public static final Color SIDEBAR_COLOR = new Color(46, 62, 73);         // 侧边栏深色(主)
    public static final Color SIDEBAR_ALT_COLOR = new Color(54, 74, 88);     // 侧边栏浅一档(交替, +8,+12,+15)
    public static final Color SIDEBAR_HOVER = new Color(66, 86, 100);        // 侧边栏悬停
    public static final Color SIDEBAR_FOOTER = new Color(38, 50, 60);        // 侧边栏底部用户栏

    // 文字色系
    public static final Color TEXT_COLOR = new Color(33, 33, 33);            // 主文字
    public static final Color TEXT_SECONDARY = new Color(97, 97, 97);        // 次要文字(已加深)
    public static final Color TEXT_LIGHT = new Color(130, 130, 130);         // 浅文字(已加深)
    public static final Color TEXT_WHITE = Color.WHITE;

    // 侧边栏文字(对比度更高,清晰可读)
    public static final Color SIDEBAR_TEXT = new Color(220, 230, 240);       // 菜单文字(已加深)
    public static final Color SIDEBAR_TEXT_MUTED = new Color(170, 185, 198);  // 侧边栏次要文字
    public static final Color SIDEBAR_TEXT_ACTIVE = Color.WHITE;             // 选中文字

    // 功能色系
    public static final Color SUCCESS_COLOR = new Color(67, 160, 71);        // 绿
    public static final Color DANGER_COLOR = new Color(229, 57, 53);         // 红
    public static final Color WARNING_COLOR = new Color(251, 192, 45);       // 黄
    public static final Color INFO_COLOR = new Color(33, 150, 243);          // 蓝

    // 边框色
    public static final Color BORDER_COLOR = new Color(220, 224, 230);       // 浅灰边框
    public static final Color BORDER_FOCUS = new Color(77, 182, 172);        // 聚焦边框

    // 卡片标签颜色
    public static final Color TAG_GREEN_BG = new Color(232, 245, 233);
    public static final Color TAG_GREEN_TEXT = new Color(46, 125, 50);
    public static final Color TAG_BLUE_BG = new Color(227, 242, 253);
    public static final Color TAG_BLUE_TEXT = new Color(21, 101, 192);
    public static final Color TAG_ORANGE_BG = new Color(255, 243, 224);
    public static final Color TAG_ORANGE_TEXT = new Color(230, 81, 0);
    public static final Color TAG_RED_BG = new Color(255, 235, 238);
    public static final Color TAG_RED_TEXT = new Color(194, 24, 91);
    public static final Color TAG_GRAY_BG = new Color(240, 242, 245);
    public static final Color TAG_GRAY_TEXT = new Color(80, 90, 100);
    public static final Color TAG_TEAL_BG = new Color(224, 247, 250);
    public static final Color TAG_TEAL_TEXT = new Color(0, 121, 107);

    public static final Color CALORIE_BADGE_BG = new Color(45, 55, 65);
    public static final Color CALORIE_BADGE_TEXT = Color.WHITE;

    // BMI分级颜色
    public static final Color BMI_UNDERWEIGHT = new Color(33, 150, 243);
    public static final Color BMI_NORMAL = new Color(67, 160, 71);
    public static final Color BMI_OVERWEIGHT = new Color(251, 192, 45);
    public static final Color BMI_OBESE = new Color(229, 57, 53);

    // 食物图标分类色 (用于程序绘制简化图标)
    public static final Color FOOD_MEAT = new Color(229, 90, 78);     // 红色 - 肉类
    public static final Color FOOD_VEG = new Color(110, 188, 90);     // 绿色 - 蔬菜
    public static final Color FOOD_GRAIN = new Color(232, 168, 75);  // 橙色 - 谷物面包
    public static final Color FOOD_SEAFOOD = new Color(70, 145, 200); // 蓝色 - 海鲜
    public static final Color FOOD_BEAN = new Color(155, 110, 185);  // 紫色 - 豆类
    public static final Color FOOD_SOUP = new Color(195, 95, 130);   // 玫红 - 汤品
    public static final Color FOOD_DEFAULT = new Color(120, 130, 145); // 灰 - 默认

    // 圆角值
    public static final int RADIUS_SM = 6;
    public static final int RADIUS_MD = 10;
    public static final int RADIUS_LG = 16;

    /**
     * 创建圆角卡片面板（白色背景 + 柔和阴影 + 圆角）
     */
    public static JPanel createCardPanel(int radius, Color borderColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 柔和阴影
                g2.setColor(new Color(0, 0, 0, 8));
                g2.fillRoundRect(2, 3, getWidth() - 2, getHeight() - 2, radius, radius);
                // 主背景
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                g2.dispose();
            }
        };
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderColor, 1),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        return card;
    }

    /**
     * 创建无阴影圆角面板
     */
    public static JPanel createRoundedPanel(int radius, Color bg) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                g2.dispose();
            }
        };
        panel.setBackground(bg);
        panel.setOpaque(false);
        return panel;
    }

    /**
     * 创建标签徽章
     */
    public static JLabel createTagLabel(String text, Color bgColor, Color textColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_SM, RADIUS_SM);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        label.setForeground(textColor);
        label.setBackground(bgColor);
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return label;
    }

    /**
     * 创建卡路里徽章
     */
    public static JLabel createCalorieBadge(double calories) {
        JLabel label = new JLabel((int) calories + " kcal", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_SM, RADIUS_SM);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("微软雅黑", Font.BOLD, 12));
        label.setForeground(CALORIE_BADGE_TEXT);
        label.setBackground(CALORIE_BADGE_BG);
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        return label;
    }

    /**
     * 根据食谱分类返回标签颜色
     */
    public static Color[] getCategoryTagColors(String categoryName) {
        if (categoryName == null) return new Color[]{TAG_GRAY_BG, TAG_GRAY_TEXT};
        String c = categoryName.trim();
        if (c.contains("低脂") || c.contains("减脂") || c.contains("瘦身") || c.contains("糖尿病") || c.contains("素食")) {
            return new Color[]{TAG_GREEN_BG, TAG_GREEN_TEXT};
        } else if (c.contains("高蛋白") || c.contains("运动") || c.contains("增肌")) {
            return new Color[]{TAG_BLUE_BG, TAG_BLUE_TEXT};
        } else if (c.contains("养生") || c.contains("汤") || c.contains("粥")) {
            return new Color[]{TAG_ORANGE_BG, TAG_ORANGE_TEXT};
        } else if (c.contains("早餐")) {
            return new Color[]{TAG_TEAL_BG, TAG_TEAL_TEXT};
        } else {
            return new Color[]{TAG_GRAY_BG, TAG_GRAY_TEXT};
        }
    }

    /**
     * 生成用户头像面板（首字母 + 圆形背景）
     */
    public static JPanel createAvatarPanel(String name, Color bgColor, int size) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setPreferredSize(new Dimension(size, size));
        panel.setOpaque(false);

        String letter = "";
        if (name != null && !name.isEmpty()) {
            letter = name.substring(0, 1);
        }
        JLabel lbl = new JLabel(letter, SwingConstants.CENTER);
        lbl.setFont(new Font("微软雅黑", Font.BOLD, size / 2));
        lbl.setForeground(Color.WHITE);
        panel.add(lbl);
        return panel;
    }

    /**
     * 食物分类: 根据食谱名匹配图标类型与配色
     * 返回 [type, hasBowl]
     *  type: 0 肉/鸡  1 鱼/海鲜  2 蔬菜/沙拉  3 谷物/面包  4 面  5 蛋/奶  6 汤/粥  7 豆/藜麦  8 蔬果
     */
    public static int classifyFood(String name) {
        if (name == null) return 8;
        String n = name.toLowerCase();
        if (n.contains("三文鱼") || n.contains("鱼") || n.contains("虾") || n.contains("海鲜")) return 1;
        if (n.contains("蔬菜") || n.contains("沙拉") || n.contains("西兰花") || n.contains("芦笋")
                || n.contains("苦瓜") || n.contains("南瓜") || n.contains("冬瓜") || n.contains("生菜")
                || n.contains("黄瓜") || n.contains("胡萝卜") || n.contains("番茄")) return 2;
        if (n.contains("面包") || n.contains("吐司") || n.contains("燕麦") || n.contains("杂粮")
                || n.contains("荞麦") || n.contains("饼")) return 3;
        if (n.contains("面") || n.contains("凉皮") || n.contains("卷")) return 4;
        if (n.contains("蛋") || n.contains("奶") || n.contains("酸奶")) return 5;
        if (n.contains("汤") || n.contains("粥") || n.contains("银耳") || n.contains("莲子")) return 6;
        if (n.contains("豆腐") || n.contains("藜麦")) return 7;
        if (n.contains("牛") || n.contains("鸡") || n.contains("肉") || n.contains("排骨")) return 0;
        if (n.contains("果") || n.contains("蓝莓") || n.contains("香蕉") || n.contains("核桃")) return 8;
        return 8;
    }

    /**
     * 根据食物名返回配色
     */
    public static Color getFoodColor(String name) {
        int t = classifyFood(name);
        switch (t) {
            case 0: return FOOD_MEAT;
            case 1: return FOOD_SEAFOOD;
            case 2: return FOOD_VEG;
            case 3: return FOOD_GRAIN;
            case 4: return FOOD_GRAIN;
            case 5: return FOOD_GRAIN;
            case 6: return FOOD_SOUP;
            case 7: return FOOD_BEAN;
            default: return FOOD_DEFAULT;
        }
    }

    /**
     * 创建程序绘制的简化食物图标 (代替 emoji, 跨平台一致)
     * 60x60 像素, 透明背景, 圆角容器 + 简化几何
     */
    public static JComponent createFoodIcon(String name, int size) {
        final int type = classifyFood(name);
        final Color color = getFoodColor(name);
        JComponent icon = new JComponent() {
            {
                setPreferredSize(new Dimension(size, size));
                setMaximumSize(new Dimension(size, size));
                setMinimumSize(new Dimension(size, size));
                setOpaque(false);
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
                int w = getWidth(), h = getHeight();
                // 外层柔和背景圆 (按食物分类色, alpha 较低)
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 28));
                g2.fillOval(w / 10, h / 10, w - w / 5, h - h / 5);
                drawFoodPicture(g2, type, color, w, h);
                g2.dispose();
            }
        };
        return icon;
    }

    /**
     * 真正绘制"像食物"的图案 - 用曲线/路径组合, 不只是几何块
     */
    private static void drawFoodPicture(Graphics2D g2, int type, Color color, int w, int h) {
        int cx = w / 2, cy = h / 2;
        int unit = w / 16; // 单位刻度, 64px → 4
        if (unit < 2) unit = 2;

        switch (type) {
            case 0: { // 肉类 - 鸡腿 (上肉球 + 下棒骨 + 烤痕)
                // 肉球 (大椭圆, 略偏上)
                g2.setColor(color);
                g2.fillOval(w * 3 / 10, h / 5, w * 2 / 5, h * 2 / 5);
                // 肉球表面阴影 (深一档)
                g2.setColor(new Color(color.getRed() * 7 / 10, color.getGreen() * 7 / 10, color.getBlue() * 7 / 10));
                g2.fillOval(w * 3 / 10 + 2, h / 5 + h * 2 / 5 - 6, w * 2 / 5 - 4, 6);
                // 烤痕 (3条斜线)
                g2.setColor(new Color(80, 30, 25, 160));
                g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(w * 7 / 16, h / 4, w * 9 / 16, h / 4 + 5);
                g2.drawLine(w * 13 / 32, h * 3 / 10, w * 15 / 32, h * 3 / 10 + 5);
                g2.drawLine(w * 17 / 32, h * 3 / 10, w * 19 / 32, h * 3 / 10 + 5);
                // 棒骨 (从肉球下方伸出, 白色细椭圆)
                g2.setColor(new Color(245, 240, 225));
                java.awt.geom.AffineTransform old = g2.getTransform();
                g2.rotate(Math.toRadians(35), cx, cy + h / 8);
                g2.fillRoundRect(cx - 3, cy + h / 8, 6, h / 4, 3, 3);
                // 骨头末端球
                g2.fillOval(cx - 5, cy + h / 8 + h / 4 - 3, 10, 10);
                g2.setTransform(old);
                return;
            }
            case 1: { // 鱼 - 椭圆身 + 三角尾 + 鳞片弧 + 鳍 + 眼
                // 鱼身 (椭圆, 略扁)
                g2.setColor(color);
                g2.fillOval(w / 5, h / 4, w * 3 / 5, h * 2 / 5);
                // 鱼肚 (浅色腹部)
                g2.setColor(new Color(245, 250, 255));
                g2.fillOval(w / 5 + 4, h / 4 + h / 6, w * 3 / 5 - 8, h / 5);
                g2.setColor(color);
                // 鱼尾 (双叉)
                int[] tx = {w * 4 / 5, w * 9 / 10, w * 17 / 20, w * 9 / 10, w * 4 / 5};
                int[] ty = {cy, h / 4, cy, h * 3 / 4, cy};
                g2.fillPolygon(tx, ty, 5);
                // 背鳍 (上方三角)
                int[] fx = {w * 3 / 8, w / 2, w * 5 / 8};
                int[] fy = {h / 4, h / 8, h / 4};
                g2.fillPolygon(fx, fy, 3);
                // 鳞片 (3条弧线)
                g2.setColor(new Color(color.getRed() * 6 / 10, color.getGreen() * 6 / 10, color.getBlue() * 6 / 10));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawArc(w * 7 / 20, h * 3 / 8, 8, 8, 60, 120);
                g2.drawArc(w * 9 / 20, h * 3 / 8, 8, 8, 60, 120);
                g2.drawArc(w * 11 / 20, h * 3 / 8, 8, 8, 60, 120);
                // 眼睛 (白圈 + 黑瞳)
                g2.setColor(Color.WHITE);
                g2.fillOval(w * 9 / 32, cy - 3, 7, 7);
                g2.setColor(new Color(20, 20, 30));
                g2.fillOval(w * 9 / 32 + 2, cy - 1, 3, 3);
                // 嘴 (小三角)
                g2.setColor(new Color(60, 50, 50));
                int[] mx = {w / 5 - 2, w / 5 + 4, w / 5 - 2};
                int[] my = {cy - 2, cy, cy + 2};
                g2.fillPolygon(mx, my, 3);
                return;
            }
            case 2: { // 沙拉 - 碗 + 蓬松菜叶 + 番茄 + 玉米
                // 碗 (梯形, 浅灰)
                g2.setColor(new Color(190, 200, 215));
                int[] bx = {w / 4, w * 3 / 4, w * 5 / 8, w * 3 / 8};
                int[] by = {cy + 2, cy + 2, h * 4 / 5, h * 4 / 5};
                g2.fillPolygon(bx, by, 4);
                // 碗口椭圆 (深一点)
                g2.setColor(new Color(160, 170, 185));
                g2.fillOval(w / 4, cy, w / 2, 6);
                // 菜叶 (3-4 片波浪椭圆, 不同绿色)
                g2.setColor(FOOD_VEG);
                g2.fillOval(w * 3 / 8, h / 4, w / 4, h / 4);
                g2.setColor(new Color(FOOD_VEG.getRed() * 8 / 10, FOOD_VEG.getGreen() * 9 / 10, FOOD_VEG.getBlue() * 8 / 10));
                g2.fillOval(w / 4, h * 5 / 16, w / 4, h / 5);
                g2.setColor(new Color(150, 200, 110));
                g2.fillOval(w / 2, h * 5 / 16, w / 4, h / 5);
                // 叶子脉络 (一条曲线)
                g2.setColor(new Color(80, 130, 60));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(w * 7 / 16, h / 4 + 2, w * 9 / 16, h / 4 + h / 6);
                // 番茄 (2 个红圆)
                g2.setColor(FOOD_MEAT);
                g2.fillOval(w * 7 / 16, h * 5 / 16, 6, 6);
                g2.fillOval(w * 10 / 16, h * 6 / 16, 5, 5);
                // 玉米粒 (黄色小点)
                g2.setColor(FOOD_GRAIN);
                g2.fillOval(w * 9 / 16, h / 4 + 4, 3, 3);
                g2.fillOval(w * 11 / 16, h * 5 / 16, 3, 3);
                return;
            }
            case 3: { // 面包 - 法棍椭圆 + 3道裂纹 + 芝麻
                // 法棍 (椭圆, 偏黄棕)
                g2.setColor(color);
                g2.fillOval(w / 6, h * 5 / 16, w * 2 / 3, h * 3 / 8);
                // 表面高光 (上沿亮一档) - 用 lighten 工具方法自动 clamp 到 0-255
                g2.setColor(lighten(color, 0.12));
                g2.fillOval(w / 6 + 2, h * 5 / 16 + 1, w * 2 / 3 - 4, 5);
                // 法棍裂纹 (3 道斜线)
                g2.setColor(new Color(120, 75, 35, 200));
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(w * 3 / 8, h * 7 / 16, w * 7 / 16, h * 9 / 16);
                g2.drawLine(w / 2, h * 7 / 16, w * 9 / 16, h * 9 / 16);
                g2.drawLine(w * 5 / 8, h * 7 / 16, w * 11 / 16, h * 9 / 16);
                // 芝麻 (3 粒小白点)
                g2.setColor(new Color(245, 235, 200));
                g2.fillOval(w * 3 / 8 + 2, h * 7 / 16 - 2, 2, 2);
                g2.fillOval(w / 2 + 2, h * 7 / 16 - 2, 2, 2);
                g2.fillOval(w * 5 / 8 + 2, h * 7 / 16 - 2, 2, 2);
                return;
            }
            case 4: { // 面条 - 红碗 + 面条堆 (波浪曲线) + 筷子挑起的面条
                // 碗 (红色半圆 + 椭圆口)
                g2.setColor(new Color(200, 95, 110));
                g2.fillArc(w / 6, cy - 2, w * 2 / 3, h * 3 / 5, 0, 180);
                // 碗口椭圆 (深一档)
                g2.setColor(new Color(165, 70, 85));
                g2.fillOval(w / 6, cy - 2, w * 2 / 3, 8);
                // 碗内白色面条堆
                g2.setColor(new Color(250, 245, 220));
                g2.fillOval(w / 5, cy - 4, w * 3 / 5, h / 3);
                // 面条 (波浪曲线 4 条)
                g2.setColor(new Color(225, 200, 130));
                g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                java.awt.geom.Path2D p;
                for (int i = 0; i < 4; i++) {
                    p = new java.awt.geom.Path2D.Float();
                    int y0 = cy - 2 + i * 3;
                    p.moveTo(w / 4, y0);
                    p.curveTo(w * 5 / 16, y0 - 3, w * 6 / 16, y0 + 2, w * 7 / 16, y0);
                    p.curveTo(w * 8 / 16, y0 - 3, w * 9 / 16, y0 + 2, w * 10 / 16, y0);
                    p.curveTo(w * 11 / 16, y0 - 3, w * 12 / 16, y0 + 2, w * 3 / 4, y0);
                    g2.draw(p);
                }
                // 筷子 (两根棕色细棒, 从右上方斜插)
                g2.setColor(new Color(120, 80, 45));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(w * 11 / 16, h / 5, w * 15 / 16, h * 5 / 16);
                g2.drawLine(w * 11 / 16 + 3, h / 5, w * 15 / 16 + 3, h * 5 / 16);
                // 筷子挑起的一根面条 (从碗到筷子尖的弧线)
                g2.setColor(new Color(225, 200, 130));
                g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                p = new java.awt.geom.Path2D.Float();
                p.moveTo(w * 9 / 16, cy);
                p.curveTo(w * 11 / 16, h / 4, w * 13 / 16, h / 5, w * 11 / 16, h / 5);
                g2.draw(p);
                return;
            }
            case 5: { // 蛋 - 煎蛋 (不规则蛋白 + 中心橙黄蛋黄)
                // 蛋白 (不规则形状, 用 Path 画)
                g2.setColor(new Color(255, 250, 235));
                java.awt.geom.Path2D white = new java.awt.geom.Path2D.Float();
                white.moveTo(w * 5 / 16, h * 5 / 16);
                white.curveTo(w / 8, h / 4, w / 8, h * 5 / 8, w * 5 / 16, h * 11 / 16);
                white.curveTo(w * 3 / 8, h * 7 / 8, w * 5 / 8, h * 7 / 8, w * 11 / 16, h * 11 / 16);
                white.curveTo(w * 7 / 8, h * 5 / 8, w * 7 / 8, h / 4, w * 11 / 16, h * 5 / 16);
                white.curveTo(w * 5 / 8, h / 8, w * 3 / 8, h / 8, w * 5 / 16, h * 5 / 16);
                white.closePath();
                g2.fill(white);
                // 蛋白边缘 (淡淡的金黄边)
                g2.setColor(new Color(245, 220, 170));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(white);
                // 蛋黄 (中心橙黄圆)
                g2.setColor(new Color(255, 195, 60));
                g2.fillOval(w * 7 / 16, h * 7 / 16, w / 4, h / 4);
                // 蛋黄高光
                g2.setColor(new Color(255, 225, 130, 220));
                g2.fillOval(w * 15 / 32, h * 15 / 32, 5, 5);
                return;
            }
            case 6: { // 汤 - 碗 + 蒸汽 (3条波浪曲线) + 表面浮料
                // 碗 (玫红半圆)
                g2.setColor(new Color(190, 100, 120));
                g2.fillArc(w / 6, cy + 2, w * 2 / 3, h * 3 / 5, 0, 180);
                // 碗口椭圆 (深一档)
                g2.setColor(new Color(155, 75, 95));
                g2.fillOval(w / 6, cy + 2, w * 2 / 3, 8);
                // 汤面 (椭圆, 颜色稍浅)
                g2.setColor(color);
                g2.fillOval(w / 6 + 3, cy + 4, w * 2 / 3 - 6, 6);
                // 汤面浮料 (胡萝卜丁 + 葱花)
                g2.setColor(FOOD_GRAIN);
                g2.fillOval(w * 3 / 8, cy + 5, 4, 4);
                g2.fillOval(w * 5 / 8, cy + 5, 4, 4);
                g2.setColor(FOOD_VEG);
                g2.fillOval(w / 2 - 1, cy + 5, 3, 3);
                g2.fillOval(w * 9 / 16, cy + 7, 3, 3);
                // 蒸汽 (3 条上升波浪曲线, 用 Path)
                g2.setColor(new Color(180, 180, 200, 180));
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int[] xs = {w * 3 / 8, w / 2, w * 5 / 8};
                for (int x : xs) {
                    java.awt.geom.Path2D steam = new java.awt.geom.Path2D.Float();
                    steam.moveTo(x, cy);
                    steam.curveTo(x - 4, cy - h / 8, x + 4, cy - h / 5, x, cy - h / 4);
                    steam.curveTo(x - 4, cy - h * 3 / 10, x + 4, cy - h * 7 / 20, x, cy - h * 2 / 5);
                    g2.draw(steam);
                }
                return;
            }
            case 7: { // 豆腐 - 立方体 (前面 + 顶面透视) + 顶面格子纹
                // 顶面 (菱形透视, 浅黄)
                g2.setColor(new Color(245, 235, 195));
                int[] tx = {w / 4, w / 2, w * 3 / 4, w / 2};
                int[] ty = {h / 4, h * 5 / 32, h / 4, h * 3 / 16};
                // 修正: 顶面用平行四边形
                int[] topX = {w / 4, w * 3 / 4, w * 3 / 4 - w / 8, w / 4 - w / 8};
                int[] topY = {h / 4, h / 4, h / 4 - h / 10, h / 4 - h / 10};
                g2.fillPolygon(topX, topY, 4);
                // 前面 (矩形, 主体色)
                g2.setColor(color);
                g2.fillRoundRect(w / 4 - w / 8, h / 4, w / 2, h * 3 / 8, 4, 4);
                // 顶面格子纹 (2x2 切线)
                g2.setColor(new Color(200, 180, 130));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(w / 2 - w / 16, h / 4 - h / 10, w / 2 - w / 16, h / 4);
                g2.drawLine(w / 2 + w / 16, h / 4 - h / 10, w / 2 + w / 16, h / 4);
                g2.drawLine(w / 4 - w / 8, h / 4 - h / 20, w * 3 / 4 - w / 8, h / 4 - h / 20);
                // 前面格子纹 (2 道横竖)
                g2.setColor(new Color(color.getRed() * 7 / 10, color.getGreen() * 7 / 10, color.getBlue() * 7 / 10));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(w / 2 - w / 16, h / 4, w / 2 - w / 16, h / 4 + h * 3 / 8);
                g2.drawLine(w / 4 - w / 8, h / 4 + h * 3 / 16, w * 3 / 4 - w / 8, h / 4 + h * 3 / 16);
                return;
            }
            default: { // 蔬果 - 苹果 (圆 + 茎 + 叶 + 高光)
                // 苹果主体 (略扁圆)
                g2.setColor(color);
                g2.fillOval(w / 4, h / 4, w / 2, h / 2);
                // 顶部凹陷 (用背景色画一道弧)
                g2.setColor(new Color(245, 245, 250));
                g2.fillArc(w * 7 / 16, h / 4 - 2, w / 8, 6, 0, 180);
                g2.setColor(color);
                // 茎 (棕色细长)
                g2.setColor(new Color(95, 65, 35));
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawLine(w / 2, h / 4 - 1, w / 2 + 2, h / 8);
                // 叶子 (绿色椭圆, 略斜)
                java.awt.geom.AffineTransform old = g2.getTransform();
                g2.rotate(Math.toRadians(-30), w / 2, h / 8);
                g2.setColor(FOOD_VEG);
                g2.fillOval(w / 2, h / 8 - 2, w / 5, 5);
                g2.setTransform(old);
                // 高光 (左上小白椭圆)
                g2.setColor(new Color(255, 255, 255, 140));
                g2.fillOval(w * 5 / 16, h * 5 / 16, w / 8, h / 8);
            }
        }
    }

    /**
     * 设置全局UI字体
     */
    public static void setGlobalFont() {
        Font font = new Font("微软雅黑", Font.PLAIN, 14);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("PasswordField.font", font);
        UIManager.put("TextArea.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("Table.font", new Font("微软雅黑", Font.PLAIN, 13));
        UIManager.put("TableHeader.font", new Font("微软雅黑", Font.BOLD, 13));
        UIManager.put("OptionPane.font", font);
        UIManager.put("Menu.font", font);
        UIManager.put("MenuItem.font", font);
        UIManager.put("TabbedPane.font", font);
        // 全局背景色
        UIManager.put("Panel.background", BG_COLOR);
        UIManager.put("OptionPane.background", BG_COLOR);
    }

    // ====== 现代化按钮 ======

    /**
     * 创建现代化圆角按钮（带悬停效果）
     */
    public static JButton createButton(String text, Color bgColor) {
        final Color normalColor = bgColor;
        final Color hoverColor = darken(bgColor, 0.12);
        final Color pressColor = darken(bgColor, 0.20);

        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? pressColor : (getModel().isRollover() ? hoverColor : normalColor);
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_MD, RADIUS_MD);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 38));
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btn.setOpaque(false);
        return btn;
    }

    /**
     * 创建主要按钮
     */
    public static JButton createPrimaryButton(String text) {
        return createButton(text, PRIMARY_COLOR);
    }

    /**
     * 创建危险按钮
     */
    public static JButton createDangerButton(String text) {
        return createButton(text, DANGER_COLOR);
    }

    /**
     * 创建成功按钮
     */
    public static JButton createSuccessButton(String text) {
        return createButton(text, SUCCESS_COLOR);
    }

    /**
     * 创建描边按钮（白底+彩色边框）
     */
    public static JButton createOutlineButton(String text, Color outlineColor) {
        final Color hoverBg = new Color(outlineColor.getRed(), outlineColor.getGreen(), outlineColor.getBlue(), 15);
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // 背景
                if (getModel().isRollover()) {
                    g2.setColor(hoverBg);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_MD, RADIUS_MD);
                }
                // 边框
                g2.setColor(outlineColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_MD, RADIUS_MD);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setForeground(outlineColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 38));
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btn.setOpaque(false);
        return btn;
    }

    /**
     * 创建标题标签
     */
    public static JLabel createTitleLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, size));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * 创建普通标签
     */
    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    /**
     * 创建输入框（圆角 + 聚焦高亮）
     */
    public static JTextField createTextField(int columns) {
        final Color normalBorder = new Color(220, 220, 220);
        final Color focusBorder = PRIMARY_LIGHT;
        final JTextField field = new JTextField(columns) {
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? focusBorder : normalBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_SM, RADIUS_SM);
                g2.dispose();
            }
        };
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setOpaque(false);
        return field;
    }

    /**
     * 创建密码框（圆角 + 聚焦高亮）
     */
    public static JPasswordField createPasswordField(int columns) {
        final Color normalBorder = new Color(220, 220, 220);
        final Color focusBorder = PRIMARY_LIGHT;
        final JPasswordField field = new JPasswordField(columns) {
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? focusBorder : normalBorder);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_SM, RADIUS_SM);
                g2.dispose();
            }
        };
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setOpaque(false);
        return field;
    }

    /**
     * 创建文本区域
     */
    public static JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        return area;
    }

    /**
     * 设置窗口居中
     */
    public static void centerWindow(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }

    /**
     * 显示提示消息
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "提示", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 显示错误消息
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 显示确认对话框
     */
    public static boolean showConfirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "确认",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * 计算BMI值
     */
    public static double calculateBMI(double height, double weight) {
        if (height <= 0 || weight <= 0) return 0;
        double heightM = height / 100.0;
        return Math.round(weight / (heightM * heightM) * 100.0) / 100.0;
    }

    /**
     * 获取BMI评估结果
     */
    public static String getBMIAssessment(double bmi) {
        if (bmi < 18.5) return "偏瘦";
        else if (bmi < 24.0) return "正常";
        else if (bmi < 28.0) return "偏胖";
        else return "肥胖";
    }

    /**
     * 获取BMI对应颜色
     */
    public static Color getBMIColor(double bmi) {
        if (bmi < 18.5) return BMI_UNDERWEIGHT;
        else if (bmi < 24.0) return BMI_NORMAL;
        else if (bmi < 28.0) return BMI_OVERWEIGHT;
        else return BMI_OBESE;
    }

    /**
     * 获取BMI膳食建议
     */
    public static String getBMIDietRecommendation(double bmi) {
        if (bmi < 18.5) {
            return "您的体重偏轻，建议：\n" +
                "1. 增加每日热量摄入，建议比正常多摄入300-500千卡\n" +
                "2. 多食用高蛋白食物如鸡蛋、牛奶、牛肉\n" +
                "3. 增加碳水化合物摄入，如杂粮米饭、全麦面包\n" +
                "4. 少食多餐，每天5-6餐\n" +
                "5. 适当进行力量训练，增加肌肉量";
        } else if (bmi < 24.0) {
            return "您的体重正常，建议：\n" +
                "1. 保持均衡饮食，每日摄入谷薯类、蔬果类、畜禽鱼蛋奶类\n" +
                "2. 每日饮水量保持在1500-1700ml\n" +
                "3. 坚持每周至少150分钟中等强度运动\n" +
                "4. 注意控制油盐摄入，每日盐不超过6g\n" +
                "5. 保持规律作息，避免暴饮暴食";
        } else if (bmi < 28.0) {
            return "您的体重偏胖，建议：\n" +
                "1. 适当减少热量摄入，每日减少300-500千卡\n" +
                "2. 选择低脂食物，如鸡胸肉、蔬菜沙拉\n" +
                "3. 减少高糖、高油食物的摄入\n" +
                "4. 增加有氧运动，如慢跑、游泳、骑车\n" +
                "5. 多食用富含膳食纤维的食物，增加饱腹感";
        } else {
            return "您的体重肥胖，建议：\n" +
                "1. 严格控制每日热量摄入在1500-1800千卡\n" +
                "2. 以低脂、低糖、高纤维饮食为主\n" +
                "3. 每日至少30分钟中等强度运动\n" +
                "4. 避免高热量零食和含糖饮料\n" +
                "5. 建议咨询专业营养师制定个性化减重方案";
        }
    }

    /**
     * 获取适合BMI范围的食谱分类
     */
    public static String getSuitableCategory(double bmi) {
        if (bmi < 18.5) return "高蛋白食谱";
        else if (bmi < 24.0) return "均衡膳食";
        else return "低脂食谱";
    }

    // ====== 工具方法 ======

    /**
     * 将颜色变暗
     */
    public static Color darken(Color color, double factor) {
        int r = (int) (color.getRed() * (1 - factor));
        int g = (int) (color.getGreen() * (1 - factor));
        int b = (int) (color.getBlue() * (1 - factor));
        return new Color(Math.max(0, r), Math.max(0, g), Math.max(0, b));
    }

    /**
     * 将颜色变亮
     */
    public static Color lighten(Color color, double factor) {
        int r = (int) (color.getRed() + (255 - color.getRed()) * factor);
        int g = (int) (color.getGreen() + (255 - color.getGreen()) * factor);
        int b = (int) (color.getBlue() + (255 - color.getBlue()) * factor);
        return new Color(Math.min(255, r), Math.min(255, g), Math.min(255, b));
    }

    // ====== 用户端新风格配色 (2026-07 新版) ======
    public static final Color USER_PRIMARY = new Color(67, 160, 71);          // 绿色主色
    public static final Color USER_PRIMARY_DARK = new Color(46, 125, 50);      // 深绿
    public static final Color USER_PRIMARY_LIGHT = new Color(232, 245, 233);   // 浅绿背景
    public static final Color USER_BG = new Color(245, 247, 250);              // 页面背景
    public static final Color USER_CARD_BG = Color.WHITE;
    public static final Color USER_SIDEBAR_BG = Color.WHITE;
    public static final Color USER_SIDEBAR_TEXT = new Color(73, 80, 87);
    public static final Color USER_SIDEBAR_TEXT_MUTED = new Color(108, 117, 125);
    public static final Color USER_SIDEBAR_SELECTED = new Color(232, 245, 233);
    public static final Color USER_SIDEBAR_HOVER = new Color(240, 248, 240);
    public static final Color USER_TEXT = new Color(33, 37, 41);
    public static final Color USER_TEXT_SECONDARY = new Color(108, 117, 125);
    public static final Color USER_TEXT_LIGHT = new Color(173, 181, 189);
    public static final Color USER_BORDER = new Color(222, 226, 230);

    /**
     * 创建用户端白色圆角卡片（柔和阴影）
     */
    public static JPanel createUserCardPanel(int radius) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, radius, radius);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
                g2.dispose();
            }
        };
        card.setBackground(USER_CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        return card;
    }

    /**
     * 创建用户端主按钮（绿色圆角）
     */
    public static JButton createUserPrimaryButton(String text) {
        return createUserButton(text, USER_PRIMARY);
    }

    private static JButton createUserButton(String text, Color bg) {
        final Color normal = bg;
        final Color hover = darken(bg, 0.08);
        final Color press = darken(bg, 0.16);
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed() ? press : (getModel().isRollover() ? hover : normal);
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_MD, RADIUS_MD);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btn.setOpaque(false);
        btn.setPreferredSize(new Dimension(0, 42));
        return btn;
    }

    /**
     * 创建用户端描边按钮
     */
    public static JButton createUserOutlineButton(String text, Color borderColor) {
        final Color hover = new Color(borderColor.getRed(), borderColor.getGreen(), borderColor.getBlue(), 20);
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover()) {
                    g2.setColor(hover);
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_MD, RADIUS_MD);
                }
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_MD, RADIUS_MD);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setForeground(borderColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btn.setOpaque(false);
        return btn;
    }

    /**
     * 创建用户端输入框
     */
    public static JTextField createUserTextField(int columns) {
        final Color border = USER_BORDER;
        final Color focus = USER_PRIMARY;
        final JTextField field = new JTextField(columns) {
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? focus : border);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_SM, RADIUS_SM);
                g2.dispose();
            }
        };
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        field.setOpaque(false);
        return field;
    }

    /**
     * 创建用户端密码框
     */
    public static JPasswordField createUserPasswordField(int columns) {
        final Color border = USER_BORDER;
        final Color focus = USER_PRIMARY;
        final JPasswordField field = new JPasswordField(columns) {
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hasFocus() ? focus : border);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_SM, RADIUS_SM);
                g2.dispose();
            }
        };
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        field.setOpaque(false);
        return field;
    }

    /**
     * 创建BMI参考色条（偏瘦/正常/偏胖/肥胖），会根据当前BMI高亮对应区间
     */
    public static JPanel createBMIScaleBar(double bmi) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        int activeIndex = -1;
        if (bmi > 0) {
            if (bmi < 18.5) activeIndex = 0;
            else if (bmi < 24.0) activeIndex = 1;
            else if (bmi < 28.0) activeIndex = 2;
            else activeIndex = 3;
        }

        JPanel segments = new JPanel(new GridLayout(1, 4, 3, 0));
        segments.setOpaque(false);
        Color[] colors = {BMI_UNDERWEIGHT, BMI_NORMAL, BMI_OVERWEIGHT, BMI_OBESE};
        for (int i = 0; i < colors.length; i++) {
            JPanel seg = new JPanel();
            seg.setBackground(colors[i]);
            seg.setPreferredSize(new Dimension(0, 8));
            seg.setOpaque(true);
            if (i == activeIndex) {
                seg.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
            segments.add(seg);
        }

        JPanel labels = new JPanel(new GridLayout(1, 4));
        labels.setOpaque(false);
        String[] texts = {"偏瘦", "正常", "偏胖", "肥胖"};
        for (int i = 0; i < texts.length; i++) {
            JLabel lbl = new JLabel(texts[i], SwingConstants.CENTER);
            lbl.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            lbl.setForeground(i == activeIndex ? colors[i] : USER_TEXT_SECONDARY);
            labels.add(lbl);
        }

        panel.add(segments, BorderLayout.CENTER);
        panel.add(labels, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * 创建BMI分级纵向色块参考表（截图2样式：BMI范围 → 分类 (说明)）
     */
    public static JPanel createBMIScaleBlocks(double bmi) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        String[][] bmiRanges = {
            {"BMI < 18.5", "偏瘦", "体重不足"},
            {"18.5 ≤ BMI < 24", "正常", "体重正常"},
            {"24 ≤ BMI < 28", "偏胖", "超重"},
            {"BMI ≥ 28", "肥胖", "需要减重"}
        };
        Color[] bmiColors = {BMI_UNDERWEIGHT, BMI_NORMAL, BMI_OVERWEIGHT, BMI_OBESE};

        int activeIndex = -1;
        if (bmi > 0) {
            if (bmi < 18.5) activeIndex = 0;
            else if (bmi < 24.0) activeIndex = 1;
            else if (bmi < 28.0) activeIndex = 2;
            else activeIndex = 3;
        }

        for (int i = 0; i < 4; i++) {
            JPanel row = new JPanel(new BorderLayout());
            row.setBackground(bmiColors[i]);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            row.setPreferredSize(new Dimension(0, 32));
            row.setOpaque(true);

            String text = bmiRanges[i][0] + "  →  " + bmiRanges[i][1] + " (" + bmiRanges[i][2] + ")";
            JLabel label = new JLabel("  " + text);
            label.setFont(new Font("微软雅黑", activeIndex == i ? Font.BOLD : Font.PLAIN, 13));
            label.setForeground(Color.WHITE);
            row.add(label, BorderLayout.CENTER);

            // 高亮当前区间：右侧显示小标识
            if (activeIndex == i) {
                JLabel current = new JLabel("●  ", SwingConstants.RIGHT);
                current.setFont(new Font("微软雅黑", Font.BOLD, 12));
                current.setForeground(Color.WHITE);
                row.add(current, BorderLayout.EAST);
            }

            panel.add(row);
            if (i < 3) panel.add(Box.createVerticalStrut(1));
        }
        return panel;
    }

    /**
     * 创建健康评分圆环
     */
    public static JPanel createHealthScorePanel(int score, double bmi) {
        // 颜色与BMI分类同步：偏瘦蓝/正常绿/偏胖黄/肥胖红，无BMI时用默认主色
        Color arcColor;
        if (bmi <= 0) {
            arcColor = USER_PRIMARY;
        } else {
            arcColor = getBMIColor(bmi);
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int size = Math.min(w, h) - 14;
                int x = (w - size) / 2, y = (h - size) / 2;

                g2.setColor(new Color(233, 236, 239));
                g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(x, y, size, size, 0, 360);

                g2.setColor(arcColor);
                int sweep = Math.min(360, (int) (360 * score / 100.0));
                g2.drawArc(x, y, size, size, 90, -sweep);
                g2.dispose();
            }
        };
        panel.setLayout(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(100, 100));

        JLabel lblScore = new JLabel(String.valueOf(score), SwingConstants.CENTER);
        lblScore.setFont(new Font("微软雅黑", Font.BOLD, 26));
        lblScore.setForeground(arcColor);
        JLabel lblText = new JLabel("健康", SwingConstants.CENTER);
        lblText.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        lblText.setForeground(USER_TEXT_SECONDARY);

        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setOpaque(false);
        lblScore.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblText.setAlignmentX(Component.CENTER_ALIGNMENT);
        inner.add(lblScore);
        inner.add(lblText);

        panel.add(inner);
        return panel;
    }

    /**
     * 创建绿色Logo图标（心形）
     */
    public static JComponent createUserLogoIcon(int size) {
        JComponent icon = new JComponent() {
            {
                setPreferredSize(new Dimension(size, size));
                setOpaque(false);
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                int cx = w / 2;
                int r = w / 6;
                int cy0 = h / 4;

                g2.setColor(USER_PRIMARY);
                g2.fillOval(cx - 2 * r, cy0, 2 * r, 2 * r);
                g2.fillOval(cx, cy0, 2 * r, 2 * r);
                int[] px = {cx - 2 * r, cx + 2 * r, cx, cx - 2 * r};
                int[] py = {cy0 + r, cy0 + r, cy0 + 3 * r + r / 2, cy0 + r};
                g2.fillPolygon(px, py, 4);
                g2.dispose();
            }
        };
        return icon;
    }

    /**
     * 创建用户端状态徽章（如“正常”）
     */
    public static JLabel createUserStatusBadge(String text, Color bgColor, Color textColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, RADIUS_SM, RADIUS_SM);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        label.setForeground(textColor);
        label.setBackground(bgColor);
        label.setOpaque(false);
        label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        return label;
    }
}

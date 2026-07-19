package com.bmi.ui.user;

import com.bmi.model.DietRecord;
import com.bmi.model.User;
import com.bmi.service.DietIntakeService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 用户端 - 膳食摄入分析面板
 *
 * 功能：
 *   1. 录入每日膳食搭配（碳水 / 脂肪 / 蛋白质 / 热量 / 膳食纤维 / 胆固醇 / 饮水量）
 *   2. 历史记录表格
 *   3. 摄入量分析图表（柱状图 + 折线图叠加）
 */
public class DietIntakePanel extends JPanel {
    private final User currentUser;
    private final DietIntakeService service = new DietIntakeService();

    // 表单
    private JTextField txtDate;
    private JTextField txtCarbs;
    private JTextField txtFat;
    private JTextField txtProtein;
    private JTextField txtCalories;
    private JTextField txtFiber;
    private JTextField txtCholesterol;
    private JTextField txtWater;
    private JTextField txtNote;

    // 列表
    private JTable table;
    private DefaultTableModel tableModel;

    // 状态提示 (今日已有记录时, 提示用户将覆盖)
    private JLabel lblTodayHint;

    // 图表
    private final IntakeChartPanel chartPanel = new IntakeChartPanel();
    private JLabel lblAvgSummary; // 平均摄入量汇总

    public DietIntakePanel(User user) {
        this.currentUser = user;
        initUI();
        loadAll();
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
        JLabel titleLabel = new JLabel("膳食摄入分析");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(UIUtil.USER_TEXT);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JLabel sub = new JLabel("记录每日膳食搭配，分析营养摄入结构");
        sub.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        sub.setForeground(UIUtil.USER_TEXT_SECONDARY);
        titlePanel.add(sub, BorderLayout.EAST);

        add(titlePanel, BorderLayout.NORTH);

        // 主体：上下结构
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIUtil.USER_BG);
        body.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        // 1. 录入卡片
        body.add(createEntryCard());
        body.add(Box.createVerticalStrut(16));

        // 2. 分析图表卡片
        body.add(createChartCard());
        body.add(Box.createVerticalStrut(16));

        // 3. 历史记录表格卡片
        body.add(createHistoryCard());

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.setBackground(UIUtil.USER_BG);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    // ====== 录入卡片 ======
    private JPanel createEntryCard() {
        JPanel card = UIUtil.createUserCardPanel(12);
        card.setLayout(new BorderLayout(0, 12));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        JLabel h = new JLabel("每日膳食录入");
        h.setFont(new Font("微软雅黑", Font.BOLD, 16));
        h.setForeground(UIUtil.USER_TEXT);
        headerRow.add(h, BorderLayout.WEST);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBtns.setOpaque(false);
        lblTodayHint = new JLabel(" ");
        lblTodayHint.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        lblTodayHint.setForeground(UIUtil.WARNING_COLOR);

        JButton btnHelp = UIUtil.createUserOutlineButton("查看推荐摄入", UIUtil.USER_PRIMARY);
        btnHelp.setPreferredSize(new Dimension(120, 32));
        btnHelp.addActionListener(e -> showRecommendationDialog());
        rightBtns.add(lblTodayHint);
        rightBtns.add(btnHelp);

        headerRow.add(rightBtns, BorderLayout.EAST);
        card.add(headerRow, BorderLayout.NORTH);

        // 表单行1: 日期 + 备注
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        row1.setOpaque(false);
        row1.add(buildLabel("日期"));
        txtDate = UIUtil.createUserTextField(10);
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        row1.add(txtDate);
        row1.add(buildLabel("备注"));
        txtNote = UIUtil.createUserTextField(20);
        row1.add(txtNote);

        // 表单行2: 营养字段
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        row2.setOpaque(false);

        row2.add(buildLabel("碳水化合物(g)"));
        txtCarbs = UIUtil.createUserTextField(6);
        row2.add(txtCarbs);

        row2.add(buildLabel("脂肪(g)"));
        txtFat = UIUtil.createUserTextField(6);
        row2.add(txtFat);

        row2.add(buildLabel("蛋白质(g)"));
        txtProtein = UIUtil.createUserTextField(6);
        row2.add(txtProtein);

        row2.add(buildLabel("热量(大卡)"));
        txtCalories = UIUtil.createUserTextField(8);
        row2.add(txtCalories);

        // 表单行3: 其余
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        row3.setOpaque(false);
        row3.add(buildLabel("膳食纤维(g)"));
        txtFiber = UIUtil.createUserTextField(6);
        row3.add(txtFiber);

        row3.add(buildLabel("胆固醇(mmol)"));
        txtCholesterol = UIUtil.createUserTextField(6);
        row3.add(txtCholesterol);

        row3.add(buildLabel("饮水量(ml)"));
        txtWater = UIUtil.createUserTextField(8);
        row3.add(txtWater);

        JPanel formWrap = new JPanel();
        formWrap.setLayout(new BoxLayout(formWrap, BoxLayout.Y_AXIS));
        formWrap.setOpaque(false);
        formWrap.add(row1);
        formWrap.add(row2);
        formWrap.add(row3);

        // 按钮行
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        btnRow.setOpaque(false);
        JButton btnSave = UIUtil.createUserPrimaryButton("保存今日记录");
        btnSave.setPreferredSize(new Dimension(130, 36));
        btnSave.addActionListener(e -> onSave());
        btnRow.add(btnSave);

        JButton btnReset = UIUtil.createUserOutlineButton("清空", UIUtil.USER_TEXT_SECONDARY);
        btnReset.setPreferredSize(new Dimension(80, 36));
        btnReset.addActionListener(e -> resetForm());
        btnRow.add(btnReset);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(formWrap, BorderLayout.CENTER);
        center.add(btnRow, BorderLayout.SOUTH);

        card.add(center, BorderLayout.CENTER);
        return card;
    }

    private JLabel buildLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        l.setForeground(UIUtil.USER_TEXT);
        return l;
    }

    // ====== 图表卡片 ======
    private JPanel createChartCard() {
        JPanel card = UIUtil.createUserCardPanel(12);
        card.setLayout(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        JLabel h = new JLabel("摄入量分析（最近7天）");
        h.setFont(new Font("微软雅黑", Font.BOLD, 16));
        h.setForeground(UIUtil.USER_TEXT);
        headerRow.add(h, BorderLayout.WEST);

        lblAvgSummary = new JLabel(" ");
        lblAvgSummary.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        lblAvgSummary.setForeground(UIUtil.USER_TEXT_SECONDARY);
        headerRow.add(lblAvgSummary, BorderLayout.EAST);

        card.add(headerRow, BorderLayout.NORTH);

        chartPanel.setPreferredSize(new Dimension(0, 360));
        chartPanel.setBackground(Color.WHITE);
        card.add(chartPanel, BorderLayout.CENTER);

        return card;
    }

    // ====== 历史记录表格卡片 ======
    private JPanel createHistoryCard() {
        JPanel card = UIUtil.createUserCardPanel(12);
        card.setLayout(new BorderLayout(0, 10));
        card.setBackground(Color.WHITE);
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerRow = new JPanel(new BorderLayout());
        headerRow.setOpaque(false);
        JLabel h = new JLabel("历史录入记录");
        h.setFont(new Font("微软雅黑", Font.BOLD, 16));
        h.setForeground(UIUtil.USER_TEXT);
        headerRow.add(h, BorderLayout.WEST);

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBtns.setOpaque(false);
        JButton btnViewAll = UIUtil.createUserOutlineButton("查看全部", UIUtil.USER_PRIMARY);
        btnViewAll.setPreferredSize(new Dimension(90, 32));
        btnViewAll.addActionListener(e -> showHistoryDialog());
        rightBtns.add(btnViewAll);

        JButton btnDelete = UIUtil.createUserOutlineButton("删除记录", UIUtil.DANGER_COLOR);
        btnDelete.setPreferredSize(new Dimension(90, 32));
        btnDelete.addActionListener(e -> onDelete());
        rightBtns.add(btnDelete);
        headerRow.add(rightBtns, BorderLayout.EAST);
        card.add(headerRow, BorderLayout.NORTH);

        String[] columns = {"日期", "碳水(g)", "脂肪(g)", "蛋白质(g)", "热量(大卡)",
                "膳食纤维(g)", "胆固醇(mmol)", "饮水量(ml)", "备注"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        table.getTableHeader().setBackground(UIUtil.USER_PRIMARY_LIGHT);
        table.getTableHeader().setForeground(UIUtil.USER_TEXT);
        table.setSelectionBackground(UIUtil.USER_PRIMARY_LIGHT);
        table.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        // 调整列宽
        if (table.getColumnCount() >= 9) {
            table.getColumnModel().getColumn(0).setPreferredWidth(95);   // 日期
            table.getColumnModel().getColumn(1).setPreferredWidth(70);   // 碳水
            table.getColumnModel().getColumn(2).setPreferredWidth(65);   // 脂肪
            table.getColumnModel().getColumn(3).setPreferredWidth(75);   // 蛋白质
            table.getColumnModel().getColumn(4).setPreferredWidth(90);   // 热量
            table.getColumnModel().getColumn(5).setPreferredWidth(90);   // 膳食纤维
            table.getColumnModel().getColumn(6).setPreferredWidth(95);   // 胆固醇
            table.getColumnModel().getColumn(7).setPreferredWidth(85);   // 饮水量
            table.getColumnModel().getColumn(8).setPreferredWidth(150);  // 备注
        }

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(0, 220));
        sp.setBorder(BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1));
        card.add(sp, BorderLayout.CENTER);

        return card;
    }

    // ====== 数据加载与操作 ======
    private void loadAll() {
        List<DietRecord> records = service.findByUserId(currentUser.getId());
        refreshTable(records);
        refreshChart(records);
        refreshSummary(records);
        loadTodayIfExists();
    }

    /**
     * 如果今天已有记录, 自动填入表单 + 显示"再次保存将覆盖"提示
     */
    private void loadTodayIfExists() {
        try {
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(today);
            DietRecord exist = service.findByDate(currentUser.getId(), date);
            if (exist != null) {
                txtDate.setText(exist.getRecordDateStr());
                txtCarbs.setText(fmt(exist.getCarbs()));
                txtFat.setText(fmt(exist.getFat()));
                txtProtein.setText(fmt(exist.getProtein()));
                txtCalories.setText(fmt(exist.getCalories()));
                txtFiber.setText(fmt(exist.getFiber()));
                txtCholesterol.setText(fmt(exist.getCholesterol()));
                txtWater.setText(fmt(exist.getWater()));
                txtNote.setText(exist.getNote() == null ? "" : exist.getNote());
                lblTodayHint.setText("⚠ 今日已有记录，再次保存将覆盖");
            } else {
                lblTodayHint.setText(" ");
            }
        } catch (ParseException ignore) { }
    }

    private void refreshTable(List<DietRecord> records) {
        tableModel.setRowCount(0);
        for (DietRecord r : records) {
            tableModel.addRow(new Object[]{
                r.getRecordDateStr(),
                fmt(r.getCarbs()),
                fmt(r.getFat()),
                fmt(r.getProtein()),
                fmt(r.getCalories()),
                fmt(r.getFiber()),
                fmt(r.getCholesterol()),
                fmt(r.getWater()),
                r.getNote() == null ? "" : r.getNote()
            });
        }
    }

    private void refreshChart(List<DietRecord> allRecords) {
        // 取最近7天 (按日期升序)
        List<DietRecord> recent = service.findRecent(currentUser.getId(), 7);
        chartPanel.setData(recent);
        chartPanel.repaint();
    }

    private void refreshSummary(List<DietRecord> records) {
        if (records == null || records.isEmpty()) {
            lblAvgSummary.setText("尚无记录");
            return;
        }
        // 最近7天平均
        List<DietRecord> recent = records.size() > 7
                ? new ArrayList<>(records.subList(0, 7))
                : new ArrayList<>(records);
        double[] avg = service.calcAverages(recent);
        lblAvgSummary.setText(String.format(
            "近 %d 天平均：碳水 %.0fg · 脂肪 %.0fg · 蛋白质 %.0fg · 热量 %.0f 大卡 · 膳食纤维 %.0fg · 胆固醇 %.1fmmol · 饮水 %.0fml",
            recent.size(), avg[0], avg[1], avg[2], avg[3], avg[4], avg[5], avg[6]
        ));
    }

    private void onSave() {
        // 校验
        java.util.Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(txtDate.getText().trim());
        } catch (ParseException ex) {
            UIUtil.showError(this, "日期格式错误，请使用 yyyy-MM-dd 格式");
            return;
        }
        DietRecord r = new DietRecord();
        r.setUserId(currentUser.getId());
        r.setRecordDate(date);
        try {
            r.setCarbs(parseDouble(txtCarbs.getText(), 0));
            r.setFat(parseDouble(txtFat.getText(), 0));
            r.setProtein(parseDouble(txtProtein.getText(), 0));
            r.setCalories(parseDouble(txtCalories.getText(), 0));
            r.setFiber(parseDouble(txtFiber.getText(), 0));
            r.setCholesterol(parseDouble(txtCholesterol.getText(), 0));
            r.setWater(parseDouble(txtWater.getText(), 0));
        } catch (NumberFormatException ex) {
            UIUtil.showError(this, "请输入合法的数字");
            return;
        }
        r.setNote(txtNote.getText().trim());

        Object[] result = service.saveRecord(r);
        if (Boolean.TRUE.equals(result[0])) {
            UIUtil.showInfo(this, Boolean.TRUE.equals(result[1]) ? "今日记录已更新！" : "保存成功！");
            loadAll();
        } else {
            String err = result.length > 2 && result[2] != null ? result[2].toString() : "数据库操作未成功";
            UIUtil.showError(this, "保存失败：" + err);
        }
    }

    private double parseDouble(String s, double defVal) {
        if (s == null || s.trim().isEmpty()) return defVal;
        return Double.parseDouble(s.trim());
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UIUtil.showError(this, "请先选择一条要删除的记录");
            return;
        }
        String dateStr = (String) tableModel.getValueAt(row, 0);
        if (!UIUtil.showConfirm(this, "确定要删除 " + dateStr + " 的记录吗？")) return;

        // 通过日期查找 ID
        List<DietRecord> list = service.findByUserId(currentUser.getId());
        int targetId = -1;
        for (DietRecord d : list) {
            if (dateStr.equals(d.getRecordDateStr())) { targetId = d.getId(); break; }
        }
        if (targetId > 0 && service.deleteRecord(targetId)) {
            UIUtil.showInfo(this, "已删除");
            loadAll();
        } else {
            UIUtil.showError(this, "删除失败");
        }
    }

    private void resetForm() {
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        txtCarbs.setText(""); txtFat.setText(""); txtProtein.setText("");
        txtCalories.setText(""); txtFiber.setText("");
        txtCholesterol.setText(""); txtWater.setText(""); txtNote.setText("");
    }

    private void showRecommendationDialog() {
        String msg = "<html><body style='width:380px;padding:6px'>" +
                "<h3 style='margin:4px 0;color:#2e7d32'>成人每日推荐摄入参考</h3>" +
                "<table style='font-size:13px;line-height:1.6'>" +
                "<tr><td>· 碳水化合物</td><td>250 - 400 g</td></tr>" +
                "<tr><td>· 脂肪</td><td>50 - 80 g</td></tr>" +
                "<tr><td>· 蛋白质</td><td>60 - 100 g</td></tr>" +
                "<tr><td>· 热量</td><td>1800 - 2400 大卡</td></tr>" +
                "<tr><td>· 膳食纤维</td><td>25 - 30 g</td></tr>" +
                "<tr><td>· 胆固醇</td><td>< 2.5 mmol</td></tr>" +
                "<tr><td>· 饮水量</td><td>1500 - 2000 ml</td></tr>" +
                "</table><p style='color:#888;font-size:12px'>* 实际需求因性别/体重/活动量而异</p>" +
                "</body></html>";
        JOptionPane.showMessageDialog(this, msg, "推荐摄入量", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 弹窗显示全部历史记录 (含日期范围筛选)
     */
    private void showHistoryDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "膳食摄入历史记录 - " + currentUser.getUsername(), true);
        dialog.setSize(960, 520);
        dialog.setLocationRelativeTo(this);

        JPanel root = new JPanel(new BorderLayout(0, 10));
        root.setBackground(Color.WHITE);
        root.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        // 顶部: 标题 + 筛选
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel title = new JLabel("全部历史记录");
        title.setFont(new Font("微软雅黑", Font.BOLD, 16));
        title.setForeground(UIUtil.USER_TEXT);
        top.add(title, BorderLayout.WEST);

        JPanel filter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filter.setOpaque(false);
        filter.add(new JLabel("从"));
        JTextField txtFrom = new JTextField(8);
        JTextField txtTo = new JTextField(8);
        // 默认: 过去 30 天
        Calendar c = Calendar.getInstance();
        txtTo.setText(new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
        c.add(Calendar.DAY_OF_MONTH, -29);
        txtFrom.setText(new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
        JButton btnQuery = UIUtil.createUserPrimaryButton("查询");
        JButton btnClose = UIUtil.createUserOutlineButton("关闭", UIUtil.USER_TEXT_SECONDARY);
        filter.add(txtFrom);
        filter.add(new JLabel("至"));
        filter.add(txtTo);
        filter.add(btnQuery);
        filter.add(btnClose);
        top.add(filter, BorderLayout.EAST);
        root.add(top, BorderLayout.NORTH);

        // 表格
        String[] columns = {"ID", "日期", "碳水(g)", "脂肪(g)", "蛋白质(g)", "热量(大卡)",
                "膳食纤维(g)", "胆固醇(mmol)", "饮水量(ml)", "备注", "录入时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(model);
        t.setRowHeight(28);
        t.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        t.getTableHeader().setBackground(UIUtil.USER_PRIMARY_LIGHT);
        t.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        t.setAutoCreateRowSorter(true);
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1));

        JLabel stat = new JLabel(" ");
        stat.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        stat.setForeground(UIUtil.USER_TEXT_SECONDARY);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(stat, BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(0, 8));
        center.setOpaque(false);
        center.add(sp, BorderLayout.CENTER);
        center.add(bottom, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);
        dialog.setContentPane(root);

        // 查询动作
        Runnable doQuery = () -> {
            model.setRowCount(0);
            try {
                Date from = new SimpleDateFormat("yyyy-MM-dd").parse(txtFrom.getText().trim());
                Date to = new SimpleDateFormat("yyyy-MM-dd").parse(txtTo.getText().trim());
                if (from.after(to)) {
                    UIUtil.showError(dialog, "起始日期不能晚于结束日期");
                    return;
                }
                List<DietRecord> all = service.findByUserId(currentUser.getId());
                int matched = 0;
                for (DietRecord r : all) {
                    Date d = r.getRecordDate();
                    if (d == null) continue;
                    if (d.compareTo(from) >= 0 && d.compareTo(to) <= 0) {
                        model.addRow(new Object[]{
                            r.getId(), r.getRecordDateStr(),
                            fmt(r.getCarbs()), fmt(r.getFat()), fmt(r.getProtein()),
                            fmt(r.getCalories()), fmt(r.getFiber()),
                            fmt(r.getCholesterol()), fmt(r.getWater()),
                            r.getNote() == null ? "" : r.getNote(),
                            r.getCreateTimeStr()
                        });
                        matched++;
                    }
                }
                stat.setText("共 " + matched + " 条记录");
            } catch (ParseException ex) {
                UIUtil.showError(dialog, "日期格式错误，请使用 yyyy-MM-dd");
            }
        };
        btnQuery.addActionListener(e -> doQuery.run());
        btnClose.addActionListener(e -> dialog.dispose());
        doQuery.run(); // 初次加载
        dialog.setVisible(true);
    }

    private String fmt(double v) {
        if (v == Math.floor(v)) return String.valueOf((long) v);
        return String.format("%.1f", v);
    }

    // ====== 自定义图表组件（柱状图 + 折线图叠加） ======
    private static class IntakeChartPanel extends JPanel {
        // 7 个指标配色, 与图例顺序一致
        private static final String[] KEYS = {"carbs", "fat", "protein", "fiber", "calories", "cholesterol", "water"};
        private static final String[] LABELS = {
            "碳水化合物(g)", "脂肪(g)", "蛋白质(g)", "膳食纤维(g)", "热量(大卡)", "胆固醇(mmol)", "饮水量(ml)"
        };
        private static final Color[] COLORS = {
            new Color(91, 156, 214),    // 碳水 - 蓝
            new Color(237, 125, 49),    // 脂肪 - 橙
            new Color(112, 173, 71),    // 蛋白质 - 绿
            new Color(68, 114, 196),    // 膳食纤维 - 深蓝
            new Color(255, 192, 0),     // 热量 - 黄
            new Color(165, 105, 189),   // 胆固醇 - 紫
            new Color(45, 55, 65)       // 饮水 - 深灰
        };

        private List<DietRecord> data = new ArrayList<>();

        // 鼠标悬停命中检测用的点坐标缓存: pts[k][i] = {x, y}
        private int[][][] pts = new int[0][0][0];
        private int ptsN = 0;
        // 当前悬停命中的指标/日期索引，用于绘制高亮
        private int hoverK = -1;
        private int hoverI = -1;

        public IntakeChartPanel() {
            // 启用 Swing tooltip 机制 —— 用非空占位符确保 ToolTipManager 注册本组件
            setToolTipText(" ");
            ToolTipManager ttm = ToolTipManager.sharedInstance();
            ttm.registerComponent(this);
            ttm.setInitialDelay(0);      // 鼠标移入立即触发
            ttm.setReshowDelay(0);       // 立即重新显示
            ttm.setDismissDelay(15000);  // 显示 15 秒

            // 鼠标移动时刷新命中状态并重绘高亮
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int[] hit = hitTest(e.getX(), e.getY());
                    int nk = hit[0], ni = hit[1];
                    if (nk != hoverK || ni != hoverI) {
                        hoverK = nk;
                        hoverI = ni;
                        repaint();
                    }
                }
            });
        }

        public void setData(List<DietRecord> data) {
            this.data = data == null ? new ArrayList<>() : new ArrayList<>(data);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // 边距
            int left = 58, right = 16, top = 52, bottom = 60;
            int chartW = w - left - right;
            int chartH = h - top - bottom;

            // 标题
            g2.setFont(new Font("微软雅黑", Font.BOLD, 14));
            g2.setColor(new Color(33, 37, 41));
            g2.drawString("摄入量分析", 12, 20);

            // 图例
            drawLegend(g2, w);

            // 坐标轴
            g2.setColor(new Color(220, 220, 220));
            g2.setStroke(new BasicStroke(1f));
            g2.drawLine(left, top + chartH, left + chartW, top + chartH); // X轴
            g2.drawLine(left, top, left, top + chartH);                     // Y轴

            if (data.isEmpty()) {
                g2.setColor(new Color(150, 150, 150));
                g2.setFont(new Font("微软雅黑", Font.PLAIN, 14));
                String msg = "暂无数据，先录入今日膳食吧";
                int tw = g2.getFontMetrics().stringWidth(msg);
                g2.drawString(msg, (w - tw) / 2, h / 2);
                g2.dispose();
                return;
            }

            // 计算统一 Y 轴最大值 (取所有指标、所有天中的真实最大值)
            double rawMax = 0;
            for (DietRecord r : data) {
                double[] vals = valuesOf(r);
                for (double v : vals) {
                    if (v > rawMax) rawMax = v;
                }
            }
            double yMax = niceYMax(rawMax);

            // Y 轴刻度与网格线
            g2.setStroke(new BasicStroke(0.5f));
            g2.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            int gridCount = 5;
            for (int i = 0; i <= gridCount; i++) {
                double ratio = i / (double) gridCount;
                int y = top + chartH - (int) (chartH * ratio);
                g2.setColor(new Color(235, 238, 242));
                g2.drawLine(left, y, left + chartW, y);
                g2.setColor(new Color(120, 120, 120));
                String label = formatYLabel(yMax * ratio);
                int lw = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, left - lw - 6, y + 4);
            }

            // 每天一个分组: 7 根柱子 + 每个柱顶一个点 + 同指标跨天连线
            int n = data.size();
            double groupW = chartW * 1.0 / n;
            double barW = groupW * 0.08;          // 每个柱宽
            double barGap = groupW * 0.01;        // 柱间距
            double barsTotalW = KEYS.length * barW + (KEYS.length - 1) * barGap;
            double barsStartOffset = (groupW - barsTotalW) / 2.0;

            // 每个指标每天一个点: [k][i] = Point
            int[][][] pts = new int[KEYS.length][n][2];

            for (int i = 0; i < n; i++) {
                DietRecord r = data.get(i);
                double[] vals = valuesOf(r);
                double groupX = left + groupW * i;

                for (int k = 0; k < KEYS.length; k++) {
                    double v = vals[k];
                    int bh = (int) (chartH * v / yMax);
                    if (bh < 0) bh = 0;
                    int bx = (int) (groupX + barsStartOffset + k * (barW + barGap));
                    int by = top + chartH - bh;

                    // 柱状
                    g2.setColor(COLORS[k]);
                    g2.fillRect(bx, by, (int) barW, bh);
                    g2.setColor(COLORS[k].darker());
                    g2.setStroke(new BasicStroke(0.5f));
                    g2.drawRect(bx, by, (int) barW, bh);

                    // 柱顶中心点
                    int cx = bx + (int) (barW / 2.0);
                    int cy = by;
                    pts[k][i][0] = cx;
                    pts[k][i][1] = cy;

                    g2.setColor(COLORS[k]);
                    g2.fillOval(cx - 3, cy - 3, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.fillOval(cx - 1, cy - 1, 2, 2);

                    // 悬停高亮：在命中的柱顶点画醒目外环 + 柱体高亮边
                    if (k == hoverK && i == hoverI) {
                        g2.setColor(new Color(255, 70, 70));
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawOval(cx - 6, cy - 6, 12, 12);
                        g2.drawRect(bx - 1, by - 1, (int) barW + 1, bh + 1);
                    }
                }

                // X 轴日期标签
                g2.setColor(new Color(100, 100, 100));
                g2.setFont(new Font("微软雅黑", Font.PLAIN, 11));
                String label = r.getRecordDateStr();
                if (label != null && label.length() >= 5) {
                    label = label.substring(5); // 只显示 MM-dd
                }
                int tw = g2.getFontMetrics().stringWidth(label);
                g2.drawString(label, (int) (groupX + groupW / 2 - tw / 2), top + chartH + 16);
            }

            // 同指标跨天连成折线
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int k = 0; k < KEYS.length; k++) {
                g2.setColor(COLORS[k]);
                for (int i = 0; i < n - 1; i++) {
                    int x1 = pts[k][i][0];
                    int y1 = pts[k][i][1];
                    int x2 = pts[k][i + 1][0];
                    int y2 = pts[k][i + 1][1];
                    g2.drawLine(x1, y1, x2, y2);
                }
            }

            // 缓存点坐标供 tooltip 命中检测使用
            this.pts = pts;
            this.ptsN = n;

            g2.dispose();
        }

        /**
         * 鼠标悬停在数据点附近时显示指标名称 + 数值 + 日期
         */
        @Override
        public String getToolTipText(MouseEvent e) {
            int[] hit = hitTest(e.getX(), e.getY());
            if (hit[0] < 0) return null;
            int bestK = hit[0];
            int bestI = hit[1];
            DietRecord r = data.get(bestI);
            double v = valuesOf(r)[bestK];
            String dateStr = r.getRecordDateStr();
            int rgb = COLORS[bestK].getRGB() & 0xFFFFFF;
            String unit = unitOf(bestK);
            return String.format(
                "<html><body style='padding:6px 8px;font-family:微软雅黑'>"
                + "<b style='color:#%06x;font-size:13px'>● %s</b>"
                + "&nbsp;&nbsp;<span style='color:#888;font-size:12px'>%s</span><br/>"
                + "<span style='font-size:14px'><b>%s</b> %s</span>"
                + "</body></html>",
                rgb, LABELS[bestK], dateStr, fmtVal(v), unit
            );
        }

        /** tooltip 显示在鼠标右下方，避免遮挡柱顶 */
        @Override
        public Point getToolTipLocation(MouseEvent e) {
            return new Point(e.getX() + 14, e.getY() + 14);
        }

        /**
         * 命中检测: 返回 {k, i}，未命中返回 {-1, -1}
         * 优先匹配柱顶小点（14px半径），其次匹配柱体内部（鼠标落在柱子上也提示）
         */
        private int[] hitTest(int mx, int my) {
            if (pts == null || pts.length == 0 || data.isEmpty()) return new int[]{-1, -1};
            // 1) 优先：柱顶小点 14px 半径
            int bestK = -1, bestI = -1;
            double bestDist = 14 * 14;
            for (int k = 0; k < KEYS.length; k++) {
                if (k >= pts.length) continue;
                for (int i = 0; i < ptsN; i++) {
                    if (i >= pts[k].length) continue;
                    int dx = pts[k][i][0] - mx;
                    int dy = pts[k][i][1] - my;
                    double d = dx * dx + dy * dy;
                    if (d < bestDist) {
                        bestDist = d;
                        bestK = k;
                        bestI = i;
                    }
                }
            }
            if (bestK >= 0) return new int[]{bestK, bestI};
            // 2) 兜底：鼠标落在柱体矩形内
            //    需要重新计算柱体坐标（与 paintComponent 一致）
            int w = getWidth();
            int left = 58, right = 16, top = 52, bottom = 60;
            int chartW = w - left - right;
            int chartH = getHeight() - top - bottom;
            int n = data.size();
            if (n == 0) return new int[]{-1, -1};
            double groupW = chartW * 1.0 / n;
            double barW = groupW * 0.08;
            double barGap = groupW * 0.01;
            double barsTotalW = KEYS.length * barW + (KEYS.length - 1) * barGap;
            double barsStartOffset = (groupW - barsTotalW) / 2.0;
            // Y 轴最大值
            double rawMax = 0;
            for (DietRecord r : data) {
                for (double v : valuesOf(r)) if (v > rawMax) rawMax = v;
            }
            double yMax = niceYMax(rawMax);
            for (int i = 0; i < n; i++) {
                DietRecord r = data.get(i);
                double[] vals = valuesOf(r);
                double groupX = left + groupW * i;
                for (int k = 0; k < KEYS.length; k++) {
                    double v = vals[k];
                    int bh = (int) (chartH * v / yMax);
                    if (bh < 0) bh = 0;
                    int bx = (int) (groupX + barsStartOffset + k * (barW + barGap));
                    int by = top + chartH - bh;
                    if (mx >= bx - 1 && mx <= bx + (int) barW + 1
                        && my >= by - 1 && my <= top + chartH + 1) {
                        return new int[]{k, i};
                    }
                }
            }
            return new int[]{-1, -1};
        }

        /** 返回指标单位 */
        private String unitOf(int k) {
            switch (k) {
                case 4: return "大卡";
                case 5: return "mmol";
                case 6: return "ml";
                default: return "g";
            }
        }

        private String fmtVal(double v) {
            if (v == Math.floor(v)) return String.valueOf((long) v);
            return String.format("%.1f", v);
        }

        /**
         * 把原始最大值向上取整为好看的 Y 轴上限 (1,2,5 进制)
         */
        private double niceYMax(double rawMax) {
            if (rawMax <= 0) return 100;
            double pow10 = Math.pow(10, Math.floor(Math.log10(rawMax)));
            double normalized = rawMax / pow10;
            double ceil;
            if (normalized <= 1) ceil = 1;
            else if (normalized <= 2) ceil = 2;
            else if (normalized <= 5) ceil = 5;
            else ceil = 10;
            return ceil * pow10;
        }

        private String formatYLabel(double v) {
            if (v >= 1000) return String.format("%.0fk", v / 1000);
            if (v == Math.floor(v)) return String.valueOf((long) v);
            return String.format("%.1f", v);
        }

        private double[] valuesOf(DietRecord r) {
            // 顺序必须与 KEYS 一致
            return new double[]{
                r.getCarbs(),
                r.getFat(),
                r.getProtein(),
                r.getFiber(),
                r.getCalories(),
                r.getCholesterol(),
                r.getWater()
            };
        }

        private void drawLegend(Graphics2D g2, int w) {
            int x = 110, y = 10;
            int swatchSize = 12;
            int lineHeight = 18;
            g2.setFont(new Font("微软雅黑", Font.PLAIN, 11));
            int rightEdge = w - 12;
            for (int i = 0; i < LABELS.length; i++) {
                int labelW = g2.getFontMetrics().stringWidth(LABELS[i]);
                int itemW = swatchSize + 4 + labelW + 16;
                // 放不下就换行
                if (x + itemW > rightEdge && x > 110) {
                    x = 110;
                    y += lineHeight;
                }
                g2.setColor(COLORS[i]);
                g2.fillRect(x, y, swatchSize, swatchSize);
                g2.setColor(new Color(50, 50, 50));
                g2.setStroke(new BasicStroke(0.5f));
                g2.drawRect(x, y, swatchSize, swatchSize);
                g2.setColor(new Color(50, 50, 50));
                g2.drawString(LABELS[i], x + swatchSize + 4, y + 10);
                x += itemW;
            }
        }
    }
}

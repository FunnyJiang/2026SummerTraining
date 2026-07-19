package com.bmi.ui.admin;

import com.bmi.model.ForumPost;
import com.bmi.model.ForumReply;
import com.bmi.model.User;
import com.bmi.service.ForumService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 管理员论坛管理面板
 */
public class AdminForumPanel extends JPanel {
    private ForumService forumService = new ForumService();
    private User currentUser;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public AdminForumPanel(User user) {
        this.currentUser = user;
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
        JLabel titleLabel = new JLabel("美食论坛");
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

        JButton btnView = UIUtil.createPrimaryButton("查看详情");
        btnView.setPreferredSize(new Dimension(100, 36));
        btnView.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要查看的帖子！"); return; }
            viewPost(row);
        });
        toolbar.add(btnView);

        JButton btnDelete = UIUtil.createDangerButton("删除帖子");
        btnDelete.setPreferredSize(new Dimension(100, 36));
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { UIUtil.showError(this, "请选择要删除的帖子！"); return; }
            deletePost(row);
        });
        toolbar.add(btnDelete);

        // 表格
        String[] columns = {"ID", "标题", "发帖人", "浏览量", "发布时间"};
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
        List<ForumPost> list = (keyword == null || keyword.isEmpty()) ?
            forumService.findAllPosts() : forumService.searchPosts(keyword);
        for (ForumPost p : list) {
            tableModel.addRow(new Object[]{
                p.getId(), p.getTitle(), p.getUserName(), p.getViews(), p.getCreateTimeStr()
            });
        }
    }

    private void viewPost(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        ForumPost post = forumService.findPostById(id);
        if (post == null) return;

        forumService.incrementViews(id);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "帖子详情", true);
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setBackground(Color.WHITE);

        // 帖子信息
        JPanel postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        postPanel.setBackground(new Color(248, 249, 250));
        postPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(UIUtil.PRIMARY_COLOR);
        postPanel.add(titleLabel);
        postPanel.add(Box.createVerticalStrut(10));

        JLabel infoLabel = new JLabel("发布人: " + post.getUserName() + "  |  浏览: " + (post.getViews() + 1) +
                "  |  时间: " + post.getCreateTimeStr());
        infoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        infoLabel.setForeground(UIUtil.USER_TEXT_LIGHT);
        postPanel.add(infoLabel);
        postPanel.add(Box.createVerticalStrut(10));

        JLabel contentLabel = new JLabel("<html><body style='width:520px'>" + post.getContent() + "</body></html>");
        contentLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        postPanel.add(contentLabel);

        panel.add(postPanel, BorderLayout.NORTH);

        // 回复区
        JPanel replyPanel = new JPanel(new BorderLayout());
        replyPanel.setBackground(Color.WHITE);

        JLabel replyTitle = new JLabel("  回复列表");
        replyTitle.setFont(new Font("微软雅黑", Font.BOLD, 15));
        replyPanel.add(replyTitle, BorderLayout.NORTH);

        JPanel replyListPanel = new JPanel();
        replyListPanel.setLayout(new BoxLayout(replyListPanel, BoxLayout.Y_AXIS));
        replyListPanel.setBackground(Color.WHITE);

        List<ForumReply> replies = forumService.findRepliesByPostId(id);
        for (ForumReply r : replies) {
            JPanel rp = new JPanel(new BorderLayout(5, 3));
            rp.setBackground(new Color(248, 250, 252));
            rp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            JLabel rInfo = new JLabel(r.getUserName() + "  " + r.getCreateTimeStr());
            rInfo.setFont(new Font("微软雅黑", Font.PLAIN, 12));
            rInfo.setForeground(UIUtil.USER_TEXT_LIGHT);
            JLabel rContent = new JLabel(r.getContent());
            rContent.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            rp.add(rInfo, BorderLayout.NORTH);
            rp.add(rContent, BorderLayout.CENTER);
            replyListPanel.add(rp);
            replyListPanel.add(Box.createVerticalStrut(5));
        }

        JScrollPane replyScroll = new JScrollPane(replyListPanel);
        replyScroll.setBorder(null);
        replyPanel.add(replyScroll, BorderLayout.CENTER);

        panel.add(replyScroll, BorderLayout.CENTER);

        JButton btnClose = new JButton("关闭");
        btnClose.setPreferredSize(new Dimension(80, 36));
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnClose);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);

        loadData(txtSearch.getText().trim());
    }

    private void deletePost(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String title = (String) tableModel.getValueAt(row, 1);
        if (UIUtil.showConfirm(this, "确定要删除帖子「" + title + "」吗？")) {
            if (forumService.deletePost(id)) {
                UIUtil.showInfo(this, "删除成功！");
                loadData(txtSearch.getText().trim());
            } else {
                UIUtil.showError(this, "删除失败！");
            }
        }
    }
}

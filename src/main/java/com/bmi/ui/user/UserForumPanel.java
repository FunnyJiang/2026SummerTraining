package com.bmi.ui.user;

import com.bmi.dao.PostLikeDAO;
import com.bmi.model.ForumPost;
import com.bmi.model.ForumReply;
import com.bmi.model.User;
import com.bmi.service.ForumService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 用户论坛面板（卡片式布局）
 */
public class UserForumPanel extends JPanel {
    private User currentUser;
    private ForumService forumService = new ForumService();
    private PostLikeDAO postLikeDAO = new PostLikeDAO();

    private JPanel postsCardsPanel;
    private JPanel categoryFilterPanel;
    private JTextField txtSearch;
    private int selectedCategoryId = 0;  // 0=全部, 用字符串映射
    private String selectedCategory = "全部";

    // 论坛分类列表
    private static final String[] FORUM_CATEGORIES = {
        "全部", "BMI健康讨论", "减脂经验", "增肌分享", "营养知识", "综合讨论"
    };

    public UserForumPanel(User user) {
        this.currentUser = user;
        initUI();
        loadPosts(null, "全部");
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(UIUtil.USER_BG);

        // 顶部标题栏
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setPreferredSize(new Dimension(0, 64));
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtil.USER_BORDER),
            BorderFactory.createEmptyBorder(16, 28, 16, 28)
        ));

        JLabel titleLabel = new JLabel("美食论坛");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(UIUtil.USER_TEXT);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        JLabel statsLabel = new JLabel("分享BMI健康经验 · 交流减脂增肌心得");
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        statsLabel.setForeground(UIUtil.USER_TEXT_SECONDARY);
        titlePanel.add(statsLabel, BorderLayout.EAST);

        // 工具栏：搜索 + 发帖按钮
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(Color.WHITE);
        toolbar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        // 搜索区
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Color.WHITE);

        txtSearch = UIUtil.createUserTextField(15);
        searchPanel.add(new JLabel("搜索") {{ setFont(new Font("微软雅黑", Font.PLAIN, 14)); setForeground(UIUtil.USER_TEXT_SECONDARY); }});
        searchPanel.add(txtSearch);

        JButton btnSearch = UIUtil.createUserPrimaryButton("搜索");
        btnSearch.setPreferredSize(new Dimension(70, 34));
        btnSearch.addActionListener(e -> loadPosts(txtSearch.getText().trim(), selectedCategory));
        searchPanel.add(btnSearch);

        JButton btnClear = UIUtil.createUserOutlineButton("清空", UIUtil.USER_TEXT_SECONDARY);
        btnClear.setPreferredSize(new Dimension(70, 34));
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            selectedCategory = "全部";
            loadCategoryFilter();
            loadPosts(null, "全部");
        });
        searchPanel.add(btnClear);

        toolbar.add(searchPanel, BorderLayout.WEST);

        JButton btnNewPost = UIUtil.createUserPrimaryButton("+ 发布新帖");
        btnNewPost.setPreferredSize(new Dimension(110, 36));
        btnNewPost.addActionListener(e -> showNewPostDialog());
        toolbar.add(btnNewPost, BorderLayout.EAST);

        // 分类筛选
        categoryFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        categoryFilterPanel.setBackground(Color.WHITE);
        categoryFilterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        loadCategoryFilter();

        // 帖子卡片容器
        postsCardsPanel = new JPanel();
        postsCardsPanel.setLayout(new BoxLayout(postsCardsPanel, BoxLayout.Y_AXIS));
        postsCardsPanel.setBackground(UIUtil.USER_BG);
        postsCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane cardsScroll = new JScrollPane(postsCardsPanel);
        cardsScroll.setBorder(null);
        cardsScroll.setBackground(UIUtil.BG_COLOR);
        cardsScroll.getVerticalScrollBar().setUnitIncrement(16);

        // 组装
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(UIUtil.BG_COLOR);
        topWrapper.add(titlePanel, BorderLayout.NORTH);
        topWrapper.add(toolbar, BorderLayout.CENTER);
        topWrapper.add(categoryFilterPanel, BorderLayout.SOUTH);

        add(topWrapper, BorderLayout.NORTH);
        add(cardsScroll, BorderLayout.CENTER);
    }

    /**
     * 加载分类筛选标签
     */
    private void loadCategoryFilter() {
        categoryFilterPanel.removeAll();
        for (String cat : FORUM_CATEGORIES) {
            addCategoryTag(cat);
        }
        categoryFilterPanel.revalidate();
        categoryFilterPanel.repaint();
    }

    private void addCategoryTag(String catName) {
        boolean selected = selectedCategory.equals(catName);
        JLabel tag = new JLabel(catName, SwingConstants.CENTER);
        tag.setFont(new Font("微软雅黑", selected ? Font.BOLD : Font.PLAIN, 13));
        tag.setOpaque(true);
        tag.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tag.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));

        if (selected) {
            tag.setBackground(UIUtil.USER_PRIMARY);
            tag.setForeground(Color.WHITE);
            tag.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.USER_PRIMARY, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
        } else {
            tag.setBackground(Color.WHITE);
            tag.setForeground(UIUtil.USER_TEXT);
            tag.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
            ));
        }

        tag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedCategory = catName;
                loadCategoryFilter();
                loadPosts(txtSearch.getText().trim(), catName);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!selectedCategory.equals(catName)) tag.setBackground(UIUtil.USER_SIDEBAR_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (!selectedCategory.equals(catName)) tag.setBackground(Color.WHITE);
            }
        });

        categoryFilterPanel.add(tag);
    }

    /**
     * 加载帖子列表（卡片式）
     */
    private void loadPosts(String keyword, String category) {
        postsCardsPanel.removeAll();

        List<ForumPost> list;
        if (keyword != null && !keyword.isEmpty()) {
            list = forumService.searchPosts(keyword);
        } else {
            list = forumService.findAllPosts();
        }

        // 按分类筛选
        if (!"全部".equals(category)) {
            list = list.stream()
                .filter(p -> category.equals(p.getCategory()))
                .collect(java.util.stream.Collectors.toList());
        }

        if (list.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(UIUtil.BG_COLOR);
            emptyPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 60, 0));
            JLabel emptyLabel = new JLabel("暂无帖子，快来发布第一篇吧！", SwingConstants.CENTER);
            emptyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            emptyLabel.setForeground(UIUtil.TEXT_LIGHT);
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            postsCardsPanel.add(emptyPanel);
        } else {
            for (ForumPost p : list) {
                postsCardsPanel.add(createPostCard(p));
                postsCardsPanel.add(Box.createVerticalStrut(12));
            }
        }

        postsCardsPanel.revalidate();
        postsCardsPanel.repaint();
    }

    /**
     * 创建帖子卡片（截图风格）
     */
    private JPanel createPostCard(ForumPost post) {
        JPanel card = UIUtil.createUserCardPanel(10);
        card.setLayout(new BorderLayout(12, 0));
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(1200, 200));
        card.setPreferredSize(new Dimension(0, 160));

        // 左侧：头像 + 用户信息
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setPreferredSize(new Dimension(80, 0));

        JPanel avatar = UIUtil.createAvatarPanel(post.getUserName(), UIUtil.PRIMARY_COLOR, 50);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(avatar);
        leftPanel.add(Box.createVerticalStrut(5));

        JLabel userLabel = new JLabel(post.getUserName(), SwingConstants.CENTER);
        userLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        userLabel.setForeground(UIUtil.TEXT_LIGHT);
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(userLabel);

        card.add(leftPanel, BorderLayout.WEST);

        // 中部：标题 + 内容 + 标签
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);

        // 标题行
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titleRow.setBackground(Color.WHITE);
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (post.getIsHot() == 1) {
            JLabel hotBadge = new JLabel("[热门]");
            hotBadge.setFont(new Font("微软雅黑", Font.BOLD, 12));
            hotBadge.setForeground(UIUtil.DANGER_COLOR);
            hotBadge.setBackground(new Color(255, 235, 238));
            hotBadge.setOpaque(true);
            hotBadge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
            titleRow.add(hotBadge);
        }

        // 分类标签
        Color[] catColors = getForumCategoryColors(post.getCategory());
        JLabel catTag = UIUtil.createTagLabel(post.getCategory(), catColors[0], catColors[1]);
        catTag.setFont(new Font("微软雅黑", Font.PLAIN, 11));
        titleRow.add(catTag);

        JLabel titleLabel = new JLabel(post.getTitle());
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setForeground(UIUtil.TEXT_COLOR);
        titleRow.add(titleLabel);

        centerPanel.add(titleRow);
        centerPanel.add(Box.createVerticalStrut(8));

        // 内容摘要
        String content = post.getContent() != null ? post.getContent() : "";
        if (content.length() > 120) content = content.substring(0, 120) + "...";
        JLabel contentLabel = new JLabel("<html><body style='width:600px'>" + content + "</body></html>");
        contentLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        contentLabel.setForeground(UIUtil.TEXT_LIGHT);
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(contentLabel);
        centerPanel.add(Box.createVerticalStrut(10));

        // 底部信息行
        JPanel infoRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        infoRow.setBackground(Color.WHITE);
        infoRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel("[日期] " + post.getCreateTimeStr());
        timeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        timeLabel.setForeground(UIUtil.TEXT_LIGHT);
        infoRow.add(timeLabel);

        JLabel viewsLabel = new JLabel("[看] " + post.getViews() + " 浏览");
        viewsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        viewsLabel.setForeground(UIUtil.TEXT_LIGHT);
        infoRow.add(viewsLabel);

        JLabel repliesLabel = new JLabel("[回复] " + forumService.findRepliesByPostId(post.getId()).size() + " 回复");
        repliesLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        repliesLabel.setForeground(UIUtil.USER_TEXT_SECONDARY);
        infoRow.add(repliesLabel);

        centerPanel.add(infoRow);
        card.add(centerPanel, BorderLayout.CENTER);

        // 右侧：点赞按钮 + 查看按钮
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setPreferredSize(new Dimension(120, 0));

        // 点赞按钮
        boolean hasLiked = postLikeDAO.hasLiked(currentUser.getId(), post.getId());
        JButton btnLike = new JButton(hasLiked ? "[已赞] " + post.getLikes() : "[赞] " + post.getLikes());
        btnLike.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        btnLike.setForeground(hasLiked ? UIUtil.DANGER_COLOR : UIUtil.USER_TEXT_SECONDARY);
        btnLike.setBackground(Color.WHITE);
        btnLike.setFocusPainted(false);
        btnLike.setBorder(BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1));
        btnLike.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLike.setPreferredSize(new Dimension(100, 36));
        btnLike.setMaximumSize(new Dimension(100, 36));
        btnLike.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLike.addActionListener(e -> toggleLike(post, btnLike));
        rightPanel.add(btnLike);
        rightPanel.add(Box.createVerticalStrut(10));

        // 查看按钮
        JButton btnView = UIUtil.createUserPrimaryButton("查看详情");
        btnView.setPreferredSize(new Dimension(100, 36));
        btnView.setMaximumSize(new Dimension(100, 36));
        btnView.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnView.addActionListener(e -> viewPost(post));
        rightPanel.add(btnView);

        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    /**
     * 点赞/取消点赞
     */
    private void toggleLike(ForumPost post, JButton btnLike) {
        boolean hasLiked = postLikeDAO.hasLiked(currentUser.getId(), post.getId());
        if (hasLiked) {
            // 取消点赞
            if (postLikeDAO.removeLike(currentUser.getId(), post.getId())) {
                // 更新帖子likes计数
                forumService.updatePostLikes(post.getId(), -1);
                btnLike.setText("[心] " + (post.getLikes() - 1));
                btnLike.setForeground(UIUtil.TEXT_LIGHT);
            }
        } else {
            // 点赞
            if (postLikeDAO.addLike(currentUser.getId(), post.getId())) {
                forumService.updatePostLikes(post.getId(), 1);
                btnLike.setText("[赞] " + (post.getLikes() + 1));
                btnLike.setForeground(UIUtil.DANGER_COLOR);
            }
        }
    }

    /**
     * 论坛分类标签颜色映射
     */
    private Color[] getForumCategoryColors(String category) {
        if (category == null) return new Color[]{UIUtil.TAG_GRAY_BG, UIUtil.TAG_GRAY_TEXT};
        switch (category) {
            case "BMI健康讨论": return new Color[]{new Color(227, 242, 253), new Color(21, 101, 192)};
            case "减脂经验": return new Color[]{new Color(232, 245, 233), new Color(46, 125, 50)};
            case "增肌分享": return new Color[]{new Color(255, 243, 224), new Color(230, 81, 0)};
            case "营养知识": return new Color[]{new Color(243, 229, 245), new Color(142, 36, 170)};
            case "综合讨论": return new Color[]{UIUtil.TAG_GRAY_BG, UIUtil.TAG_GRAY_TEXT};
            default: return new Color[]{UIUtil.TAG_GRAY_BG, UIUtil.TAG_GRAY_TEXT};
        }
    }

    /**
     * 发布新帖对话框（含分类选择）
     */
    private void showNewPostDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "发布新帖", true);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        // 分类选择
        JPanel catRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        catRow.setBackground(Color.WHITE);
        catRow.add(UIUtil.createLabel("分类:"));
        JComboBox<String> cmbCategory = new JComboBox<>(FORUM_CATEGORIES);
        cmbCategory.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        // Remove "全部" from the combo since it's not a real category
        cmbCategory.removeItem("全部");
        cmbCategory.setSelectedItem("综合讨论");
        catRow.add(cmbCategory);
        formPanel.add(catRow);
        formPanel.add(Box.createVerticalStrut(10));

        // 标题输入
        formPanel.add(UIUtil.createLabel("标题:"));
        JTextField txtTitle = UIUtil.createTextField(30);
        formPanel.add(txtTitle);
        formPanel.add(Box.createVerticalStrut(10));

        // 内容输入
        formPanel.add(UIUtil.createLabel("内容:"));
        JTextArea txtContent = UIUtil.createTextArea();
        txtContent.setRows(8);
        JScrollPane contentScroll = new JScrollPane(txtContent);
        formPanel.add(contentScroll);

        panel.add(formPanel, BorderLayout.CENTER);

        // 按钮
        JButton btnPost = UIUtil.createPrimaryButton("发布");
        JButton btnCancel = new JButton("取消");
        btnCancel.setPreferredSize(new Dimension(80, 36));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnPost);
        btnPanel.add(btnCancel);
        panel.add(btnPanel, BorderLayout.SOUTH);

        btnPost.addActionListener(e -> {
            String title = txtTitle.getText().trim();
            String content = txtContent.getText().trim();
            String category = (String) cmbCategory.getSelectedItem();
            if (title.isEmpty()) {
                UIUtil.showError(dialog, "请输入标题！");
                return;
            }
            if (content.isEmpty()) {
                UIUtil.showError(dialog, "请输入内容！");
                return;
            }

            ForumPost post = new ForumPost(currentUser.getId(), title, content, category);
            if (forumService.addPost(post)) {
                UIUtil.showInfo(dialog, "发布成功！");
                dialog.dispose();
                loadPosts(txtSearch.getText().trim(), selectedCategory);
            } else {
                UIUtil.showError(dialog, "发布失败！");
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(panel);
        dialog.setVisible(true);
    }

    /**
     * 查看帖子详情（含回复和点赞）
     */
    private void viewPost(ForumPost post) {
        forumService.incrementViews(post.getId());

        ForumPost fetched = forumService.findPostById(post.getId());
        final ForumPost actualPost = (fetched != null) ? fetched : post;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "帖子详情", true);
        dialog.setSize(650, 700);
        dialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(Color.WHITE);

        // ===== 帖子信息区 =====
        JPanel postInfoPanel = new JPanel(new BorderLayout(12, 0));
        postInfoPanel.setBackground(new Color(248, 250, 252));
        postInfoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // 头像
        JPanel avatarWrap = new JPanel();
        avatarWrap.setLayout(new BoxLayout(avatarWrap, BoxLayout.Y_AXIS));
        avatarWrap.setBackground(new Color(248, 250, 252));
        JPanel avatar = UIUtil.createAvatarPanel(actualPost.getUserName(), UIUtil.PRIMARY_COLOR, 50);
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarWrap.add(avatar);
        JLabel userNameLabel = new JLabel(actualPost.getUserName(), SwingConstants.CENTER);
        userNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        userNameLabel.setForeground(UIUtil.TEXT_LIGHT);
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarWrap.add(userNameLabel);
        postInfoPanel.add(avatarWrap, BorderLayout.WEST);

        // 帖子内容
        JPanel contentWrap = new JPanel();
        contentWrap.setLayout(new BoxLayout(contentWrap, BoxLayout.Y_AXIS));
        contentWrap.setBackground(new Color(248, 250, 252));

        // 标题行
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titleRow.setBackground(new Color(248, 250, 252));
        titleRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        if (actualPost.getIsHot() == 1) {
            JLabel hotBadge = new JLabel("[热门]");
            hotBadge.setFont(new Font("微软雅黑", Font.BOLD, 12));
            hotBadge.setForeground(UIUtil.DANGER_COLOR);
            hotBadge.setBackground(new Color(255, 235, 238));
            hotBadge.setOpaque(true);
            hotBadge.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
            titleRow.add(hotBadge);
        }

        Color[] catColors = getForumCategoryColors(actualPost.getCategory());
        titleRow.add(UIUtil.createTagLabel(actualPost.getCategory(), catColors[0], catColors[1]));

        JLabel titleLabel = new JLabel(actualPost.getTitle());
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        titleLabel.setForeground(UIUtil.TEXT_COLOR);
        titleRow.add(titleLabel);
        contentWrap.add(titleRow);
        contentWrap.add(Box.createVerticalStrut(8));

        // 统计行
        JLabel statsLabel = new JLabel("[看] " + (actualPost.getViews()) + " 浏览  |  [赞] " + actualPost.getLikes() + " 点赞  |  [日期] " + actualPost.getCreateTimeStr());
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        statsLabel.setForeground(UIUtil.TEXT_LIGHT);
        statsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentWrap.add(statsLabel);
        contentWrap.add(Box.createVerticalStrut(10));

        // 内容
        JLabel contentLabel = new JLabel("<html><body style='width:530px'>" + actualPost.getContent() + "</body></html>");
        contentLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        contentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentWrap.add(contentLabel);

        postInfoPanel.add(contentWrap, BorderLayout.CENTER);
        mainPanel.add(postInfoPanel, BorderLayout.NORTH);

        // ===== 回复区 =====
        JPanel replyPanel = new JPanel(new BorderLayout(5, 5));
        replyPanel.setBackground(Color.WHITE);

        JLabel replyTitle = new JLabel("[回复] (" + forumService.findRepliesByPostId(actualPost.getId()).size() + ")");
        replyTitle.setFont(new Font("微软雅黑", Font.BOLD, 15));
        replyTitle.setForeground(UIUtil.USER_TEXT);
        replyPanel.add(replyTitle, BorderLayout.NORTH);

        JPanel replyList = new JPanel();
        replyList.setLayout(new BoxLayout(replyList, BoxLayout.Y_AXIS));
        replyList.setBackground(Color.WHITE);

        List<ForumReply> replies = forumService.findRepliesByPostId(actualPost.getId());
        if (replies.isEmpty()) {
            JLabel noReply = new JLabel("暂无回复，快来发表你的看法吧！");
            noReply.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            noReply.setForeground(UIUtil.TEXT_LIGHT);
            replyList.add(noReply);
        } else {
            for (ForumReply r : replies) {
                JPanel rp = new JPanel(new BorderLayout(8, 3));
                rp.setBackground(new Color(248, 250, 252));
                rp.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
                ));

                // 头像 + 用户名
                JPanel rLeft = new JPanel();
                rLeft.setLayout(new BoxLayout(rLeft, BoxLayout.Y_AXIS));
                rLeft.setBackground(new Color(248, 250, 252));
                JPanel rAvatar = UIUtil.createAvatarPanel(r.getUserName(), new Color(76, 175, 80), 36);
                rAvatar.setAlignmentX(Component.CENTER_ALIGNMENT);
                rLeft.add(rAvatar);
                JLabel rName = new JLabel(r.getUserName(), SwingConstants.CENTER);
                rName.setFont(new Font("微软雅黑", Font.PLAIN, 11));
                rName.setForeground(UIUtil.TEXT_LIGHT);
                rName.setAlignmentX(Component.CENTER_ALIGNMENT);
                rLeft.add(rName);
                rp.add(rLeft, BorderLayout.WEST);

                // 回复内容 + 时间
                JPanel rContent = new JPanel();
                rContent.setLayout(new BoxLayout(rContent, BoxLayout.Y_AXIS));
                rContent.setBackground(new Color(248, 250, 252));

                JLabel rTime = new JLabel(r.getCreateTimeStr());
                rTime.setFont(new Font("微软雅黑", Font.PLAIN, 11));
                rTime.setForeground(UIUtil.TEXT_LIGHT);
                rTime.setAlignmentX(Component.LEFT_ALIGNMENT);
                rContent.add(rTime);
                rContent.add(Box.createVerticalStrut(3));

                JLabel rText = new JLabel(r.getContent());
                rText.setFont(new Font("微软雅黑", Font.PLAIN, 13));
                rText.setAlignmentX(Component.LEFT_ALIGNMENT);
                rContent.add(rText);

                rp.add(rContent, BorderLayout.CENTER);
                replyList.add(rp);
                replyList.add(Box.createVerticalStrut(6));
            }
        }

        JScrollPane replyScroll = new JScrollPane(replyList);
        replyScroll.setBorder(null);
        replyPanel.add(replyScroll, BorderLayout.CENTER);

        // 回复输入
        JPanel replyInput = new JPanel(new BorderLayout(8, 5));
        replyInput.setBackground(Color.WHITE);
        replyInput.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JTextField txtReply = UIUtil.createTextField(35);
        JButton btnReply = UIUtil.createPrimaryButton("回复");
        btnReply.setPreferredSize(new Dimension(70, 34));
        btnReply.addActionListener(e -> {
            String content = txtReply.getText().trim();
            if (content.isEmpty()) {
                UIUtil.showError(dialog, "请输入回复内容！");
                return;
            }
            ForumReply reply = new ForumReply(actualPost.getId(), currentUser.getId(), content);
            if (forumService.addReply(reply)) {
                UIUtil.showInfo(dialog, "回复成功！");
                dialog.dispose();
                loadPosts(txtSearch.getText().trim(), selectedCategory);
            } else {
                UIUtil.showError(dialog, "回复失败！");
            }
        });
        replyInput.add(txtReply, BorderLayout.CENTER);
        replyInput.add(btnReply, BorderLayout.EAST);
        replyPanel.add(replyInput, BorderLayout.SOUTH);

        mainPanel.add(replyPanel, BorderLayout.CENTER);

        // 关闭按钮
        JButton btnClose = new JButton("关闭");
        btnClose.setPreferredSize(new Dimension(80, 36));
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnClose);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);

        btnClose.addActionListener(e -> {
            dialog.dispose();
            loadPosts(txtSearch.getText().trim(), selectedCategory);
        });

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}

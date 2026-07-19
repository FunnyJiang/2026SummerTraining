package com.bmi.ui.user;

import com.bmi.model.Favorite;
import com.bmi.model.Recipe;
import com.bmi.model.RecipeCategory;
import com.bmi.model.User;
import com.bmi.service.ForumService;
import com.bmi.service.RecipeService;
import com.bmi.util.UIUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 用户收藏管理面板（卡片式布局）
 */
public class FavoritePanel extends JPanel {
    private User currentUser;
    private ForumService forumService = new ForumService();
    private RecipeService recipeService = new RecipeService();
    private JTable table;
    private DefaultTableModel tableModel;

    // 食谱浏览区组件
    private JPanel recipeCardsPanel;
    private JPanel categoryFilterPanel;
    private JTextField txtSearch;
    private int selectedCategoryId = 0;

    public FavoritePanel(User user) {
        this.currentUser = user;
        initUI();
        loadFavorites();
        loadRecipes(null, 0);
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
        JLabel titleLabel = new JLabel("我的收藏");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setForeground(UIUtil.USER_TEXT);
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 上部：食谱浏览区
        JPanel browsePanel = new JPanel(new BorderLayout());
        browsePanel.setBackground(UIUtil.USER_BG);
        browsePanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 12, 24));

        JPanel browseTitlePanel = new JPanel(new BorderLayout());
        browseTitlePanel.setBackground(Color.WHITE);
        browseTitlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));
        JLabel browseTitle = new JLabel("浏览食谱");
        browseTitle.setFont(new Font("微软雅黑", Font.BOLD, 15));
        browseTitle.setForeground(UIUtil.USER_TEXT);
        browseTitlePanel.add(browseTitle, BorderLayout.WEST);

        // 搜索栏
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        searchPanel.add(new JLabel("搜索") {{ setFont(new Font("微软雅黑", Font.PLAIN, 14)); setForeground(UIUtil.USER_TEXT_SECONDARY); }});
        txtSearch = UIUtil.createUserTextField(12);
        searchPanel.add(txtSearch);

        JButton btnSearch = UIUtil.createUserPrimaryButton("搜索");
        btnSearch.setPreferredSize(new Dimension(70, 34));
        btnSearch.addActionListener(e -> loadRecipes(txtSearch.getText().trim(), selectedCategoryId));
        searchPanel.add(btnSearch);

        JButton btnClear = UIUtil.createUserOutlineButton("清空", UIUtil.USER_TEXT_SECONDARY);
        btnClear.setPreferredSize(new Dimension(70, 34));
        btnClear.addActionListener(e -> {
            txtSearch.setText("");
            selectedCategoryId = 0;
            loadCategoryFilter();
            loadRecipes(null, 0);
        });
        searchPanel.add(btnClear);

        // 分类筛选
        categoryFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        categoryFilterPanel.setBackground(Color.WHITE);
        categoryFilterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        loadCategoryFilter();

        // 食谱卡片容器
        recipeCardsPanel = new JPanel(new GridLayout(0, 3, 18, 18));
        recipeCardsPanel.setBackground(UIUtil.USER_BG);
        recipeCardsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JScrollPane cardsScroll = new JScrollPane(recipeCardsPanel);
        cardsScroll.setBorder(BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1));
        cardsScroll.setBackground(UIUtil.USER_BG);
        cardsScroll.getVerticalScrollBar().setUnitIncrement(16);

        // 上部：搜索 + 分类筛选，避免筛选区被撑出大量空白
        JPanel filterPanel = new JPanel(new BorderLayout(0, 0));
        filterPanel.setBackground(UIUtil.USER_BG);
        filterPanel.add(searchPanel, BorderLayout.NORTH);
        filterPanel.add(categoryFilterPanel, BorderLayout.CENTER);

        JPanel centerWrapper = new JPanel(new BorderLayout(0, 12));
        centerWrapper.setBackground(UIUtil.USER_BG);
        centerWrapper.add(filterPanel, BorderLayout.NORTH);
        centerWrapper.add(cardsScroll, BorderLayout.CENTER);

        browsePanel.add(browseTitlePanel, BorderLayout.NORTH);
        browsePanel.add(centerWrapper, BorderLayout.CENTER);

        // 下部：我的收藏列表
        JPanel favPanel = new JPanel(new BorderLayout());
        favPanel.setBackground(UIUtil.USER_BG);
        favPanel.setBorder(BorderFactory.createEmptyBorder(12, 24, 24, 24));

        JPanel favTitlePanel = new JPanel(new BorderLayout());
        favTitlePanel.setBackground(Color.WHITE);
        favTitlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
            BorderFactory.createEmptyBorder(14, 20, 14, 20)
        ));
        JLabel favTitle = new JLabel("我的收藏列表");
        favTitle.setFont(new Font("微软雅黑", Font.BOLD, 15));
        favTitle.setForeground(UIUtil.USER_TEXT);
        favTitlePanel.add(favTitle, BorderLayout.WEST);

        JButton btnRemove = UIUtil.createUserOutlineButton("取消收藏", UIUtil.DANGER_COLOR);
        btnRemove.setPreferredSize(new Dimension(90, 34));
        btnRemove.addActionListener(e -> removeFavorite());
        favTitlePanel.add(btnRemove, BorderLayout.EAST);

        String[] favColumns = {"收藏ID", "食谱名称", "分类", "热量(千卡)", "收藏时间"};
        tableModel = new DefaultTableModel(favColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.getTableHeader().setBackground(new Color(240, 248, 240));
        table.getTableHeader().setForeground(UIUtil.USER_TEXT);
        table.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 13));
        table.setSelectionBackground(new Color(232, 245, 233));

        JScrollPane favScroll = new JScrollPane(table);
        favScroll.setBorder(BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1));
        favScroll.setPreferredSize(new Dimension(0, 180));

        favPanel.add(favTitlePanel, BorderLayout.NORTH);
        favPanel.add(favScroll, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);
        add(browsePanel, BorderLayout.CENTER);
        add(favPanel, BorderLayout.SOUTH);
    }

    private void loadCategoryFilter() {
        categoryFilterPanel.removeAll();

        addCategoryTag("全部", 0);
        List<RecipeCategory> categories = recipeService.findAllCategories();
        for (RecipeCategory c : categories) {
            addCategoryTag(c.getName(), c.getId());
        }

        categoryFilterPanel.revalidate();
        categoryFilterPanel.repaint();
    }

    private void addCategoryTag(String name, int catId) {
        boolean selected = (selectedCategoryId == catId);
        JLabel tag = new JLabel(name, SwingConstants.CENTER);
        tag.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        tag.setOpaque(true);
        tag.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tag.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));

        if (selected) {
            tag.setBackground(UIUtil.USER_PRIMARY);
            tag.setForeground(Color.WHITE);
            tag.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.USER_PRIMARY, 1),
                BorderFactory.createEmptyBorder(4, 13, 4, 13)
            ));
        } else {
            tag.setBackground(Color.WHITE);
            tag.setForeground(UIUtil.USER_TEXT);
            tag.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtil.USER_BORDER, 1),
                BorderFactory.createEmptyBorder(4, 13, 4, 13)
            ));
        }

        tag.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedCategoryId = catId;
                loadCategoryFilter();
                loadRecipes(txtSearch.getText().trim(), catId);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedCategoryId != catId) tag.setBackground(UIUtil.USER_SIDEBAR_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedCategoryId != catId) tag.setBackground(Color.WHITE);
            }
        });

        categoryFilterPanel.add(tag);
    }

    private void loadRecipes(String keyword, int categoryId) {
        recipeCardsPanel.removeAll();

        List<Recipe> list = recipeService.searchRecipes(keyword, categoryId);

        if (list.isEmpty()) {
            JLabel empty = new JLabel("暂无符合的食谱", SwingConstants.CENTER);
            empty.setFont(new Font("微软雅黑", Font.PLAIN, 14));
            empty.setForeground(UIUtil.TEXT_LIGHT);
            recipeCardsPanel.add(empty);
        } else {
            for (Recipe r : list) {
                recipeCardsPanel.add(createRecipeCard(r));
            }
        }

        recipeCardsPanel.revalidate();
        recipeCardsPanel.repaint();
    }

    private JPanel createRecipeCard(Recipe r) {
        JPanel card = UIUtil.createUserCardPanel(12);
        card.setLayout(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(220, 280));
        card.setMaximumSize(new Dimension(280, Integer.MAX_VALUE));

        // 顶部图标区
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(248, 250, 252));
        top.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        top.setPreferredSize(new Dimension(0, 100));

        // 程序绘制的简化食物图标 (代替 emoji, 跨平台一致)
        JPanel iconWrap = new JPanel(new GridBagLayout());
        iconWrap.setOpaque(false);
        iconWrap.add(UIUtil.createFoodIcon(r.getName(), 56));
        top.add(iconWrap, BorderLayout.CENTER);

        JLabel calorieBadge = UIUtil.createCalorieBadge(r.getCalories());
        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        badgeWrap.setOpaque(false);
        badgeWrap.add(calorieBadge);
        top.add(badgeWrap, BorderLayout.NORTH);

        card.add(top, BorderLayout.NORTH);

        // 中部
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(Color.WHITE);
        center.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

        JLabel nameLabel = new JLabel(r.getName());
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        nameLabel.setForeground(UIUtil.TEXT_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(nameLabel);
        center.add(Box.createVerticalStrut(4));

        String desc = r.getDescription() != null ? r.getDescription() : "";
        // 不再截断, 用 HTML 自动换行显示完整描述
        JLabel descLabel = new JLabel("<html><body style='width:170px'>" + desc + "</body></html>");
        descLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        descLabel.setForeground(UIUtil.TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(descLabel);
        center.add(Box.createVerticalStrut(6));

        // 标签
        JPanel tagPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        tagPanel.setBackground(Color.WHITE);
        tagPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        Color[] colors = UIUtil.getCategoryTagColors(r.getCategoryName());
        tagPanel.add(UIUtil.createTagLabel(r.getCategoryName() != null ? r.getCategoryName() : "未分类", colors[0], colors[1]));
        if (r.getProtein() >= 15) {
            tagPanel.add(UIUtil.createTagLabel("高蛋白", UIUtil.TAG_ORANGE_BG, UIUtil.TAG_ORANGE_TEXT));
        }
        if (r.getCalories() <= 200) {
            tagPanel.add(UIUtil.createTagLabel("低卡", UIUtil.TAG_GREEN_BG, UIUtil.TAG_GREEN_TEXT));
        }
        center.add(tagPanel);
        card.add(center, BorderLayout.CENTER);

        // 底部：收藏按钮
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            JButton btnFav = UIUtil.createUserPrimaryButton("+ 收藏");
            btnFav.setFont(new Font("微软雅黑", Font.PLAIN, 13));
            btnFav.setPreferredSize(new Dimension(80, 32));
            btnFav.addActionListener(e -> addFavorite(r.getId(), r.getName()));
        bottom.add(btnFav, BorderLayout.EAST);

        card.add(bottom, BorderLayout.SOUTH);
        return card;
    }

    private void loadFavorites() {
        tableModel.setRowCount(0);
        List<Favorite> list = forumService.findFavoritesByUserId(currentUser.getId());
        for (Favorite f : list) {
            tableModel.addRow(new Object[]{
                f.getId(), f.getRecipeName(), f.getCategoryName(),
                f.getCalories(), f.getCreateTimeStr()
            });
        }
    }

    private void addFavorite(int recipeId, String recipeName) {
        if (forumService.isFavorited(currentUser.getId(), recipeId)) {
            UIUtil.showInfo(this, "该食谱已在您的收藏列表中！");
            return;
        }

        if (forumService.addFavorite(currentUser.getId(), recipeId)) {
            UIUtil.showInfo(this, "收藏成功！");
            loadFavorites();
        } else {
            UIUtil.showError(this, "收藏失败！");
        }
    }

    private void removeFavorite() {
        int row = table.getSelectedRow();
        if (row < 0) {
            UIUtil.showError(this, "请选择要取消收藏的食谱！");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        if (UIUtil.showConfirm(this, "确定要取消收藏吗？")) {
            if (forumService.deleteFavorite(id)) {
                UIUtil.showInfo(this, "已取消收藏！");
                loadFavorites();
            } else {
                UIUtil.showError(this, "操作失败！");
            }
        }
    }
}

package com.mooyeh.mytool.view;

import org.springframework.beans.factory.annotation.Autowired;
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;

public class LeftPanel extends JPanel {
    private JTree menuTree;
    private final Base64Panel base64Panel;
    private final JsonPanel jsonPanel;
    private JPanel rightPanel;
    private JLabel titleLabel;
    
    // 与右侧一致
    private static final Color BACKGROUND_COLOR = null;
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color SELECTED_COLOR = new Color(255, 0, 0);
    private static final Color HOVER_COLOR = new Color(53, 53, 53);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Color SELECTED_TEXT_COLOR = new Color(51, 51,255);
    
    @Autowired
    public LeftPanel(Base64Panel base64Panel, JsonPanel jsonPanel) {
        this.base64Panel = base64Panel;
        this.jsonPanel = jsonPanel;
        
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 0));
        setBackground(BACKGROUND_COLOR);
        
        initTree();
    }
    
    public void setRightPanel(JPanel rightPanel) {
        this.rightPanel = rightPanel;
        
        // 创建标题标签
        titleLabel = new JLabel("欢迎使用", JLabel.LEFT);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // 将标题标签添加到右侧面板的顶部
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.add(titleLabel, BorderLayout.WEST);
        
        // 用CardLayout包装原内容
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        for (Component comp : rightPanel.getComponents()) {
            contentPanel.add(comp, "welcome");
        }
        
        // 清空右侧面板并添加新的布局
        rightPanel.removeAll();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(topPanel, BorderLayout.NORTH);
        rightPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void initTree() {
        // 创建树的根节点
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("工具箱");
        
        // 创建工具分类
        String[] categories = {"编解码器", "文本工具", "网络工具"};
        String[][] tools = {
            {"Base64编解码", "URL编解码", "MD5加密"},
            {"文本对比", "文本格式化", "JSON工具", "正则测试"},
            {"Ping测试", "端口扫描", "DNS查询"}
        };
        
        // 添加分类和工具到树中
        for (int i = 0; i < categories.length; i++) {
            DefaultMutableTreeNode category = new DefaultMutableTreeNode(categories[i]);
            root.add(category);
            
            for (String tool : tools[i]) {
                category.add(new DefaultMutableTreeNode(tool));
            }
        }
        
        // 创建树
        menuTree = new JTree(root);
        menuTree.setBackground(BACKGROUND_COLOR);
        menuTree.setForeground(TEXT_COLOR);
        
        // 设置树的外观
        menuTree.setRootVisible(false);
        menuTree.setShowsRootHandles(true);
        menuTree.setToggleClickCount(1); // 单击展开/折叠
        menuTree.setRowHeight(24); // 增加行高
        
        // 自定义树节点渲染器
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            public Component getTreeCellRendererComponent(JTree tree, Object value,
                    boolean selected, boolean expanded, boolean leaf, int row,
                    boolean hasFocus) {
                super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
                setBackgroundNonSelectionColor(null);
                setOpaque(true);
                
                // 只有叶子节点（工具项）在选中时改变颜色
                if (leaf) {
                    setForeground(selected ? SELECTED_TEXT_COLOR : TEXT_COLOR);
                } else {
                    setForeground(TEXT_COLOR); // 非叶子节点（分类）始终使用默认颜色
                }

                // 设置图标
                if (leaf) {
                    setIcon(UIManager.getIcon("FileView.fileIcon"));
                } else if (expanded) {
                    setIcon(UIManager.getIcon("Tree.openIcon"));
                } else {
                    setIcon(UIManager.getIcon("Tree.closedIcon"));
                }
                return this;
            }
        };
        
        // 设置渲染器
        menuTree.setCellRenderer(renderer);
        
        // 添加树的选择监听器
        menuTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                menuTree.getLastSelectedPathComponent();
            
            if (node != null && node.isLeaf()) {
                String tool = node.toString();
                switch (tool) {
                    case "Base64编解码":
                        showBase64Panel();
                        break;
                    case "JSON工具":
                        showJsonPanel();
                        break;
                    default:
                        showWelcomePanel();
                        break;
                }
            }
        });
        
        // 添加鼠标监听器来改变光标和背景色
        menuTree.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                TreePath path = menuTree.getPathForLocation(e.getX(), e.getY());
                if (path != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                        path.getLastPathComponent();
                    if (node.isLeaf()) {
                        menuTree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        menuTree.setCursor(Cursor.getDefaultCursor());
                    }
                } else {
                    menuTree.setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        
        // 将树添加到滚动面板中
        JScrollPane scrollPane = new JScrollPane(menuTree);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void showWelcomePanel() {
        titleLabel.setText("欢迎使用");
        JPanel contentPanel = (JPanel)rightPanel.getComponent(1);
        ((CardLayout)contentPanel.getLayout()).show(contentPanel, "welcome");
    }
    
    private void showBase64Panel() {
        titleLabel.setText("Base64编解码");
        JPanel contentPanel = (JPanel)rightPanel.getComponent(1);
        if (contentPanel.getComponentCount() < 2) {
            contentPanel.add(base64Panel, "base64");
        }
        ((CardLayout)contentPanel.getLayout()).show(contentPanel, "base64");
    }
    
    private void showJsonPanel() {
        titleLabel.setText("JSON工具");
        JPanel contentPanel = (JPanel)rightPanel.getComponent(1);
        if (contentPanel.getComponentCount() < 3) {
            contentPanel.add(jsonPanel, "json");
        }
        ((CardLayout)contentPanel.getLayout()).show(contentPanel, "json");
    }
} 
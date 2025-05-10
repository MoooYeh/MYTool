package com.mooyeh.mytool.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;

@Component
public class MainFrame extends JFrame {
    private final LeftPanel leftPanel;
    private final WelcomePanel welcomePanel;
    private final JPanel rightPanel;
    
    // 颜色
    private static final Color BACKGROUND_COLOR = null;
    private static final Color BORDER_COLOR = new Color(60, 60, 60);
    private static final Color SELECTED_COLOR = new Color(62, 62, 62);
    private static final Color HOVER_COLOR = new Color(53, 53, 53);
    
    @Autowired
    public MainFrame(LeftPanel leftPanel, WelcomePanel welcomePanel) {
        this.leftPanel = leftPanel;
        this.welcomePanel = welcomePanel;
        this.rightPanel = new JPanel(new CardLayout());
        
        // 设置全局UI属性
        setUIFont(new FontUIResource("Dialog", Font.PLAIN, 12));
        UIManager.put("Panel.background", BACKGROUND_COLOR);
        UIManager.put("SplitPane.background", BACKGROUND_COLOR);
        UIManager.put("SplitPane.dividerSize", 1);
        UIManager.put("SplitPaneDivider.draggingColor", BORDER_COLOR);
        
        initFrame();
        initMenuBar();
        initMainPanel();
    }
    
    private void setUIFont(FontUIResource f) {
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }
    
    private void initFrame() {
        setTitle("MYTool");
        setSize(1024, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
    }
    
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(null);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        
        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        fileMenu.setForeground(Color.WHITE);
        fileMenu.setBackground(null);
        JMenuItem exitItem = new JMenuItem("退出");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setForeground(Color.WHITE);
        helpMenu.setBackground(null);
        JMenuItem aboutItem = new JMenuItem("关于");
        aboutItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "MYTool v1.0\n一个简单的工具集合",
                "关于",
                JOptionPane.INFORMATION_MESSAGE);
        });
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void initMainPanel() {
        // 创建主面板，使用BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // 添加欢迎面板到右侧面板
        rightPanel.add(welcomePanel, "welcome");
        rightPanel.setBackground(BACKGROUND_COLOR);
        
        // 设置右侧面板的边框
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 1, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 设置左侧面板的右侧面板引用
        leftPanel.setRightPanel(rightPanel);
        
        // 创建分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(250); // 设置初始分割位置
        splitPane.setDividerSize(5); // 设置分割条宽度
        splitPane.setOneTouchExpandable(false); // 禁用快速展开/折叠按钮
        splitPane.setResizeWeight(0.0); // 固定左侧面板宽度
        splitPane.setContinuousLayout(true); // 启用连续布局更新
        splitPane.setBorder(null); // 移除分割面板的边框
        
        // 将分割面板添加到主面板
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // 将主面板添加到窗口
        add(mainPanel);
    }
} 
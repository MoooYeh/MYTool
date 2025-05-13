package com.mooyeh.mytool.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
        JMenuItem downloadFFmpegItem = new JMenuItem("下载 FFmpeg");
        downloadFFmpegItem.addActionListener(e -> showFFmpegDownloadInfo());
        helpMenu.add(downloadFFmpegItem);
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
    
    private void showFFmpegDownloadInfo() {
        String os = System.getProperty("os.name").toLowerCase();
        String downloadUrl;
        String fileName;
        
        if (os.contains("windows")) {
            downloadUrl = "https://www.alipan.com/s/NVqd9rAgPcT";
            fileName = "ffmpeg.exe";
        } else if (os.contains("mac")) {
            downloadUrl = "https://www.alipan.com/s/N61hVqCDmYu";
            fileName = "ffmpeg";
        } else {
            JOptionPane.showMessageDialog(this, 
                "不支持的操作系统: " + os, 
                "错误", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 获取程序运行目录
        String programDir = System.getProperty("user.dir");
        
        // 构建提示信息
        StringBuilder message = new StringBuilder();
        message.append("请按照以下步骤下载并安装 FFmpeg：\n\n");
        message.append("1. 点击下方链接下载 FFmpeg：\n");
        message.append(downloadUrl).append("\n\n");
        message.append("2. 下载完成后，请将文件重命名为：").append(fileName).append("\n");
        message.append("3. 将文件放到程序目录：\n");
        message.append(programDir).append("\n\n");
        message.append("4. 如果是 Mac 系统，请在终端中执行以下命令设置权限：\n");
        message.append("chmod +x ").append(programDir).append("/").append(fileName).append("\n\n");
        message.append("完成以上步骤后，程序将自动识别并使用 FFmpeg。");

        // 创建对话框
        JDialog dialog = new JDialog(this, "FFmpeg 下载说明", true);
        dialog.setLayout(new BorderLayout());
        
        // 创建文本区域
        JTextArea textArea = new JTextArea(message.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(dialog.getBackground());
        textArea.setFont(new Font("Dialog", Font.PLAIN, 12));
        
        // 创建滚动面板
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton copyButton = new JButton("复制下载链接");
        JButton closeButton = new JButton("关闭");
        
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(downloadUrl);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            JOptionPane.showMessageDialog(dialog,
                "下载链接已复制到剪贴板",
                "提示",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(copyButton);
        buttonPanel.add(closeButton);
        
        // 添加组件到对话框
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 显示对话框
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }
} 
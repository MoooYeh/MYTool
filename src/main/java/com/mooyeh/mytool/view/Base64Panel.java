package com.mooyeh.mytool.view;

import com.mooyeh.mytool.controller.Base64Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@Component
public class Base64Panel extends JPanel {
    private JTextArea inputArea;
    private JTextArea outputArea;
    private final Base64Controller base64Controller;
    private ButtonGroup charsetGroup;
    private JRadioButton utf8Button;
    private JRadioButton asciiButton;
    
    @Autowired
    public Base64Panel(Base64Controller base64Controller) {
        this.base64Controller = base64Controller;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 创建顶部控制面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.setBackground(null);
        
        // 创建字符集选择
        JLabel charsetLabel = new JLabel("字符集：");
        charsetGroup = new ButtonGroup();
        utf8Button = new JRadioButton("UTF-8 编码", true);
        asciiButton = new JRadioButton("ASCII 编码");
        charsetGroup.add(utf8Button);
        charsetGroup.add(asciiButton);
        
        // 创建按钮
        JButton encodeButton = new JButton("编码");
        JButton decodeButton = new JButton("解码");
        
        // 添加组件到顶部面板
        topPanel.add(charsetLabel);
        topPanel.add(utf8Button);
        topPanel.add(asciiButton);
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(encodeButton);
        topPanel.add(decodeButton);
        
        // 创建输入区域
        inputArea = new JTextArea();
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder("输入"));
        
        // 创建输出区域
        outputArea = new JTextArea();
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        JScrollPane outputScrollPane = new JScrollPane(outputArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("输出"));
        
        // 添加编码解码功能
        encodeButton.addActionListener(e -> {
            try {
                String input = inputArea.getText();
                String charset = utf8Button.isSelected() ? "UTF-8" : "ASCII";
                String encoded = base64Controller.encode(input, charset);
                outputArea.setText(encoded);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        decodeButton.addActionListener(e -> {
            try {
                String input = inputArea.getText();
                String charset = utf8Button.isSelected() ? "UTF-8" : "ASCII";
                String decoded = base64Controller.decode(input, charset);
                outputArea.setText(decoded);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 使用JSplitPane分割输入和输出区域
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            inputScrollPane, outputScrollPane);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        
        // 添加所有组件到面板
        add(topPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
    }
} 
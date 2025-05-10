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
    
    @Autowired
    public Base64Panel(Base64Controller base64Controller) {
        this.base64Controller = base64Controller;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
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
        
        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton encodeButton = new JButton("编码");
        JButton decodeButton = new JButton("解码");
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);
        
        // 添加编码解码功能
        encodeButton.addActionListener(e -> {
            try {
                String input = inputArea.getText();
                String encoded = base64Controller.encode(input);
                outputArea.setText(encoded);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        decodeButton.addActionListener(e -> {
            try {
                String input = inputArea.getText();
                String decoded = base64Controller.decode(input);
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
        add(splitPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
} 
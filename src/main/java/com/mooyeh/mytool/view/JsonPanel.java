package com.mooyeh.mytool.view;

import com.mooyeh.mytool.controller.JsonController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.*;

@Component
public class JsonPanel extends JPanel {
    private RSyntaxTextArea inputArea;
    private RSyntaxTextArea outputArea;
    private final JsonController jsonController;
    private JLabel statusLabel;

    @Autowired
    public JsonPanel(JsonController jsonController) {
        this.jsonController = jsonController;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建输入区域
        inputArea = new RSyntaxTextArea();
        inputArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        inputArea.setCodeFoldingEnabled(true);
        inputArea.setAntiAliasingEnabled(true);
        inputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        inputArea.setBracketMatchingEnabled(true);
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/default.xml"));
            theme.apply(inputArea);
        } catch (Exception ex) {
            // 主题加载失败忽略
        }
        RTextScrollPane inputScrollPane = new RTextScrollPane(inputArea);
        inputScrollPane.setBorder(BorderFactory.createTitledBorder("输入 JSON"));

        // 创建输出区域
        outputArea = new RSyntaxTextArea();
        outputArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        outputArea.setCodeFoldingEnabled(true);
        outputArea.setAntiAliasingEnabled(true);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setBracketMatchingEnabled(true);
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/org/fife/ui/rsyntaxtextarea/themes/default.xml"));
            theme.apply(outputArea);
        } catch (Exception ex) {
            // 主题加载失败忽略
        }
        RTextScrollPane outputScrollPane = new RTextScrollPane(outputArea);
        outputScrollPane.setBorder(BorderFactory.createTitledBorder("输出 JSON"));

        // 创建状态标签
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JButton formatButton = new JButton("格式化");
        JButton compressButton = new JButton("压缩");
        JButton validateButton = new JButton("验证");
        buttonPanel.add(formatButton);
        buttonPanel.add(compressButton);
        buttonPanel.add(validateButton);
        buttonPanel.add(statusLabel);

        // 格式化按钮：先校验，校验通过再格式化，否则提示
        formatButton.addActionListener(e -> {
            String input = inputArea.getText();
            JsonController.JsonValidationResult result = jsonController.validate(input);
            if (result.isValid()) {
                try {
                    String formatted = jsonController.format(input);
                    outputArea.setText(formatted);
                    outputArea.setCaretPosition(0);
                    statusLabel.setText("JSON 格式正确");
                    statusLabel.setForeground(new Color(0, 128, 0));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                statusLabel.setText("JSON 格式错误: " + result.getErrorMessage());
                statusLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "JSON 格式错误: " + result.getErrorMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                if (result.getErrorOffset() >= 0) {
                    try {
                        inputArea.setCaretPosition(result.getErrorOffset());
                        int start = result.getErrorOffset();
                        int end = Math.min(start + 1, inputArea.getText().length());
                        inputArea.select(start, end);
                        inputArea.setSelectionColor(new Color(255, 0, 0, 128)); // 半透明红色
                        inputArea.requestFocus();
                    } catch (Exception ex) {}
                }
            }
        });

        // 压缩按钮：同样先校验
        compressButton.addActionListener(e -> {
            String input = inputArea.getText();
            JsonController.JsonValidationResult result = jsonController.validate(input);
            if (result.isValid()) {
                try {
                    String compressed = jsonController.compress(input);
                    outputArea.setText(compressed);
                    outputArea.setCaretPosition(0);
                    statusLabel.setText("JSON 格式正确");
                    statusLabel.setForeground(new Color(0, 128, 0));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                statusLabel.setText("JSON 格式错误: " + result.getErrorMessage());
                statusLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "JSON 格式错误: " + result.getErrorMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                if (result.getErrorOffset() >= 0) {
                    try {
                        inputArea.setCaretPosition(result.getErrorOffset());
                        int start = result.getErrorOffset();
                        int end = Math.min(start + 1, inputArea.getText().length());
                        inputArea.select(start, end);
                        inputArea.setSelectionColor(new Color(255, 0, 0, 128)); // 半透明红色
                        inputArea.requestFocus();
                    } catch (Exception ex) {}
                }
            }
        });

        // 验证按钮：只做校验
        validateButton.addActionListener(e -> {
            String input = inputArea.getText();
            JsonController.JsonValidationResult result = jsonController.validate(input);
            if (result.isValid()) {
                statusLabel.setText("JSON 格式正确");
                statusLabel.setForeground(new Color(0, 128, 0));
            } else {
                statusLabel.setText("JSON 格式错误: " + result.getErrorMessage());
                statusLabel.setForeground(Color.RED);
                JOptionPane.showMessageDialog(this, "JSON 格式错误: " + result.getErrorMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                if (result.getErrorOffset() >= 0) {
                    try {
                        inputArea.setCaretPosition(result.getErrorOffset());
                        int start = result.getErrorOffset();
                        int end = Math.min(start + 1, inputArea.getText().length());
                        inputArea.select(start, end);
                        inputArea.setSelectionColor(new Color(255, 0, 0, 128)); // 半透明红色
                        inputArea.requestFocus();
                    } catch (Exception ex) {}
                }
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
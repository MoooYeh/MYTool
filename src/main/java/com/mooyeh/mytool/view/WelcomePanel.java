package com.mooyeh.mytool.view;

import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;

@Component
public class WelcomePanel extends JPanel {
    
    public WelcomePanel() {
        setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("欢迎使用MYTool", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }
} 
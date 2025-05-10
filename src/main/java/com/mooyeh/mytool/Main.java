package com.mooyeh.mytool;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.mooyeh.mytool.config.AppConfig;
import com.mooyeh.mytool.view.MainFrame;

public class Main {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        MainFrame mainFrame = context.getBean(MainFrame.class);
        mainFrame.setVisible(true);
    }
} 
package com.mooyeh.mytool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import com.mooyeh.mytool.view.LeftPanel;
import com.mooyeh.mytool.view.Base64Panel;
import com.mooyeh.mytool.controller.Base64Controller;

@Configuration
@ComponentScan("com.mooyeh.mytool")
public class AppConfig {
    @Bean
    public Base64Controller base64Controller() {
        return new Base64Controller();
    }

    @Bean
    public Base64Panel base64Panel(Base64Controller base64Controller) {
        return new Base64Panel(base64Controller);
    }

    @Bean
    public LeftPanel leftPanel(Base64Panel base64Panel) {
        return new LeftPanel(base64Panel);
    }
} 
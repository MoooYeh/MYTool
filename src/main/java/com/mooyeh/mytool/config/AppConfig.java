package com.mooyeh.mytool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;
import com.mooyeh.mytool.view.LeftPanel;
import com.mooyeh.mytool.view.Base64Panel;
import com.mooyeh.mytool.view.JsonPanel;
import com.mooyeh.mytool.controller.Base64Controller;
import com.mooyeh.mytool.controller.JsonController;

@Configuration
@ComponentScan("com.mooyeh.mytool")
public class AppConfig {
    @Bean
    public Base64Controller base64Controller() {
        return new Base64Controller();
    }

    @Bean
    public JsonController jsonController() {
        return new JsonController();
    }

    @Bean
    public Base64Panel base64Panel(Base64Controller base64Controller) {
        return new Base64Panel(base64Controller);
    }

    @Bean
    public JsonPanel jsonPanel(JsonController jsonController) {
        return new JsonPanel(jsonController);
    }

    @Bean
    public LeftPanel leftPanel(Base64Panel base64Panel, JsonPanel jsonPanel) {
        return new LeftPanel(base64Panel, jsonPanel);
    }
} 
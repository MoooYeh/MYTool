package com.mooyeh.mytool.controller;

import org.springframework.stereotype.Component;
import java.util.Base64;

@Component
public class Base64Controller {
    
    public String encode(String input) {
        try {
            return Base64.getEncoder().encodeToString(input.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("编码失败: " + e.getMessage());
        }
    }
    
    public String decode(String input) {
        try {
            byte[] decoded = Base64.getDecoder().decode(input);
            return new String(decoded);
        } catch (Exception e) {
            throw new RuntimeException("解码失败: " + e.getMessage());
        }
    }
} 
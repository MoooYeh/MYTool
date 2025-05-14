package com.mooyeh.mytool.controller;

import java.util.Base64;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Base64Controller {
    
    public String encode(String input, String charset) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            throw new Exception("输入不能为空");
        }
        
        Charset selectedCharset = charset.equals("UTF-8") ? 
            StandardCharsets.UTF_8 : StandardCharsets.US_ASCII;
            
        byte[] bytes = input.getBytes(selectedCharset);
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public String decode(String input, String charset) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            throw new Exception("输入不能为空");
        }
        
        try {
            Charset selectedCharset = charset.equals("UTF-8") ? 
                StandardCharsets.UTF_8 : StandardCharsets.US_ASCII;
                
            byte[] decodedBytes = Base64.getDecoder().decode(input.trim());
            return new String(decodedBytes, selectedCharset);
        } catch (IllegalArgumentException e) {
            throw new Exception("无效的Base64编码");
        }
    }
} 
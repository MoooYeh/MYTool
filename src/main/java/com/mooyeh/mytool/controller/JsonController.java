package com.mooyeh.mytool.controller;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonLocation;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class JsonController {
    private final ObjectMapper objectMapper;
    
    public JsonController() {
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    public String format(String input) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            throw new Exception("JSON 不能为空");
        }
        
        Object jsonObject = objectMapper.readValue(input, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }
    
    public String compress(String input) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            throw new Exception("JSON 不能为空");
        }
        
        Object jsonObject = objectMapper.readValue(input, Object.class);
        return objectMapper.writeValueAsString(jsonObject);
    }

    public JsonValidationResult validate(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new JsonValidationResult(false, "JSON 不能为空", -1);
        }
        
        try {
            objectMapper.readTree(input);
            return new JsonValidationResult(true, null, -1);
        } catch (Exception e) {
            String message = e.getMessage();
            int offset = -1;
            
            // 尝试从错误消息中提取位置信息
            if (message != null) {
                int posIndex = message.indexOf(" at [Source:");
                if (posIndex > 0) {
                    message = message.substring(0, posIndex);
                    // 尝试提取位置数字
                    try {
                        String posStr = message.substring(message.lastIndexOf(" ") + 1);
                        offset = Integer.parseInt(posStr);
                    } catch (NumberFormatException ex) {
                        // 忽略解析错误
                    }
                }
            }
            
            return new JsonValidationResult(false, message, offset);
        }
    }

    public static class JsonValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final int errorOffset;
        
        public JsonValidationResult(boolean valid, String errorMessage, int errorOffset) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.errorOffset = errorOffset;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public int getErrorOffset() {
            return errorOffset;
        }
    }
} 
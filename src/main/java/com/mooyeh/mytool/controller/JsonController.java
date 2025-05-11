package com.mooyeh.mytool.controller;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonLocation;

@Component
public class JsonController {
    private final ObjectMapper objectMapper;
    
    public JsonController() {
        this.objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }
    
    public String format(String input) {
        try {
            // 先解析 JSON 字符串为对象
            Object jsonObject = objectMapper.readValue(input, Object.class);
            // 再格式化输出
            return objectMapper.writeValueAsString(jsonObject);
        } catch (Exception e) {
            throw new RuntimeException("JSON 格式化失败: " + e.getMessage());
        }
    }
    
    public String compress(String input) {
        try {
            // 先解析 JSON 字符串为对象
            Object jsonObject = objectMapper.readValue(input, Object.class);
            // 禁用缩进输出
            objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
            String compressed = objectMapper.writeValueAsString(jsonObject);
            // 重新启用缩进输出
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            return compressed;
        } catch (Exception e) {
            throw new RuntimeException("JSON 压缩失败: " + e.getMessage());
        }
    }

    public JsonValidationResult validate(String input) {
        try {
            objectMapper.readValue(input, Object.class);
            return new JsonValidationResult(true, null, -1, -1);
        } catch (JsonParseException e) {
            JsonLocation location = e.getLocation();
            return new JsonValidationResult(false, e.getMessage(), 
                (int)location.getCharOffset(), 
                (int)location.getColumnNr());
        } catch (Exception e) {
            return new JsonValidationResult(false, e.getMessage(), -1, -1);
        }
    }

    public static class JsonValidationResult {
        private final boolean valid;
        private final String errorMessage;
        private final int errorOffset;
        private final int errorColumn;

        public JsonValidationResult(boolean valid, String errorMessage, int errorOffset, int errorColumn) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.errorOffset = errorOffset;
            this.errorColumn = errorColumn;
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

        public int getErrorColumn() {
            return errorColumn;
        }
    }
} 
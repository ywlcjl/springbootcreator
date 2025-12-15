package com.example.springbootcreator.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FreemarkerUtils {
    // 默认的日期时间格式
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getMyCode() {
        return "abc";
    }
// ----------------------------------------------------
    // 2. 重载方法（1个参数），使用默认格式
    // ----------------------------------------------------
    /**
     * 将 LocalDateTime 格式化为默认格式 ("yyyy-MM-dd HH:mm:ss") 的字符串。
     * * @param dateTime 要格式化的 LocalDateTime 对象。
     * @return 格式化后的日期时间字符串。
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        // 调用下面的主要方法，传入默认 pattern
        return formatDateTime(dateTime, DEFAULT_PATTERN);
    }

    // ----------------------------------------------------
    // 1. 主要方法（2个参数），处理实际格式化逻辑
    // ----------------------------------------------------
    /**
     * 将 LocalDateTime 格式化为指定的字符串格式。
     * * @param dateTime 要格式化的 LocalDateTime 对象。
     * @param pattern  日期时间格式字符串，例如 "yyyy-MM-dd HH:mm:ss"。
     * @return 格式化后的日期时间字符串，如果输入为 null 则返回空字符串。
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        // 如果 pattern 为 null 或空，使用默认值
        String finalPattern = (pattern == null || pattern.trim().isEmpty()) ? DEFAULT_PATTERN : pattern;

        if (dateTime == null) {
            return "";
        }

        try {
            // 根据最终的 finalPattern 创建 DateTimeFormatter
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(finalPattern);
            return dateTime.format(formatter);
        } catch (Exception e) {
            System.err.println("日期格式化失败，pattern: " + finalPattern + ", 错误: " + e.getMessage());
            return "Format Error";
        }
    }

    public static String getImageThumb(String originalPath, String size) {
        return CommonUtils.getImageSizePath(originalPath, size);
    }

}

package com.example.springbootcreator.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 安全工具类：用于输入参数的安全过滤和清洗 (Sanitization)。
 */
public class SecurityUtils {
    /**
     * 清洗字符串，移除潜在的 XSS 攻击向量，例如 <script> 标签。
     * 注意：这只是一个简单的示例过滤，对于生产级应用，应该使用更专业的库如 OWASP Java HTML Sanitizer。
     *
     * @param value 待清洗的字符串
     * @return 清洗后的字符串
     */
    public static String sanitizeInput(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        // 1. 移除前后的空白字符
        String sanitized = value.trim();

        // 2. 简单的 HTML/XSS 过滤
        sanitized = sanitized.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        sanitized = sanitized.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        sanitized = sanitized.replaceAll("'", "&#39;");
        sanitized = sanitized.replaceAll("eval\\((.*)\\)", "");
        sanitized = sanitized.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");

        // 3. 移除常见的脚本标签 (不区分大小写)
        sanitized = sanitized.replaceAll("(?i)<script.*?>.*?</script.*?>", "");

        return sanitized;
    }

    /**
     * 生成一个随机的 Salt (16字符)。
     * @return 16字符的随机字符串
     */
    public static String generateSalt() {
        // 生成一个 UUID，去除连字符后截取前16位
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    /**
     * 验证密码是否匹配。
     * @param rawPassword 用户输入的原始密码
     * @param storedHashedPassword 数据库中存储的哈希密码
     * @param salt 数据库中存储的盐值
     * @return 匹配返回 true, 否则 false
     */
    public static boolean verifyPassword(String rawPassword, String storedHashedPassword, String salt) {
        String hashedAttempt = sha1Hash(rawPassword+salt);
        // 比较计算出的哈希值和存储的哈希值
        return hashedAttempt != null && hashedAttempt.equals(storedHashedPassword);
    }

    // 将输入字符串进行 SHA-1 哈希
    public static String sha1Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] array = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            //throw new RuntimeException("SHA-1 Hashing failed due to missing algorithm.", e);
            System.err.println("SHA-1 algorithm not available: " + e.getMessage());
            return null;
        }
    }

    // JPEG/JPG 魔数 (至少需要读取前 3 个字节: FF D8 FF)
    private static final byte[] JPEG_MAGIC = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};

    // PNG 魔数 (需要读取前 8 个字节)
    private static final byte[] PNG_MAGIC = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A};

    /**
     * 校验文件是否为真实的 JPEG, JPG, 或 PNG 格式。
     *
     * @param file 上传的 MultipartFile 对象
     * @return 如果是真实的图像文件返回 true，否则返回 false
     */
    public static boolean isActualImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        try (InputStream is = file.getInputStream()) {

            // 读取文件开头的 8 个字节，这是 PNG 校验所需要的最大长度
            byte[] header = new byte[8];
            if (is.read(header) < 3) { // 至少要能读取 3 个字节 (JPEG 最小要求)
                return false;
            }

            // 1. 校验 PNG
            if (Arrays.equals(header, PNG_MAGIC)) {
                return true;
            }

            // 2. 校验 JPEG/JPG
            // 只比较前 3 个字节
            if (header[0] == JPEG_MAGIC[0] && header[1] == JPEG_MAGIC[1] && header[2] == JPEG_MAGIC[2]) {
                return true;
            }

            // 如果文件头不匹配任何已知图像格式，则校验失败
            return false;

        } catch (IOException e) {
            System.err.println("读取文件头失败: " + e.getMessage());
            return false;
        }
    }

}

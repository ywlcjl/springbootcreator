package com.example.springbootcreator.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonUtils {

    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将字符串转换为 LocalDateTime。
     * 严格使用 "yyyy-MM-dd HH:mm:ss" 格式进行解析。
     *
     * @param dateStr 待转换的日期时间字符串
     * @return 转换后的 LocalDateTime 对象，如果失败或为空则返回 null
     */
    public static LocalDateTime stringToLocalDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(dateStr.trim(), DEFAULT_FORMATTER);

        } catch (DateTimeParseException e) {
            System.err.println("日期解析失败，格式不匹配 [yyyy-MM-dd HH:mm:ss]: " + dateStr);
            return null;
        }
    }

    public static Map<String, String> mapKeyIntegerToString(Map<Integer, String> integerKeyMap) {
        if (integerKeyMap == null) {
            return new HashMap<>();
        }

        Map<String, String> stringKeyMap = new HashMap<>(integerKeyMap.size());

        for (Map.Entry<Integer, String> entry : integerKeyMap.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

            String newKey = Objects.toString(key, null);

            if (newKey != null && value != null) {
                stringKeyMap.put(newKey, value);
            }
        }

        return stringKeyMap;
    }

    public static String getImageSizePath(String originalPath) {
        return getImageSizePath(originalPath, "thumb");
    }
    /**
     * 返回缩略图路径
     *
     * @param originalPath
     * @param size
     * @return
     */
    public static String getImageSizePath(String originalPath, String size) {

        Objects.requireNonNull(originalPath, "Original path cannot be null");

        // 如果尺寸标识符为空或 null，直接返回原路径
        if (size == null || size.trim().isEmpty()) {
            return originalPath;
        }

        // 查找文件扩展名（最后一个 '.' 的位置）
        int lastDotIndex = originalPath.lastIndexOf('.');

        // 检查路径中是否包含文件扩展名
        if (lastDotIndex == -1) {
            // 如果路径没有扩展名（例如，它是一个目录名或没有后缀），直接返回原路径
            return originalPath;
        }

        // 提取文件名基础部分和扩展名
        String baseName = originalPath.substring(0, lastDotIndex); // "/upload/image"
        String extension = originalPath.substring(lastDotIndex);   // ".jpg"

        // baseName + "_" + size + extension
        String newPath = baseName + "_" + size + extension;

        return newPath;
    }


    //该目录位置位于项目根目录,自动映射到resources/static, 为网站的/, 默认情况不要修改
    public static final String PATH_STATIC = "static";
    public static final String PATH_UPLOADS = "uploads";
    //保存文章图片目录 uploads/attach/2025/12/10/example.png
    public static final String PATH_UPLOADS_ATTACHS = PATH_UPLOADS+"/attachs";

    // 关键修改：分开定义最大宽度和最大高度
    public static final int MAX_THUMB_WIDTH = 480;
    public static final int MAX_THUMB_HEIGHT = 640;

    public static boolean createThumb(String originalFilePath) {
        return createThumb(originalFilePath, "thumb");
    }
    /**
     * 按原图比例缩放图片，并保存为缩略图文件。
     * 保持图片纵横比，确保宽不大于 MAX_THUMB_WIDTH 且高不大于 MAX_THUMB_HEIGHT。
     *
     * @param originalFilePath 原图的服务器路径
     * @param thumbName    缩略图要后序
     * @return 成功返回 true，失败返回 false
     */
    public static boolean createThumb(String originalFilePath, String thumbName) {

        if (originalFilePath == null || originalFilePath.isEmpty() || thumbName == null || thumbName.isEmpty()) {
            System.err.println("文件路径不能为空。");
            return false;
        }

        File originalFile = new File(originalFilePath);
        if (!originalFile.exists()) {
            System.err.println("原始文件不存在: " + originalFilePath);
            return false;
        }

        String thumbSavePath = CommonUtils.getImageSizePath(originalFilePath);

        try {
            // 读取原始图像
            BufferedImage originalImage = ImageIO.read(originalFile);
            if (originalImage == null) {
                System.err.println("无法识别或读取原始图像文件格式。");
                return false;
            }

            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();

            // 计算缩放比例，保持纵横比
            double scale = 1.0;

            // 计算宽度和高度各自需要的缩放比例
            double widthScale = (double) MAX_THUMB_WIDTH / originalWidth;
            double heightScale = (double) MAX_THUMB_HEIGHT / originalHeight;

            // 如果图片超过了任一最大尺寸，则取较小的缩放比例，以确保图片完整地放入限制框内
            if (originalWidth > MAX_THUMB_WIDTH || originalHeight > MAX_THUMB_HEIGHT) {
                scale = Math.min(widthScale, heightScale);
            }

            int newWidth = (int) (originalWidth * scale);
            int newHeight = (int) (originalHeight * scale);

            // 缩放图像
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = resizedImage.createGraphics();

            // 设置渲染质量，这对于缩放质量至关重要
            //VALUE_INTERPOLATION_BILINEAR 双线性快,质量差, VALUE_INTERPOLATION_BICUBIC 三线性
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制缩放后的图像
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            // 确保目标保存目录存在
            Path thumbDirPath = Paths.get(thumbSavePath).getParent();
            if (thumbDirPath != null && !Files.exists(thumbDirPath)) {
                Files.createDirectories(thumbDirPath);
            }

            // 保存缩略图
            String formatName = getFileExtension(thumbSavePath);
//            if (formatName.equalsIgnoreCase("jpeg")) {
//                formatName = "jpg";
//            }

            boolean success = ImageIO.write(resizedImage, formatName, new File(thumbSavePath));

            return success;

        } catch (IOException e) {
            System.err.println("创建缩略图失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 辅助方法：从路径中获取文件扩展名 (不含点号)
     */
    public static String getFileExtension(String path) {
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == path.length() - 1) {
            return "";
        }
        return path.substring(lastDotIndex + 1).toLowerCase();
    }

    /**
     * 获取创建日期目录
     *
     * @param baseDirName
     * @return
     */
    public static String genDatePath(String baseDirName) {
        // 获取当前日期信息
        LocalDate today = LocalDate.now();

        // 格式化为年/月/日的字符串
        String datePathSuffix = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        // 拼接完整路径字符串
        String newRelativePath = baseDirName + "/" + datePathSuffix;

        // 创建目录
        Path fullPath = Paths.get(newRelativePath);

//        try {
//            // 检查目录是否存在，不存在则创建
//            if (!Files.exists(fullPath)) {
//                // Files.createDirectories() 抛出 IOException
//                Files.createDirectories(fullPath);
//            }
//        } catch (IOException e) {
//            // 关键修改：捕获受检异常，并将其封装为非受检异常抛出
//            // 这样方法签名就不需要 throws IOException
//            throw new UncheckedIOException("无法创建文件上传目录: " + fullPath, e);
//        }

        return newRelativePath;
    }
}

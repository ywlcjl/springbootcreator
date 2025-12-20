package com.example.springbootcreator.util;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    //是否使用Imagemagick生成缩略图, true使用,false不使用, 如果使用,请自行安装Imagemagick 6.*, win10不要安装7.*, 因为路径问题会报错
    public static final boolean USE_IMAGEMAGICK = true;
    //Imagemagick的安装路径 "C:\\Program Files\\ImageMagick-6.9.13-Q16-HDRI"
    //Imagemagick 6.* 下载网址 https://github.com/ImageMagick/ImageMagick6
    public static final String IMAGEMAGICK_PATH = "C:\\Program Files\\ImageMagick-6.9.13-Q16-HDRI";

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

        String thumbSavePath = CommonUtils.getImageSizePath(originalFilePath, thumbName);

        try {
            // 确保目标保存目录存在
            Path thumbDirPath = Paths.get(thumbSavePath).getParent();
            if (thumbDirPath != null && !Files.exists(thumbDirPath)) {
                Files.createDirectories(thumbDirPath);
            }

            //配置USE_IMAGEMAGICK true, 就是使用USE_IMAGEMAGICK生成缩略图
            if (USE_IMAGEMAGICK) {
                //使用ImageMagick生成缩略图
                IMOperation op = new IMOperation();
                // 处理 Windows 路径：将 \ 替换为 /，避免 im4java 传参时转义失败
                String inputPath = originalFile.getAbsolutePath().replace("\\", "/");
                String outputPath = new File(thumbSavePath).getAbsolutePath().replace("\\", "/");
                op.addImage(inputPath);
                // ImageMagick 的 resize 默认就是【等比例缩放】且【适合框内】
                // 对应你之前的 Math.min(widthScale, heightScale) 逻辑
                // 加 '>' 表示：只在原图大于目标尺寸时才缩小（防止小图被拉大模糊） ">"
                op.resize(MAX_THUMB_WIDTH, MAX_THUMB_HEIGHT);
                // 设置质量
                op.quality(100.0);
                op.addImage(outputPath);
                //执行命令
                ConvertCmd cmd = new ConvertCmd();
                // 重要：如果在 Windows 环境，显式设置搜索路径防止 convert 冲突
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    //windows需要手写设置ImageMagick安装路径, i4java只支持ImageMagick 6, 如果用7会路径报错
                    cmd.setSearchPath(IMAGEMAGICK_PATH);
                }
                cmd.run(op);
                return true;
            } else {
                //使用Thumbnails 生成缩略图
                Thumbnails.of(originalFile)
                        .size(MAX_THUMB_WIDTH, MAX_THUMB_HEIGHT) // 自动保持比例，取宽高的最小值缩放
                        .scalingMode(ScalingMode.BICUBIC)       // 高质量缩放模式
                        .outputFormat(getFileExtension(originalFilePath)) // 保持原格式
                        .outputQuality(1.0f)                // 质量压缩，0.8-0.9 之间 QPS 与质量平衡最好
                        .toFile(thumbSavePath);
                return true;
            }

        } catch (IOException e) {
            System.err.println("文件读写失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IM4JavaException e) {
            throw new RuntimeException(e);
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

        return newRelativePath;
    }
}

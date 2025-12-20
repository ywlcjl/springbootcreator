package com.example.springbootcreator.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.Attach;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.entity.Syslog;
import com.example.springbootcreator.mapper.AttachMapper;
import com.example.springbootcreator.service.AttachService;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.util.CommonUtils;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.process.Pipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * attach管理控制器
 */
@Controller
@RequestMapping("/admin/attach")
public class AttachController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private AttachService attachService;

    /**
     * attach列表
     */
    @GetMapping
    public String listAttachs(HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(value = "type", required = false) String type
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ATTACH)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ATTACH);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Page<Attach> mpPage = new Page<>(page, size);
        QueryWrapper<Attach> queryWrapper = new QueryWrapper<>();
        String pageSuffix = "";

        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("type", type);
            pageSuffix += "&type=" + type;
            model.addAttribute("type", type);
        }

        queryWrapper.orderByDesc("id");
        Page<Attach> resultPage = attachMapper.selectPage(mpPage, queryWrapper);

        model.addAttribute("title", "文章图片列表");
        model.addAttribute("resultPage", resultPage);
        model.addAttribute("currentPage", resultPage.getCurrent());
        model.addAttribute("pageSize", resultPage.getSize());
        model.addAttribute("totalPages", resultPage.getPages());
        model.addAttribute("pageUrl", "/admin/attach");
        model.addAttribute("pageSuffix", pageSuffix);

        return "admin/attach/list";
    }


    @GetMapping("/edit")
    public String editAttach(HttpSession session, Model model,
                             @RequestParam(value = "id", required = false) Long id,
                             RedirectAttributes ra
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ATTACH)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ATTACH);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        String title = "添加文章图片";
        Attach editAttach = null;
        Integer success = 0;
        String message = "";

        if (id != null && id > 0) {
            //编辑文章
            editAttach = attachMapper.selectById(id);
            if (editAttach != null) {
                title = "编辑文章图片";
            } else {
                message = "该文章图片id不存在";
                ra.addFlashAttribute("success", success);
                ra.addFlashAttribute("message", message);
                return "redirect:/admin/attach";
            }
        } else {
            editAttach = new Attach();
        }

        model.addAttribute("title", title);
        model.addAttribute("attach", editAttach);

        return "admin/attach/edit";
    }

    /**
     * 保存文章
     */
    @PostMapping("/save")
    public String saveAttach(HttpSession session, Model model,
                             @RequestParam(value = "id", required = false) Long id,
                             @RequestParam(value = "name") String name,
                             @RequestParam(value = "origName") String origName,
                             @RequestParam(value = "path") String path,
                             @RequestParam(value = "type") String type,
                             @RequestParam(value = "articleId", required = false) Long articleId,
                             @RequestParam(value = "userfile", required = false) MultipartFile file,
                             RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ATTACH)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ATTACH);
        }

        Attach saveAttach = new Attach();
        Integer success = 0;
        String message = "";

        try {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("name不能为空.");
            } else if (origName == null || origName.isEmpty()) {
                throw new IllegalArgumentException("origName不能为空");
            } else if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException("path不能为空");
            } else if (type == null || type.isEmpty()) {
                throw new IllegalArgumentException("type不能为空");
            } else if (articleId == null || articleId < 0) {
                throw new IllegalArgumentException("articleId不能为空");
            }

            if (id != null && id > 0) {
                saveAttach.setId(id);
            }

            //输入过滤
            name = SecurityUtils.sanitizeInput(name);
            origName = SecurityUtils.sanitizeInput(origName);
            path = SecurityUtils.sanitizeInput(path);
            type = SecurityUtils.sanitizeInput(type);

            saveAttach.setName(name);
            saveAttach.setOrigName(origName);
            saveAttach.setPath(path);
            saveAttach.setType(type);
            saveAttach.setArticleId(articleId);

            //处理图片
            String fileMessage = "";

            //只能编辑现成的, 不能新建无文章关联的图片
            if (id != null && id > 0 && file != null && !file.isEmpty()) {

                String originalFilename = file.getOriginalFilename();
                String fileExtension = CommonUtils.getFileExtension(originalFilename);
                Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png");
                //验证文件类型
                if (allowedExtensions.contains(fileExtension.toLowerCase())
                        && SecurityUtils.isActualImage(file)) {

                    String uploadDir = path.replace("/" + name, "");

                    Path uploadPath = Paths.get(CommonUtils.PATH_STATIC + "/" + uploadDir);

                    if (Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    String filePathStr = CommonUtils.PATH_STATIC + "/" + path;

                    //真实文件路径 uploadPath.resolve(newFileName);
                    Path filePath = uploadPath.resolve(name);

                    //如果与原来的图片不同类型则自动转换为原来图片的扩展名的图片
                    Attach attachSource = attachMapper.selectById(id);
                    if (attachSource != null && attachSource.getType() != null) {
                        if (attachSource.getType().equals(fileExtension)) {
                            if (!Files.exists(filePath)) {
                                Files.copy(file.getInputStream(), filePath);
                            } else {
                                //图片文件存在
                                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                            }

                            //生成缩略图
                            CommonUtils.createThumb(filePathStr, "thumb");

                        } else {
                            //替换图片扩展名不同则自动转换
                            File targetFile = filePath.toFile();

                            if (CommonUtils.USE_IMAGEMAGICK) {
                                //使用imagemagick进行格式转换
                                try {
                                    // 使用 im4java 构建转换命令
                                    IMOperation op = new IMOperation();

                                    // "-" 代表从标准输入读取流（即 file.getInputStream()）
                                    op.addImage("-");

                                    // 设置输出路径，ImageMagick 会根据 targetFile 的后缀自动转换格式（png/jpg）
                                    op.addImage(targetFile.getAbsolutePath().replace("\\", "/"));

                                    ConvertCmd cmd = new ConvertCmd();
                                    // Windows 环境下指定路径
                                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                                        cmd.setSearchPath(CommonUtils.IMAGEMAGICK_PATH);
                                    }

                                    // 执行转换，直接通过 Pipe 将上传的文件流喂给 ImageMagick
                                    Pipe pipeIn = new Pipe(file.getInputStream(), null);
                                    cmd.setInputProvider(pipeIn);

                                    cmd.run(op);

                                    // 生成缩略图
                                    CommonUtils.createThumb(filePathStr, "thumb");

                                } catch (Exception e) {
                                    fileMessage = "ImageMagick 转换失败: " + e.getMessage();
                                    e.printStackTrace();
                                }
                            } else {
                                //使用Java ImageIO进行格式转换
                                BufferedImage fileImage = ImageIO.read(file.getInputStream());

                                if (attachSource.getType().equals("png")) {
                                    ImageIO.write(fileImage, "png", targetFile);

                                } else if (attachSource.getType().equals("jpg") || attachSource.getType().equals("jpeg")) {
                                    ImageIO.write(fileImage, "jpg", targetFile);
                                } else {
                                    fileMessage = "上传图片不是png和jpg";
                                }
                                CommonUtils.createThumb(filePathStr, "thumb");
                            }
                        }
                    } else {
                        fileMessage = "新上传图片与原图文件扩展名不同";
                    }
                } else {
                    fileMessage = "文件类型不被允许，只支持 JPG, JPEG, PNG 格式。";
                }
            }

            Boolean result = attachService.save(saveAttach);

            if (result) {
                success = 1;
                message = "文章图片保存成功!";

            } else {
                message = "文章图片没有更新";
            }

            message = message + " " + fileMessage;

            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);

            return "redirect:/admin/attach";
        } catch (IllegalArgumentException e) {
            //表单验证失败
            Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

            saveAttach.setId(id);
            saveAttach.setName(name);
            saveAttach.setOrigName(origName);
            saveAttach.setPath(path);
            saveAttach.setType(type);
            saveAttach.setArticleId(articleId);

            model.addAttribute("success", 0);
            model.addAttribute("message", "保存文章图片失败: " + e.getMessage());
            model.addAttribute("attach", saveAttach);
            model.addAttribute("title", id != null ? "编辑文章图片" : "新增文章图片");
            return "admin/attach/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("success", 1);
            ra.addFlashAttribute("message", "保存文章图片时发生错误：" + e.getMessage());
            return "redirect:/admin/attach";
        }
    }

    /**
     * 删除文章
     */
    @PostMapping("/delete")
    public String deleteArticle(@RequestParam Long id,
                                RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ATTACH)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ATTACH);
        }

        Integer success = 0;
        String message = "";

        try {
            if (id == null || id < 0) {
                throw new IllegalArgumentException("请输出正确的ID");
            }
            Boolean result = attachService.deleteById(id);

            if (result) {
                success = 1;
                message = "文章图片删除成功";
            } else {
                message = "文章图片不存在或已经被删除";
            }
        } catch (IllegalStateException e) {
            // 捕获 Service 抛出的业务异常
            message = "删除文章图片失败: " + e.getMessage();
        } catch (Exception e) {
            message = "删除文章图片发生错误: " + e.getMessage();
        }

        ra.addFlashAttribute("success", success);
        ra.addFlashAttribute("message", message);
        return "redirect:/admin/attach";
    }

    @PostMapping("/ajaxUpload")
    @ResponseBody
    public Map<String, Object> ajaxUpload(
            HttpSession session, Model model,
            @RequestParam(value = "userfile", required = false) MultipartFile file,
            @RequestParam(value = "articleId", required = false) Long articleId,
            RedirectAttributes ra
    ) {
        Map<String, Object> response = new HashMap<>();

        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            response.put("message", "你的操作权限不足, 需要权限ID " + WebUtils.PERMISSION_ID_ARTICLE);
            response.put("success", false);
            return response;
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        if (file == null || file.isEmpty()) {
            response.put("message", "上传文件为空");
            response.put("success", false);
            return response;
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = CommonUtils.getFileExtension(originalFilename);
        Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png");
        //验证文件类型
        if (!allowedExtensions.contains(fileExtension.toLowerCase())
                || !SecurityUtils.isActualImage(file)) {
            response.put("message", "文件类型不被允许，只支持 JPG, JPEG, PNG 格式。");
            response.put("success", false);
            return response;
        }

        //获取文章图片目录, uploads/attachs
        String attachDirF = CommonUtils.PATH_UPLOADS_ATTACHS;
        //获取日期目录返回完整路径 uploads/attachs/2025/12/10
        String attachDir = CommonUtils.genDatePath(attachDirF);
        //自动映射static为根目录, 物理路径目录, static/uploads/attachs/2025/12/10
        String attachDirPath = CommonUtils.PATH_STATIC + "/" + attachDir;

        try {
            String newFileNameUUID = UUID.randomUUID().toString();
            String newFileName = newFileNameUUID + "." + fileExtension;

            //真实目录路径
            Path uploadPath = Paths.get(attachDirPath);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            //真实文件路径
            Path filePath = uploadPath.resolve(newFileName);

            Files.copy(file.getInputStream(), filePath);

            //获取缩略图真实路径
            String filePathStr = attachDirPath + "/" + newFileName;

            //生成缩略图
            CommonUtils.createThumb(filePathStr);

            // 注意：如果 UPLOAD_DIR 是项目内部 static 目录，则路径直接从根开始
            String picUrl = attachDir + "/" + newFileName;
            String picUrlThumb = CommonUtils.getImageSizePath(picUrl);

            //写入数据库attach表
            Attach saveAttach = new Attach();
            saveAttach.setName(newFileName);
            saveAttach.setOrigName(originalFilename);
            saveAttach.setPath(picUrl);
            saveAttach.setType(fileExtension);
            saveAttach.setAdminId(currentAdmin.getId());

            if (articleId != null && articleId > 0) {
                saveAttach.setArticleId(articleId);
            }

            Boolean result = attachService.save(saveAttach);

            if (result) {
                response.put("success", true);
                response.put("message", "上传成功");

                response.put("picUrl", picUrl);
                response.put("picUrlThumb", picUrlThumb);
                response.put("attachId", saveAttach.getId());
                response.put("articleId", saveAttach.getArticleId());
            } else {
                response.put("success", false);
                response.put("message", "图片上传失败");
            }


        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "服务器处理文件失败: " + e.getMessage());
            response.put("success", false);
        }

        return response;
    }

    @PostMapping("/ajaxDelete")
    @ResponseBody
    public Map<String, Object> ajaxUpload(
            @RequestParam(value = "id") Long id,
            RedirectAttributes ra
    ) {
        Map<String, Object> response = new HashMap<>();

        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            response.put("message", "你的操作权限不足, 需要权限ID " + WebUtils.PERMISSION_ID_ARTICLE);
            response.put("success", false);
            return response;
        }

        if (id == null || id < 0) {
            response.put("message", "attach id 为空");
            response.put("success", false);
            return response;
        }

        Boolean success = false;
        String message = "";

        try {
            Boolean result = attachService.deleteById(id);

            if (result) {
                success = true;
                message = "图片删除成功";
            } else {
                message = "图片不存在或已经被删除";
            }
        } catch (IllegalStateException e) {
            message = "删除图片失败: " + e.getMessage();
        } catch (Exception e) {
            message = "删除图片发生错误: " + e.getMessage();
        }

        response.put("success", success);
        response.put("message", message);

        return response;
    }
}
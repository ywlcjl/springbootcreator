package com.example.springbootcreator.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.AdminMapper;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.service.AdminService;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * 管理员用户管理控制器
 * 路径: /admin/admin/**
 */
@Controller
@RequestMapping("/admin/admin")
public class AdminController {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthService authService;

    /**
     * 查看所有用户列表 (带分页)
     * GET /admin/admin
     *
     * @param page  页码 (默认第0页)
     * @param size  每页大小 (默认10条)
     * @param model Spring MVC 模型
     * @return FreeMarker 模板路径 (假设为 admin/admin/list.ftlh)
     */
    @GetMapping
    public String listAdmins(HttpSession session, Model model, RedirectAttributes ra,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(value = "status", required = false) Integer status //value="status"是一个get参数
            ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Page<Admin> mpPage = new Page<>(page, size);

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();

        String pageSuffix = "";

        if (status != null) {
            queryWrapper.eq("status", status);
            pageSuffix += "&status=" + status;
        }

        queryWrapper.orderByDesc("id");

        Page<Admin> resultPage = adminMapper.selectPage(mpPage, queryWrapper);

        // 遍历列表并修改每个 Admin 对象
        if (resultPage != null && resultPage.getRecords().size() > 0) {
            for (Admin admin : resultPage.getRecords()) {
                admin.setPassword(null);
                admin.setSalt(null);
            }
        }

        model.addAttribute("title", "用户列表");
        model.addAttribute("resultPage", resultPage);

        model.addAttribute("currentPage", resultPage.getCurrent());
        model.addAttribute("pageSize", resultPage.getSize());
        model.addAttribute("totalPages", resultPage.getPages());
        model.addAttribute("pageUrl", "/admin/admin");
        model.addAttribute("pageSuffix", pageSuffix);

        model.addAttribute("status", status);

        return "admin/admin/list";
    }

    /**
     * 显示新增/编辑用户的表单
     * GET /admin/admin/edit (编辑)
     *
     * @param id    用户ID (可选)
     * @param model Spring MVC 模型
     * @return FreeMarker 模板路径 (假设为 admin/admin/form.ftlh)
     */
    @GetMapping("/edit")
    public String editAdmin(HttpSession session, Model model,
                           @RequestParam(value = "id", required = false) Long id,
                           RedirectAttributes ra
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        String title = "新增用户";
        Admin editAdmin = null;
        Integer success = 0;
        String message = "";

        if (id != null && id > 0) {
            //编辑admin
            editAdmin = adminMapper.selectById(id);

            if (editAdmin != null) {
                editAdmin.setPassword(null);
                editAdmin.setSalt(null);
                title = "编辑用户: " + editAdmin.getUsername();
            } else {
                message = "该用户名id不存在.";
                ra.addFlashAttribute("success", success);
                ra.addFlashAttribute("message", message);
                return "redirect:/admin/admin";
            }

        } else {
            //新增admin
            editAdmin = new Admin();
        }

        model.addAttribute("admin", editAdmin);
        model.addAttribute("title", title);

        return "admin/admin/edit";
    }

    /**
     * 处理新增/编辑用户表单提交
     */
    @PostMapping("/save")
    public String saveAdmin(
            HttpSession session, Model model,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "username") String username,
            @RequestParam(value = "rawPassword", required = false) String rawPassword, // 假设表单中密码输入框的 name 是 rawPassword
            @RequestParam(value = "status") Integer status,
            RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin saveAdmin = new Admin();
        Integer success = 0;
        String message = "";

        try {
            //表单验证
            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("用户名不能为空。");
            } else if(status == null || status < 0){
                throw new IllegalArgumentException("status不能为空。");
            }

            if (id != null && id > 0) {
                //编辑用户
                Admin editAdmin = adminMapper.selectById(id);

                if (editAdmin != null) {
                    saveAdmin.setId(editAdmin.getId());
                    saveAdmin.setIsRoot(editAdmin.getIsRoot());
                } else {
                    throw new IllegalArgumentException("该用户id不存在。");
                }
            }

            username = SecurityUtils.sanitizeInput(username);

            saveAdmin.setUsername(username);
            saveAdmin.setStatus(status);

            // UserService 负责处理密码加密和更新逻辑
            Boolean result =  adminService.save(session, saveAdmin, rawPassword);

            if(result) {
                success = 1;
                message = "用户 [" + saveAdmin.getUsername() + "] 保存成功!";
            } else {
                message = "用户信息没有被更新";
            }

            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);
            return "redirect:/admin/admin";
        } catch (IllegalArgumentException e) {
            //验证失败
            Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

            saveAdmin.setId(id);
            saveAdmin.setUsername(username);
            saveAdmin.setStatus(status);

            model.addAttribute("success", 0);
            model.addAttribute("message", "保存用户失败: " + e.getMessage());
            model.addAttribute("admin", saveAdmin);
            model.addAttribute("title", (id != null && id > 0 ? "编辑用户" : "新增用户"));
            return "admin/admin/edit";
        } catch (Exception e) {
            //闪存保存参数值, spring会在session保存,一次调用注销
            ra.addFlashAttribute("success", 1);
            ra.addFlashAttribute("message", "保存用户错误: " + e.getMessage());
            // 失败时重定向回用户列表，并显示错误信息
            return "redirect:/admin/admin";
        }
    }
}
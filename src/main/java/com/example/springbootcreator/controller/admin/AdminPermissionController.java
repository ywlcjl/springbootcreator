package com.example.springbootcreator.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.AdminPermission;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.AdminMapper;
import com.example.springbootcreator.mapper.AdminPermissionMapper;
import com.example.springbootcreator.service.AdminPermissionService;
import com.example.springbootcreator.service.AdminService;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员后台文章分类管理控制器
 */
@Controller
@RequestMapping("/admin/adminPermission")
public class AdminPermissionController {

    @Autowired
    private AdminPermissionMapper adminPermissionMapper;

    @Autowired
    private AdminPermissionService adminPermissionService;

    @Autowired
    private AuthService authService;
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;
    /**
     * 显示分类列表。
     */
    @GetMapping
    public String listAdminPermissions(HttpSession session, Model model,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Page<AdminPermission> mpPage = new Page<>(page, size);
        QueryWrapper<AdminPermission> queryWrapper = new QueryWrapper<>();
        String pageSuffix = "";

        queryWrapper.orderByAsc("id");
        Page<AdminPermission> resultPage = adminPermissionMapper.selectPage(mpPage, queryWrapper);

        model.addAttribute("title", "权限列表");
        model.addAttribute("resultPage", resultPage);

        model.addAttribute("currentPage", resultPage.getCurrent());
        model.addAttribute("pageSize", resultPage.getSize());
        model.addAttribute("totalPages", resultPage.getPages());
        model.addAttribute("pageUrl", "/admin/adminPermission");
        model.addAttribute("pageSuffix", pageSuffix);

        return "admin/admin_permission/list";
    }

    /**
     * 显示新增表单。
     */
    @GetMapping("/edit")
    public String editAdminPermission(HttpSession session, Model model,
                                      @RequestParam(value = "id", required = false) Integer id,
                                      RedirectAttributes ra
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        QueryWrapper<AdminPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);

        String title = "添加权限";
        AdminPermission editAdminPermission = null;
        Integer success = 0;
        String message = "";

        if (id != null && id > 0) {
            editAdminPermission = adminPermissionMapper.selectById(id);
            if (editAdminPermission != null) {
                title = "编辑权限";
            } else {
                message = "没有找到该权限id";
                ra.addFlashAttribute("success", success);
                ra.addFlashAttribute("message", message);
                return "redirect:/admin/adminPermission";
            }
        } else {
            editAdminPermission = new AdminPermission();
        }

        model.addAttribute("title", title);
        model.addAttribute("adminPermission", editAdminPermission);

        return "admin/admin_permission/edit";
    }

    /**
     * 处理新增或更新分类的请求。
     */
    @PostMapping("/save")
    public String saveAdminPermission(
            HttpSession session, Model model,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "summary", required = false) String summary,
            @RequestParam(value = "status") Integer status,
            RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        AdminPermission saveAdminPermission = new AdminPermission();
        Integer success = 0;
        String message = "";

        try {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("名称不能为空。");
            } else if (status == null || status < 0) {
                throw new IllegalArgumentException("status不能为空");
            }

            if (id != null && id > 0) {
                saveAdminPermission.setId(id);
            }

            //输入过滤
            name = SecurityUtils.sanitizeInput(name);
            summary = SecurityUtils.sanitizeInput(summary);

            saveAdminPermission.setName(name);
            saveAdminPermission.setSummary(summary);
            saveAdminPermission.setStatus(status);

            Boolean reslut = adminPermissionService.save(saveAdminPermission);

            if (reslut) {
                success = 1;
                message = "权限保存成功!";
            } else {
                message = "权限没有更新";
            }

            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);

            return "redirect:/admin/adminPermission";
        } catch (IllegalArgumentException e) {
            //表单验证失败
            Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

            saveAdminPermission.setId(id);
            saveAdminPermission.setName(name);
            saveAdminPermission.setSummary(summary);
            saveAdminPermission.setStatus(status);

            model.addAttribute("success", 0);
            model.addAttribute("message", "保存权限失败: " + e.getMessage());
            model.addAttribute("adminPermission", saveAdminPermission);
            model.addAttribute("title", id != null ? "编辑权限" : "新增权限");
            return "admin/admin_permission/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("success", 1);
            ra.addFlashAttribute("message", "保存权限时发生错误：" + e.getMessage());
            return "redirect:/admin/adminPermission";
        }
    }

    @GetMapping("/permit")
    public String permit(HttpSession session, Model model,
                                       @RequestParam(value = "adminId") Long adminId,
                                       RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);
        Integer success = 0;
        String message = "";
        String title = "授予权限";
        List<AdminPermission> adminPermissions = new ArrayList<>();
        List<Integer> permissionIdList = new ArrayList<>();
        Admin permitAdmin = new Admin();

        if (adminId != null || adminId > 0) {
            Admin admin = adminMapper.selectById(adminId);
            if (admin != null && admin.getId() > 0) {
                QueryWrapper<AdminPermission> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("status", 1);
                adminPermissions = adminPermissionMapper.selectList(queryWrapper);

                if (admin.getAdminPermission() != null && !admin.getAdminPermission().isEmpty()) {
                    permissionIdList = WebUtils.adminPermissionStrToIntList(admin.getAdminPermission());
                }

                admin.setPassword(null);
                admin.setSalt(null);
                permitAdmin = admin;

                success = 1;
            } else {
                message = "Admin 为空";
            }
        } else {
            message = "Admin ID为空";
        }

        if (success == 0) {
            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);
            return "redirect:/admin/admin";
        }

        model.addAttribute("title", title);
        model.addAttribute("adminPermissions", adminPermissions);
        model.addAttribute("permissionIdList", permissionIdList);
        model.addAttribute("permitAdmin", permitAdmin);

        return "admin/admin_permission/permit";
    }

    @PostMapping("/savePermit")
    public String savePermit(
            HttpSession session, Model model,
            @RequestParam(value = "adminId") Long adminId,
            @RequestParam(value = "permissionIds", required = false) List<Integer> permissionIds,
            RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin saveAdmin = new Admin();
        Integer success = 0;
        String message = "";

        try {
            if (adminId == null || adminId < 0) {
                throw new IllegalArgumentException("adminId不能为空。");
            }

            saveAdmin.setId(adminId);

            //保存到admin表的adminPermission的权限字段 1|2|3
            String adminPermissionString = "";
            if(permissionIds != null && !permissionIds.isEmpty()) {
                Integer i = 0;
                for(Integer permissionId : permissionIds) {
                    if(i>0) {
                        adminPermissionString += "|";
                    }
                    adminPermissionString += permissionId.toString();
                    i++;
                }
            }

            saveAdmin.setAdminPermission(adminPermissionString);
            saveAdmin.setUpdateTime(LocalDateTime.now());

            Integer affectedRow = adminMapper.updateById(saveAdmin);

            if (affectedRow > 0) {
                success = 1;
                message = "授予权限保存成功!";
            } else {
                message = "授予权限没有更新";
            }

            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);

            return "redirect:/admin/admin";
        } catch (IllegalArgumentException e) {
            //表单验证失败
            Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

            Admin permitAdmin = adminMapper.selectById(adminId);
            permitAdmin.setPassword(null);
            permitAdmin.setSalt(null);

            QueryWrapper<AdminPermission> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1);
            List<AdminPermission> adminPermissions = adminPermissionMapper.selectList(queryWrapper);

            model.addAttribute("success", 0);
            model.addAttribute("message", "授予权限保存失败: " + e.getMessage());
            model.addAttribute("title", "授予权限");
            model.addAttribute("adminPermissions", adminPermissions);
            model.addAttribute("permissionIdList", permissionIds);
            model.addAttribute("permitAdmin", permitAdmin);
            return "admin/admin_permission/permit";
        } catch (Exception e) {
            ra.addFlashAttribute("success", 1);
            ra.addFlashAttribute("message", "授予权限保存时发生错误：" + e.getMessage());
            return "redirect:/admin/admin";
        }
    }

}

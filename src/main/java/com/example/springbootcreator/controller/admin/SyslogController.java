package com.example.springbootcreator.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.Syslog;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.SyslogMapper;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.service.SyslogService;
import com.example.springbootcreator.util.CommonUtils;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * syslog管理控制器
 */
@Controller
@RequestMapping("/admin/syslog")
public class SyslogController {
    @Autowired
    private AuthService authService;

    @Autowired
    private SyslogMapper syslogMapper;

    @Autowired
    private SyslogService syslogService;

    /**
     * syslog列表
     */
    @GetMapping
    public String listSyslogs(HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(value = "type", required = false) String type
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Page<Syslog> mpPage = new Page<>(page, size);
        QueryWrapper<Syslog> queryWrapper = new QueryWrapper<>();
        String pageSuffix = "";

        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("type", type);
            pageSuffix += "&type=" + type;
            model.addAttribute("type", type);
        }

        queryWrapper.orderByDesc("id");
        Page<Syslog> resultPage = syslogMapper.selectPage(mpPage, queryWrapper);

        Map<Integer, String> typeMap = syslogService.getSyslogTypeMap();
        //转换为字符串map,以适用freemarker
        Map<String, String> typeMapT = CommonUtils.mapKeyIntegerToString(typeMap);
        model.addAttribute("typeMap", typeMapT);

        model.addAttribute("title", "日志列表");
        model.addAttribute("resultPage", resultPage);
        model.addAttribute("currentPage", resultPage.getCurrent());
        model.addAttribute("pageSize", resultPage.getSize());
        model.addAttribute("totalPages", resultPage.getPages());
        model.addAttribute("pageUrl", "/admin/syslog");
        model.addAttribute("pageSuffix", pageSuffix);

        return "admin/syslog/list";
    }

    @GetMapping("/edit")
    public String editSyslog(HttpSession session, Model model,
                              @RequestParam(value = "id", required = false) Long id,
                              RedirectAttributes ra
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        String title = "添加日志";
        Syslog editSyslog = null;
        Integer success = 0;
        String message = "";

        if (id != null && id > 0) {
            //编辑文章
            editSyslog = syslogMapper.selectById(id);
            if (editSyslog != null) {
                title = "编辑日志";
            } else {
                message = "该日志id不存在";
                ra.addFlashAttribute("success", success);
                ra.addFlashAttribute("message", message);
                return "redirect:/admin/syslog";
            }
        } else {
            editSyslog = new Syslog();
        }

        model.addAttribute("title", title);
        model.addAttribute("syslog", editSyslog);

        return "admin/syslog/edit";
    }

    /**
     * 保存文章
     */
    @PostMapping("/save")
    public String saveSyslog(HttpSession session,Model model,
                              @RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "type") Integer type,
                              @RequestParam(value = "typeName", required = false) String typeName,
                              @RequestParam(value = "content", required = false) String content,
                              @RequestParam(value = "adminId") Long adminId,
                              @RequestParam(value = "username") String username,
                              @RequestParam(value = "ipAddress", required = false) String ipAddress,
                              RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_SYSTEM);
        }

        Syslog saveSyslog = new Syslog();
        Integer success = 0;
        String message = "";

        try {
            if (type == null || type < 0) {
                throw new IllegalArgumentException("type不能为空");
            }else if (adminId == null || adminId < 0) {
                throw new IllegalArgumentException("userId不能为空.");
            } else if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("username不能为空");
            }

            if (id != null && id > 0) {
                saveSyslog.setId(id);
            }

            //输入过滤
            typeName = SecurityUtils.sanitizeInput(typeName);
            content = SecurityUtils.sanitizeInput(content);
            username = SecurityUtils.sanitizeInput(username);
            ipAddress = SecurityUtils.sanitizeInput(ipAddress);

            saveSyslog.setType(type);
            saveSyslog.setTypeName(typeName);
            saveSyslog.setContent(content);
            saveSyslog.setAdminId(adminId);
            saveSyslog.setUsername(username);
            saveSyslog.setIpAddress(ipAddress);

            Boolean result = syslogService.save(saveSyslog);

            if (result) {
                success = 1;
                message = "日志保存成功!";
            } else {
                message = "日志没有更新";
            }

            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);

            return "redirect:/admin/syslog";
        } catch (IllegalArgumentException e) {
            //表单验证失败
            Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

            saveSyslog.setId(id);
            saveSyslog.setType(type);
            saveSyslog.setTypeName(typeName);
            saveSyslog.setContent(content);
            saveSyslog.setAdminId(adminId);
            saveSyslog.setUsername(username);
            saveSyslog.setIpAddress(ipAddress);

            model.addAttribute("success", 0);
            model.addAttribute("message", "保存日志失败: " + e.getMessage());
            model.addAttribute("syslog", saveSyslog);
            model.addAttribute("title", id != null ? "编辑日志" : "新增日志");
            return "admin/syslog/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("success", 1);
            ra.addFlashAttribute("message", "保存日志时发生错误：" + e.getMessage());
            return "redirect:/admin/syslog";
        }
    }

    /**
     * 删除文章
     */
    @PostMapping("/delete")
    public String deleteArticle(@RequestParam Long id,
                                RedirectAttributes ra) {
        Integer success = 0;
        String message = "";

        try {
            if (id == null || id < 0) {
                throw new IllegalArgumentException("请输出正确的ID");
            }

            Boolean result = syslogService.deleteById(id);

            if (result) {
                success = 1;
                message = "日志删除成功";
            } else {
                message = "日志不存在或已经被删除";
            }
        } catch (IllegalStateException e) {
            // 捕获 Service 抛出的业务异常
            message = "删除日志失败: " + e.getMessage();
        } catch (Exception e) {
            message = "删除日志发生错误: " + e.getMessage();
        }

        ra.addFlashAttribute("success", success);
        ra.addFlashAttribute("message", message);
        return "redirect:/admin/syslog";
    }
}
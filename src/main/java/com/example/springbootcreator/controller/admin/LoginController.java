package com.example.springbootcreator.controller.admin;

import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.ArticleMapper;
import com.example.springbootcreator.mapper.AdminMapper;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.service.SyslogService;
import com.example.springbootcreator.service.AdminService;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * 负责用户登录、登出和公共页面的控制器。使用自定义认证和 Session 管理。
 */
@Controller
public class LoginController {

    //依赖注入authService, 不用new
    @Autowired
    private AuthService authService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;
    @Autowired
    private SyslogService syslogService;

    /**
     * 显示自定义登录页面。
     */
    //路由 Get /login
    @GetMapping("/admin/login")
    public String login(
            //Get 后面的Parameters 参数
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "redirect", required = false) String redirect,
            Model model) {

        if (success != null) {
            model.addAttribute("success", success);
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        if (redirect != null) {
            model.addAttribute("redirect", redirect);
        }

        return "admin/login";
    }

    /**
     * 处理登录表单提交。
     */
    //路由 Post /login
    @PostMapping("/admin/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(value = "redirect", required = false) String redirect,
            HttpSession session,
            Model model) {
        //输入参数安全过滤
        String saniUsername = SecurityUtils.sanitizeInput(username);
        String trimPassword = password.trim();

        Admin admin = authService.authenticate(saniUsername, trimPassword);

        if(admin != null) {
            //认证成功：设置 Session
            admin.setPassword(null); // 清除密码以防泄露
            admin.setSalt(null); // 清除盐值以防泄露

            //将用户信息存入 Session
            session.setAttribute(WebUtils.SESSION_CURRENT_ADMIN, admin);

            //权限处理
            List<Integer> adminPermissionIntList = new ArrayList<>();
            if (admin.getAdminPermission() != null && !admin.getAdminPermission().isEmpty()) {
                adminPermissionIntList = WebUtils.adminPermissionStrToIntList(admin.getAdminPermission());
            }
            //写入权限数组到session
            session.setAttribute(WebUtils.SESSION_CURRENT_PERMISSION, adminPermissionIntList);

            //登录系统写入日志
            syslogService.addSyslog(1, "登录系统");

            //String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
            //String redirectUrl = "/";
            String redirectUrl = "redirect:/admin/dashboard";

            if (redirect != null && !redirect.isEmpty()) {
                redirectUrl = "redirect:"+redirect;
            }

            return redirectUrl;
        } else {
            model.addAttribute("success", 0);
            model.addAttribute("message", "用户名或密码错误，请重试。");
            model.addAttribute("username", username);
            return "admin/login";
        }

    }

    /**
     * 登出操作：清除 Session。
     */
    @GetMapping("/admin/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate(); // 使当前会话无效
        String message = "您已成功登出.";
        ra.addFlashAttribute("success", 1);
        ra.addFlashAttribute("message", message);

        System.out.println(message);
        return "redirect:/admin/login";
    }
}
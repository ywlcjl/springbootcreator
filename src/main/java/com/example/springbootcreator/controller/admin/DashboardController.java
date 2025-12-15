package com.example.springbootcreator.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootcreator.entity.Syslog;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.ArticleMapper;
import com.example.springbootcreator.mapper.AttachMapper;
import com.example.springbootcreator.mapper.SyslogMapper;
import com.example.springbootcreator.mapper.AdminMapper;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.service.SyslogService;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardController {
    //依赖注入authService, 不用new
    @Autowired
    private AuthService authService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private SyslogService syslogService;

    @Autowired
    private SyslogMapper syslogMapper;

    /**
     * 登录成功后的跳转（管理员仪表盘）
     */
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        // 从 Session 中获取用户信息并传递给模板
        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        List<Integer> adminPermissionList = (List<Integer>) session.getAttribute(WebUtils.SESSION_CURRENT_PERMISSION);


        Long articleCount = articleMapper.selectCount(null);
        Long attachCount = attachMapper.selectCount(null);
        Long adminCount = adminMapper.selectCount(null);

        QueryWrapper<Syslog> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.last("limit 10");

        if (adminPermissionList == null
                || adminPermissionList.isEmpty()
                || !authService.hasPermission(WebUtils.PERMISSION_ID_SYSTEM)) {
            queryWrapper.eq("admin_id", currentAdmin.getId());
        }

        List<Syslog> syslogs = syslogMapper.selectList(queryWrapper);
        model.addAttribute("syslogs", syslogs);

        model.addAttribute("title", "后台首页");
        model.addAttribute("articleCount", articleCount);
        model.addAttribute("attachCount", attachCount);
        model.addAttribute("adminCount", adminCount);

        return "admin/dashboard";
    }

    @GetMapping("/forbidden/{permissionId}")
    public String forbidden(HttpSession session, Model model,
                            @PathVariable(value = "permissionId") Integer permissionId) {
        // 从 Session 中获取用户信息并传递给模板
        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        String message = "您的操作权限不足, 需要权限ID：" + permissionId;

        model.addAttribute("title", "权限不足提示");
        model.addAttribute("message", message);
        return "admin/forbidden";
    }

    @GetMapping("/test500")
    public String testError500() {
        // 模拟一个未捕获的运行时异常，这将导致 500 错误
        throw new RuntimeException("数据库连接失败或关键业务逻辑出错");
    }


}

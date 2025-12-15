package com.example.springbootcreator.util;

import com.example.springbootcreator.entity.Admin;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

public class WebUtils {
    /**
     * 当前登录系统用户的admin对象 存放在 Session 中的键名
     */
    public static final String SESSION_CURRENT_ADMIN = "CURRENT_ADMIN";

    //用户权限 adminPermission权限 session
    public static final String SESSION_CURRENT_PERMISSION = "CURRENT_PERMISSION";

    //权限检查失败, 跳转页面
    public static final String PERMISSION_FORBIDDEN_URL = "redirect:/admin/forbidden";
    //系统操作权限 对应permission 1
    public static final Integer PERMISSION_ID_SYSTEM = 1;
    //文章操作权限 对应permission 2
    public static final Integer PERMISSION_ID_ARTICLE = 2;
    //图片操作权限 permission 3
    public static final Integer PERMISSION_ID_ATTACH = 3;

    //获取权限不足跳转链接
    public static String getPermissionForbiddenUrl(Integer permissionId) {
        return PERMISSION_FORBIDDEN_URL+"/"+permissionId;
    }

    /**
     * 封装session admin放入ui model
     * @param session
     * @param model
     * @return
     */
    public static Admin getCurrentAdminInit(HttpSession session, Model model) {
        if (session == null) {
            return null;
        }

        Admin admin = (Admin) session.getAttribute(SESSION_CURRENT_ADMIN);

        model.addAttribute("currentAdmin", admin);

        return admin;
    }

    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return null;
        }
        return attributes.getRequest();
    }

    /**
     * 获得session
     * @param createIfAbsent 是否创建新回话, 默认否
     * @return
     */
    public static HttpSession getCurrentSession(boolean createIfAbsent) {
        try {
            HttpServletRequest request = getCurrentRequest();

            return request.getSession(createIfAbsent);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获得session重载
     * @return
     */
    public static HttpSession getCurrentSession() {
        return getCurrentSession(false);
    }

    public static String getClientIp() {
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return "unknown";
        }

        // 尝试从各种常见的 HTTP 头中获取 IP 地址，这些头通常由负载均衡器或代理设置。
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            // 这是最直接的获取方式，但可能不是真实客户端 IP (如果是通过代理访问)
            ip = request.getRemoteAddr();
        }

        // 如果存在多个 IP (例如 X-Forwarded-For)，取第一个（即真实客户端 IP）
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    public static List<Integer> adminPermissionStrToIntList(String adminPermissionString) {
        if (adminPermissionString == null || adminPermissionString.isEmpty()) {
            return new ArrayList<>();
        }

        String[] stringIds = adminPermissionString.split("\\|");

        List<Integer> integerIds = new ArrayList<>();

        for (String idStr : stringIds) {
            String trimId = idStr.trim();

            // 确保字符串不为空，并可以安全转换为数字
            if (!trimId.isEmpty()) {
                try {
                    // 转换为 Integer 并添加到列表
                    integerIds.add(Integer.valueOf(trimId));
                } catch (NumberFormatException e) {
                    // 捕获异常：如果字符串包含非数字字符，则跳过或记录错误
                    System.err.println("跳过无效ID: " + trimId);
                }
            }
        }

        return integerIds;
    }



}

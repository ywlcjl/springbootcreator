package com.example.springbootcreator.interceptor;

import com.example.springbootcreator.util.CommonUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 自定义授权拦截器, Interceptor
 * 拦截所有 /admin/** 路径，检查用户是否已登录。
 */
//告诉spring扫描这个类,成为一个Spring Bean, 方便依赖注入
@Component
public class AuthInterceptor implements HandlerInterceptor {
    //覆盖父类方法
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        // 检查 Session 中是否存在用户对象
        if (session.getAttribute(WebUtils.SESSION_CURRENT_ADMIN) == null) {
            // 如果用户未登录，重定向到登录页面
            response.sendRedirect(request.getContextPath() + "/admin/login?redirect=" + request.getRequestURI());
            return false;
        }

        // 用户已登录，允许访问
        return true;
    }
}
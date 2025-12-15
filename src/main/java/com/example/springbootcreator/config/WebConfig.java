package com.example.springbootcreator.config;

import com.example.springbootcreator.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置 Spring MVC，注册自定义的拦截器。
 */
//@Configuration, Spring 依赖注入 (DI) 和 控制反转 (IoC) 容器工作的基础
@Configuration
public class WebConfig implements WebMvcConfigurer {
    //依赖注入, 自动装配一个bean
    @Autowired
    private AuthInterceptor authInterceptor;

    //重写继承方法
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 AuthInterceptor，拦截所有 /admin/** 路径
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**")
                // 排除不需要认证的公共路径 (例如 /admin/login 如果存在的话)
                .excludePathPatterns("/admin/login", "/admin/logout");
    }
}

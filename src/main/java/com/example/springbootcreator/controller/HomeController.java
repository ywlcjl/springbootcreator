package com.example.springbootcreator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.Article;
import com.example.springbootcreator.entity.ArticleCategory;
import com.example.springbootcreator.mapper.AttachMapper;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Home
 */
@Controller
public class HomeController {

    @Autowired
    private AuthService authService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        model.addAttribute("title", "Spring Boot Creator 首页");
        return "home";
    }
}

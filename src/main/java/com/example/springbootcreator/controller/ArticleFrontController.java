package com.example.springbootcreator.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.entity.Article;
import com.example.springbootcreator.entity.ArticleCategory;
import com.example.springbootcreator.mapper.ArticleCategoryMapper;
import com.example.springbootcreator.mapper.ArticleMapper;
import com.example.springbootcreator.service.ArticleCategoryService;
import com.example.springbootcreator.service.ArticleService;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.HtmlUtils;

import java.util.List;


/**
 * 示例文章前端
 */
@Controller
public class ArticleFrontController {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleCategoryMapper articleCategoryMapper;

    /**
     * 示例文章列表
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/article/list")
    public String list(HttpSession session, Model model,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "10") int size
    ) {
        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Page<Article> mpPage = new Page<>(page, size);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("post_time");
        queryWrapper.eq("status", 1);

        String pageSuffix = "";

        Page<Article> resultPage = articleMapper.selectPage(mpPage, queryWrapper);

        List<ArticleCategory> allCategories = articleCategoryMapper.selectList(null);

        model.addAttribute("title", "示例文章列表");
        model.addAttribute("resultPage", resultPage);
        model.addAttribute("currentPage", resultPage.getCurrent());
        model.addAttribute("pageSize", resultPage.getSize());
        model.addAttribute("totalPages", resultPage.getPages());
        model.addAttribute("pageUrl", "/article/list");
        model.addAttribute("pageSuffix", pageSuffix);
        model.addAttribute("allCategories", allCategories);

        return "article/list";
    }

    @GetMapping("/article/{id}")
    public String detail(HttpSession session, Model model,
                         @PathVariable(value = "id") Long id
    ) {
        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        if (id != null && id > 0) {
            QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1);
            queryWrapper.eq("id", id);
            Article article = articleMapper.selectOne(queryWrapper);

            if (article != null && article.getId() != null) {
                ArticleCategory articleCategory = articleCategoryMapper.selectById(article.getCategoryId());

                String unescapeContent = HtmlUtils.htmlUnescape(article.getContent());
                article.setContent(unescapeContent);

                model.addAttribute("category", articleCategory);
                model.addAttribute("article", article);
                model.addAttribute("title", article.getTitle());
                return "article/detail";
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章未找到");
            }

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "文章ID错误");
        }
    }
}

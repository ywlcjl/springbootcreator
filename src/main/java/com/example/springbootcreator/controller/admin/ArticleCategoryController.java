package com.example.springbootcreator.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.ArticleCategory;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.mapper.ArticleCategoryMapper;
import com.example.springbootcreator.service.ArticleCategoryService;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

/**
 * 管理员后台文章分类管理控制器。
 * 路径: /admin/categories/**
 */
@Controller
@RequestMapping("/admin/articleCategory")
public class ArticleCategoryController {

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Autowired
    private AuthService authService;

    /**
     * 显示分类列表。
     */
    @GetMapping
    public String listCategories(HttpSession session, Model model,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "10") int size
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ARTICLE);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Page<ArticleCategory> mpPage = new Page<>(page, size);
        QueryWrapper<ArticleCategory> queryWrapper = new QueryWrapper<>();
        String pageSuffix = "";

        queryWrapper.orderByAsc("id");
        Page<ArticleCategory> resultPage = articleCategoryMapper.selectPage(mpPage, queryWrapper);

        List<ArticleCategory> allArticleCategories = articleCategoryMapper.selectList(null);

        model.addAttribute("title", "文章分类列表");
        model.addAttribute("resultPage", resultPage);
        model.addAttribute("allArticleCategories", allArticleCategories);

        model.addAttribute("currentPage", resultPage.getCurrent());
        model.addAttribute("pageSize", resultPage.getSize());
        model.addAttribute("totalPages", resultPage.getPages());
        model.addAttribute("pageUrl", "/admin/articleCategory");
        model.addAttribute("pageSuffix", pageSuffix);

        return "admin/article_category/list";
    }

    /**
     * 显示新增分类表单。
     */
    @GetMapping("/edit")
    public String editArticleCategory(HttpSession session, Model model,
                                      @RequestParam(value = "id", required = false) Integer id,
                                      RedirectAttributes ra
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ARTICLE);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        QueryWrapper<ArticleCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);

        List<ArticleCategory> allArticleCategories = articleCategoryMapper.selectList(queryWrapper);

        String title = "添加文章分类";
        ArticleCategory editArticleCategory = null;
        Integer success = 0;
        String message = "";

        if (id != null && id > 0) {
            editArticleCategory = articleCategoryMapper.selectById(id);
            if (editArticleCategory != null) {
                title = "编辑文章分类";
            } else {
                message = "没有找到该分类id";
                ra.addFlashAttribute("success", success);
                ra.addFlashAttribute("message", message);
                return "redirect:/admin/articleCategory";
            }
        } else {
            editArticleCategory = new ArticleCategory();
        }

        model.addAttribute("allArticleCategories", allArticleCategories);
        model.addAttribute("title", title);
        model.addAttribute("articleCategory", editArticleCategory);

        return "admin/article_category/edit";
    }

    /**
     * 处理新增或更新分类的请求。
     */
    @PostMapping("/save")
    public String saveCategory(
            HttpSession session, Model model,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "parentId", required = false) Integer parentId,
            @RequestParam(value = "sort") Integer sort,
            @RequestParam(value = "hopLink", required = false) String hopLink,
            @RequestParam(value = "status") Integer status,
            RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ARTICLE);
        }

        ArticleCategory saveArticleCategory = new ArticleCategory();
        Integer success = 0;
        String message = "";

        try {
            if (name == null || name.isEmpty()) {
                throw new IllegalArgumentException("分类名称不能为空。");
            } else if (sort == null || sort < 0) {
                throw new IllegalArgumentException("排序不能为空");
            } else if (status == null || status < 0) {
                throw new IllegalArgumentException("status不能为空");
            }

            if (id != null && id > 0) {
                saveArticleCategory.setId(id);
            }

            //输入过滤
            name = SecurityUtils.sanitizeInput(name);
            hopLink = SecurityUtils.sanitizeInput(hopLink);

            saveArticleCategory.setName(name);
            saveArticleCategory.setSort(sort);
            saveArticleCategory.setStatus(status);

            if (parentId != null) {
                saveArticleCategory.setParentId(parentId);
            }

            if (hopLink != null) {
                saveArticleCategory.setHopLink(hopLink);
            }

            Boolean reslut = articleCategoryService.save(saveArticleCategory);

            if (reslut) {
                success = 1;
                message = "分类保存成功!";
            } else {
                message = "分类没有更新";
            }

            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);

            return "redirect:/admin/articleCategory";
        } catch (IllegalArgumentException e) {
            //表单验证失败
            Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

            QueryWrapper<ArticleCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1);
            List<ArticleCategory> allArticleCategories = articleCategoryMapper.selectList(queryWrapper);

            saveArticleCategory.setId(id);
            saveArticleCategory.setName(name);
            saveArticleCategory.setSort(sort);
            saveArticleCategory.setStatus(status);
            saveArticleCategory.setParentId(parentId);
            saveArticleCategory.setHopLink(hopLink);

            model.addAttribute("success", 0);
            model.addAttribute("message", "保存分类失败: " + e.getMessage());
            model.addAttribute("articleCategory", saveArticleCategory);
            model.addAttribute("allArticleCategories", allArticleCategories);
            model.addAttribute("title", id != null ? "编辑分类" : "新增分类");
            return "admin/article_category/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("success", 1);
            ra.addFlashAttribute("message", "保存分类时发生错误：" + e.getMessage());
            return "redirect:/admin/articleCategory";
        }
    }

    /**
     * 删除分类。@PathVariable Integer id
     */
    @PostMapping("/delete")
    public String deleteCategory(@RequestParam Integer id, RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ARTICLE);
        }

        Integer success = 0;
        String message = "";

        try {
            if (id == null || id < 0) {
                throw new IllegalArgumentException("请输出正确的ID");
            }
            Boolean result = articleCategoryService.deleteById(id);

            if (result) {
                success = 1;
                message = "文章分类删除成功";
            } else {
                message = "文章分类不存在或已经被删除";
            }
        } catch (IllegalStateException e) {
            // 捕获 Service 抛出的业务异常
            message = "删除文章分类失败: " + e.getMessage();
        } catch (Exception e) {
            message = "删除文章分类发生错误: " + e.getMessage();
        }

        ra.addFlashAttribute("success", success);
        ra.addFlashAttribute("message", message);
        return "redirect:/admin/articleCategory";
    }
}

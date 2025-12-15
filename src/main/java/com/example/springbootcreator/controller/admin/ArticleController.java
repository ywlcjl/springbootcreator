package com.example.springbootcreator.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootcreator.entity.Article;
import com.example.springbootcreator.entity.ArticleCategory;
import com.example.springbootcreator.entity.Admin;
import com.example.springbootcreator.entity.Attach;
import com.example.springbootcreator.mapper.ArticleCategoryMapper;
import com.example.springbootcreator.mapper.ArticleMapper;
import com.example.springbootcreator.mapper.AttachMapper;
import com.example.springbootcreator.service.ArticleService;
import com.example.springbootcreator.service.AttachService;
import com.example.springbootcreator.service.AuthService;
import com.example.springbootcreator.util.CommonUtils;
import com.example.springbootcreator.util.SecurityUtils;
import com.example.springbootcreator.util.WebUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章管理控制器
 */
@Controller
@RequestMapping("/admin/article")
public class ArticleController {
    @Autowired
    private AuthService authService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    @Autowired
    private ArticleService articleService;
    @Autowired
    private AttachMapper attachMapper;
    @Autowired
    private AttachService attachService;

    /**
     * 显示文章列表
     */
    @GetMapping
    public String listArticles(HttpSession session, Model model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(value = "status", required = false) Integer status
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ARTICLE);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Page<Article> mpPage = new Page<>(page, size);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        String pageSuffix = "";

        if (status != null) {
            queryWrapper.eq("status", status);
            pageSuffix += "&status=" + status;
            model.addAttribute("status", status);
        }

        queryWrapper.orderByDesc("id");
        Page<Article> resultPage = articleMapper.selectPage(mpPage, queryWrapper);

        List<ArticleCategory> allArticleCategories = articleCategoryMapper.selectList(null);

        model.addAttribute("title", "文章列表");
        model.addAttribute("resultPage", resultPage);
        model.addAttribute("currentPage", resultPage.getCurrent());
        model.addAttribute("pageSize", resultPage.getSize());
        model.addAttribute("totalPages", resultPage.getPages());
        model.addAttribute("pageUrl", "/admin/article");
        model.addAttribute("pageSuffix", pageSuffix);

        model.addAttribute("allArticleCategories", allArticleCategories);

        return "admin/article/list";
    }


    @GetMapping("/edit")
    public String editArticle(HttpSession session, Model model,
                              @RequestParam(value = "id", required = false) Long id,
                              RedirectAttributes ra
    ) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ARTICLE);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        QueryWrapper<ArticleCategory> queryWrapper = new QueryWrapper<>();
        //queryWrapper.eq("status", 1);

        List<ArticleCategory> allArticleCategories = articleCategoryMapper.selectList(queryWrapper);

        String title = "添加文章";
        Article editArticle = null;
        Integer success = 0;
        String message = "";

        if (id != null && id > 0) {
            //编辑文章
            editArticle = articleMapper.selectById(id);
            if (editArticle != null) {
                title = "编辑文章";

                //获取文章图片
                List<Attach> attachList = attachService.getAttachsByArticleId(editArticle.getId());
                model.addAttribute("attachs", attachList);

            } else {
                message = "该文章id不存在";
                ra.addFlashAttribute("success", success);
                ra.addFlashAttribute("message", message);
                return "redirect:/admin/article";
            }
        } else {
            editArticle = new Article();
        }

        model.addAttribute("allArticleCategories", allArticleCategories);
        model.addAttribute("title", title);
        model.addAttribute("article", editArticle);

        return "admin/article/edit";
    }

    /**
     * 保存文章
     */
    @PostMapping("/save")
    public String saveArticle(HttpSession session,Model model,
                              @RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "title") String title,
                              @RequestParam(value = "author", required = false) String author,
                              @RequestParam(value = "source", required = false) String source,
                              @RequestParam(value = "coverPic", required = false) String coverPic,
                              @RequestParam(value = "descTxt", required = false) String descTxt,
                              @RequestParam(value = "content") String content,
                              @RequestParam(value = "hopLink", required = false) String hopLink,
                              @RequestParam(value = "top") Integer top,
                              @RequestParam(value = "categoryId") Integer categoryId,
                              @RequestParam(value = "postTime", required = false) String postTime,
                              @RequestParam(value = "status") Integer status,
                              @RequestParam(value = "attachIds", required = false) List<Integer> attachIds,
                              RedirectAttributes ra) {
        //检查权限
        if (!authService.hasPermission(WebUtils.PERMISSION_ID_ARTICLE)) {
            return WebUtils.getPermissionForbiddenUrl(WebUtils.PERMISSION_ID_ARTICLE);
        }

        Admin currentAdmin = WebUtils.getCurrentAdminInit(session, model);

        Article saveArticle = new Article();
        Integer success = 0;
        String message = "";

        try {
            if (title == null || title.isEmpty()) {
                throw new IllegalArgumentException("标题不能为空.");
            } else if (content == null || content.isEmpty()) {
                throw new IllegalArgumentException("内容不能为空");
            } else if (status == null || status < 0) {
                throw new IllegalArgumentException("status不能为空");
            } else if (top == null || top < 0) {
                throw new IllegalArgumentException("top不能为空");
            } else if (categoryId == null || categoryId < 0) {
                throw new IllegalArgumentException("categoryId不能为空");
            }

            if (id != null && id > 0) {
                saveArticle.setId(id);
            } else {
                //新增
                saveArticle.setAdminId(currentAdmin.getId());
            }

            //输入过滤
            title = SecurityUtils.sanitizeInput(title);
            author = SecurityUtils.sanitizeInput(author);
            source = SecurityUtils.sanitizeInput(source);
            coverPic = SecurityUtils.sanitizeInput(coverPic);
            descTxt = SecurityUtils.sanitizeInput(descTxt);
            content = SecurityUtils.sanitizeInput(content);
            hopLink = SecurityUtils.sanitizeInput(hopLink);

            saveArticle.setTitle(title);
            saveArticle.setContent(content);
            saveArticle.setTop(top);
            saveArticle.setCategoryId(categoryId);
            saveArticle.setStatus(status);

            if (author != null) {
                saveArticle.setAuthor(author);
            }
            if (source != null) {
                saveArticle.setSource(source);
            }
            if (coverPic != null) {
                saveArticle.setCoverPic(coverPic);
            }
            if (descTxt != null) {
                saveArticle.setDescTxt(descTxt);
            }
            if (hopLink != null) {
                saveArticle.setHopLink(hopLink);
            }
            if (postTime != null && !postTime.trim().isEmpty()) {
                LocalDateTime currentPostTime = CommonUtils.stringToLocalDateTime(postTime);
                saveArticle.setPostTime(currentPostTime);
            }

            Boolean result = articleService.save(saveArticle);

            //保存文章图片修改articleId
            if (saveArticle.getId() !=null && saveArticle.getId() > 0
                    && attachIds != null && attachIds.size() > 0) {
                attachService.updateArticleId(saveArticle.getId(), attachIds);
            }

            if (result) {
                success = 1;
                message = "文章保存成功!";
            } else {
                message = "文章没有更新";
            }

            ra.addFlashAttribute("success", success);
            ra.addFlashAttribute("message", message);

            return "redirect:/admin/article";
        } catch (IllegalArgumentException e) {
            //表单验证失败
            QueryWrapper<ArticleCategory> queryWrapper = new QueryWrapper<>();
            //queryWrapper.eq("status", 1);
            List<ArticleCategory> allArticleCategories = articleCategoryMapper.selectList(queryWrapper);

            //获取已存在的图片
            if (id != null && id > 0) {
                List<Attach> attachList = attachService.getAttachsByArticleId(id);
                model.addAttribute("attachs", attachList);
            }

            //获取提交未保存articleId的图片
            if (attachIds != null && attachIds.size() > 0) {
                QueryWrapper<Attach> queryWrapperAttach = new QueryWrapper<>();
                queryWrapperAttach.in("id", attachIds);
                List<Attach> formAttachLists = attachMapper.selectList(queryWrapperAttach);
                model.addAttribute("formAttachs", formAttachLists);
            }

            saveArticle.setId(id);
            saveArticle.setTitle(title);
            saveArticle.setContent(content);
            saveArticle.setTop(top);
            saveArticle.setCategoryId(categoryId);
            saveArticle.setStatus(status);
            saveArticle.setAuthor(author);
            saveArticle.setSource(source);
            saveArticle.setCoverPic(coverPic);
            saveArticle.setDescTxt(descTxt);
            saveArticle.setHopLink(hopLink);
            saveArticle.setPostTime(CommonUtils.stringToLocalDateTime(postTime));

            model.addAttribute("success", 0);
            model.addAttribute("message", "保存文章失败: " + e.getMessage());
            model.addAttribute("article", saveArticle);
            model.addAttribute("allArticleCategories", allArticleCategories);
            model.addAttribute("title", id != null ? "编辑文章" : "新增文章");
            return "admin/article/edit";
        } catch (Exception e) {
            ra.addFlashAttribute("success", 1);
            ra.addFlashAttribute("message", "保存文章时发生错误：" + e.getMessage());
            return "redirect:/admin/article";
        }
    }

    /**
     * 删除文章
     */
    @PostMapping("/delete")
    public String deleteArticle(@RequestParam Long id,
                                RedirectAttributes ra) {
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
            Boolean result = articleService.deleteById(id);

            if (result) {
                success = 1;
                message = "文章删除成功";
            } else {
                message = "文章不存在或已经被删除";
            }
        } catch (IllegalStateException e) {
            // 捕获 Service 抛出的业务异常
            message = "删除文章失败: " + e.getMessage();
        } catch (Exception e) {
            message = "删除文章发生错误: " + e.getMessage();
        }

        ra.addFlashAttribute("success", success);
        ra.addFlashAttribute("message", message);
        return "redirect:/admin/article";
    }
}
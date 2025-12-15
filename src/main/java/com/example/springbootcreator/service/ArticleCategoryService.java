package com.example.springbootcreator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootcreator.entity.ArticleCategory;
import com.example.springbootcreator.mapper.ArticleCategoryMapper;
import com.example.springbootcreator.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 文章分类业务逻辑服务。
 */
@Service
public class ArticleCategoryService {

    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;

    /**
     * 新增或更新分类。
     * @param saveArticleCategory 待保存的分类实体
     * @return ArticleCategory
     */
    public Boolean save(ArticleCategory saveArticleCategory) {
        LocalDateTime now = LocalDateTime.now();

        Boolean isNew = (saveArticleCategory.getId() == null || saveArticleCategory.getId() == 0) ? true : false;
        Integer affectedRow = 0;

        if (isNew) {
            // 新增操作
            affectedRow = articleCategoryMapper.insert(saveArticleCategory);
        } else {
            // 更新
            saveArticleCategory.setUpdateTime(now);
            affectedRow = articleCategoryMapper.updateById(saveArticleCategory);
        }

        if (affectedRow > 0) {
            return true;
        } else  {
            return false;
        }
    }

    /**
     * 根据 ID 删除分类。
     * @param id 分类ID
     */
    public Boolean deleteById(Integer id) {
        if (id == 1) {
            throw new IllegalStateException("默认分类,ID:1不能被删除");
        }

        QueryWrapper<ArticleCategory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        Long sonCount = articleCategoryMapper.selectCount(queryWrapper);

        if (sonCount != null && sonCount > 0) {
            throw new IllegalStateException("该分类包含子分类，请先删除或移动子分类。");
        }

        Integer affectedRow = articleCategoryMapper.deleteById(id);
        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }
    }
}

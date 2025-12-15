package com.example.springbootcreator.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springbootcreator.entity.Article;
import com.example.springbootcreator.entity.Attach;
import com.example.springbootcreator.mapper.ArticleMapper;
import com.example.springbootcreator.mapper.AttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章业务服务
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private SyslogService syslogService;
    @Autowired
    private AttachMapper attachMapper;

    @Autowired
    private AttachService attachService;

    /**
     * 保存文章
     *
     * @param saveArticle 待保存的文章实体
     * @return 保存后的文章实体
     */
    public Boolean save(Article saveArticle) {
        LocalDateTime now = LocalDateTime.now();

        Boolean isNew = (saveArticle.getId() == null || saveArticle.getId() == 0) ? true : false;

        Integer affectedRow = 0;
        if (isNew) {
            // 新增操作
            affectedRow = articleMapper.insert(saveArticle);
        } else {
            // 更新
            saveArticle.setUpdateTime(now);
            affectedRow = articleMapper.updateById(saveArticle);
        }

        if (affectedRow > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据 ID 删除文章
     *
     * @param id 文章ID
     */
    public Boolean deleteById(Long id) {
        Integer affectedRow = articleMapper.deleteById(id);
        if (affectedRow > 0) {
            //删除文章的图片
            List<Attach> attachList = attachMapper.selectList(new QueryWrapper<Attach>().eq("article_id", id));

            if (attachList != null && attachList.size() > 0) {
                for (Attach attach : attachList) {
                    attachService.deleteById(attach.getId());
                }
            }

            //登录系统写入日志
            syslogService.addSyslog(2, "删除文章ID: "+id);
            return true;
        } else {
            return false;
        }
    }
}

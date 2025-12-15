package com.example.springbootcreator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库表 `article_category` (文章分类)
 */
@Data
@TableName("article_category")
public class ArticleCategory {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    //父分类ID，用于构建树状结构 对应表字段 parent_id
    private Integer parentId;

    //跳转链接 对应表字段 hop_link
    private String hopLink;

    // 排序值
    private Integer sort;

    // 状态 (例如: 1-启用, 0-禁用)
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}

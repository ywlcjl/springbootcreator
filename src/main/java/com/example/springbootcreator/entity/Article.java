package com.example.springbootcreator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库表 `article`
 */
// Lombok: 自动生成 Getter/Setter/equals/hashCode/toString
@Data
@TableName("article")
public class Article {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;
    private String author;
    private String source;

    //默认开启驼峰命名约定 @TableField 映射, 可以省略@TableField, coverPic对应数据库字段cover_pic
    @TableField("cover_pic")
    private String coverPic;
    private String descTxt;
    private String content;
    private String hopLink;

    // 推荐
    private Integer top;

    // 分类id
    private Integer categoryId;

    // 管理员id
    private Long adminId;

    // 状态
    private Integer status;

    private LocalDateTime postTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}
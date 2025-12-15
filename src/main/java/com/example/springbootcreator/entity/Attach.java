package com.example.springbootcreator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库表 `attach`
 */
@Data
@TableName("attach")
public class Attach {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;
    private String origName;
    private String path;
    private String type;
    private Long articleId;
    private Long adminId;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;

}
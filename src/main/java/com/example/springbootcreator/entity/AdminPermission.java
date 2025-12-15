package com.example.springbootcreator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库表 `admin_permission`
 */
@Data
@TableName("admin_permission")
public class AdminPermission {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    //描述
    private String summary;

    //受保护权限,不允许被删除
    private Integer isProtected;

    // 状态 (例如: 1-启用, 0-禁用)
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}


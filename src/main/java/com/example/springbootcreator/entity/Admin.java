package com.example.springbootcreator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 对应数据库表 `admin` 数据映射层
 */
//lombok 自动生成getter setter
@Data
@TableName("admin")
public class Admin {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    // 密码字段，存储的是 sha-1 (passwor+salt)
    private String password;

    // 添加 salt 字段，用于密码加盐。长度为 16 字符
    private String salt;

    private Integer isRoot;

    // 权限字符集，例如 "1|2"
    private String adminPermission;

    // 状态: 0停用, 1启用
    private Integer status;

    private LocalDateTime loginTime;

    private LocalDateTime updateTime;

    private LocalDateTime createTime;
}

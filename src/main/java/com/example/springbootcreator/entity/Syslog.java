package com.example.springbootcreator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("syslog")
public class Syslog {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer type;
    private String typeName;
    private String content;
    private Long adminId;
    private String username;
    private String ipAddress;
    private LocalDateTime updateTime;
    private LocalDateTime createTime;

}
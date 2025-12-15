package com.example.springbootcreator.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.example.springbootcreator.entity.Admin;

// 告诉 Spring 这是一个 Mapper接口
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}

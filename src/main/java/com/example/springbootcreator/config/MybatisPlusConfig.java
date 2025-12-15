package com.example.springbootcreator.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    /**
     * Mybatis-Plus 拦截器配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // **核心步骤：添加分页拦截器**
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor(DbType.MYSQL); // 假设您使用 MySQL

        // 如果您希望开启超页时自动调整页码到最后一页（可选）
        paginationInterceptor.setOverflow(false);

        // 设置最大单页限制数量（可选，防止传入过大的 size 导致内存溢出）
        paginationInterceptor.setMaxLimit(1000L);

        interceptor.addInnerInterceptor(paginationInterceptor);

        return interceptor;
    }
}

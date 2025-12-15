package com.example.springbootcreator.config;

import com.example.springbootcreator.util.FreemarkerUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.beans.BeansWrapper; // 引入静态工具的核心类
import freemarker.template.TemplateHashModel; // 用于包装静态类

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FreemarkerConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:/templates");

        // 1. 获取 FreeMarker 的 BeansWrapper
        // BeansWrapper 是 Freemarker 用来访问 Java 对象的机制
        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();

        // 2. 将 FreemarkerUtils 类包装成一个静态模型 (Static Model)
        TemplateHashModel staticModels = wrapper.getStaticModels();

        // 3. 注册静态类。键名 'FreemarkerUtilsStatic' 可以在模板中使用
        // FreemarkerUtils.class.getName() 是类的完全限定名 (e.g., com.example.springbootcreator.util.FreemarkerUtils)
        TemplateHashModel freemarkerUtilsStatics = null;
        try {
            freemarkerUtilsStatics = (TemplateHashModel) staticModels.get(FreemarkerUtils.class.getName());
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }

        // 4. 将静态模型添加到共享变量
        Map<String, Object> sharedVariables = new HashMap<>();
        // 键名 'FreemarkerUtils' 将在模板中被使用 全称FreemarkerUtils，缩写FU
        sharedVariables.put("FU", freemarkerUtilsStatics);

        configurer.setFreemarkerVariables(sharedVariables);

        // 注意：如果您的配置中已经有配置 configuration.setFreemarkerVariables(...) 的地方，确保合并变量。
        return configurer;
    }
}
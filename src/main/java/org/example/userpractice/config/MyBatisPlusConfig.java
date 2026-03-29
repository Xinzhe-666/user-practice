package org.example.userpractice.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration注解：告诉Spring Boot，这是一个配置类，项目启动时会自动加载
@Configuration
public class MyBatisPlusConfig {

    // @Bean注解：把这个方法返回的对象，放到Spring容器里管理
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        // 1. 创建MyBatis-Plus的拦截器（插件核心）
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 2. 添加分页拦截器，指定数据库类型是MySQL
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 3. 返回拦截器，让配置生效
        return interceptor;
    }
}
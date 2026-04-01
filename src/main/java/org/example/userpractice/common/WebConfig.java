package org.example.userpractice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC配置类
 * 配置拦截器的拦截规则
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 白名单：放行不需要登录就能访问的接口
                .excludePathPatterns(
                        "/user/register", // 用户注册接口
                        "/user/login",    // 用户登录接口
                        "/user/list"      // 用户列表接口（可选，方便测试）
                );
    }
}
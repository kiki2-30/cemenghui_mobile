package com.kiki.springboot_kiki.config;

import com.kiki.springboot_kiki.interceptor.AuthInterceptor;
import com.kiki.springboot_kiki.interceptor.RateLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private RateLimitInterceptor rateLimitInterceptor;
    
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 限流拦截器（优先级高）
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                .order(1);
        
        // 权限拦截器 - 只对管理端操作进行权限验证
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(
                        // 会议管理接口
                        "/api/conferences/*/standard-detail",
                        "/api/conferences/*/training-detail", 
                        "/api/conferences/*/tool-detail",
                        // 行业动态管理接口
                        "/api/news/list",
                        "/api/news/*/status",
                        "/api/news/*/top",
                        "/api/news/*/sort-order",
                        // 超级管理员接口
                        "/api/super-admin/**"
                )
                .excludePathPatterns(
                        // 公开接口
                        "/api/conferences/categories",
                        "/api/conferences/hot",
                        "/api/conferences/*/registrations",
                        "/api/conferences/*/check-registration",
                        "/api/conferences/*/registration-stats",
                        "/api/news/public-list",
                        "/api/news/behavior/trend",
                        "/user/login",
                        "/user/register",
                        "/user/check-*"
                )
                .order(2);
    }
} 
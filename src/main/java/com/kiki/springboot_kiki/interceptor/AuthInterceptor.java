package com.kiki.springboot_kiki.interceptor;

import com.kiki.springboot_kiki.exception.UnauthorizedException;
import com.kiki.springboot_kiki.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的Token
        String token = request.getHeader("Authorization");
        
        // 如果是OPTIONS预检请求，直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        
        // 检查Token是否存在
        if (token == null || token.isEmpty()) {
            throw new UnauthorizedException("缺少访问令牌");
        }
        
        // 检查Token格式
        if (!token.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token格式错误");
        }
        
        String actualToken = token.substring(7);
        
        // 使用JWT验证Token
        if (!jwtUtil.validateToken(actualToken)) {
            throw new UnauthorizedException("Token无效或已过期");
        }
        
        // 获取用户角色
        String role = jwtUtil.getRoleFromToken(actualToken);
        
        // 超级管理员接口权限检查
        if (request.getRequestURI().startsWith("/api/super-admin/")) {
            if (!"SUPER_ADMIN".equals(role)) {
                throw new UnauthorizedException("需要超级管理员权限");
            }
        }
        
        return true;
    }
} 
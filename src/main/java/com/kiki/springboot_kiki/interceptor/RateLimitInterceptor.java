package com.kiki.springboot_kiki.interceptor;

import com.kiki.springboot_kiki.annotation.RateLimit;
import com.kiki.springboot_kiki.exception.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // 使用内存存储限流计数器（生产环境建议使用Redis）
    private final Map<String, RateLimitCounter> counters = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        RateLimit rateLimit = handlerMethod.getMethodAnnotation(RateLimit.class);
        
        if (rateLimit == null) {
            return true;
        }

        String key = getLimitKey(request, rateLimit);
        if (!isAllowed(key, rateLimit)) {
            throw new RateLimitException("请求过于频繁，请稍后再试");
        }

        return true;
    }

    private String getLimitKey(HttpServletRequest request, RateLimit rateLimit) {
        String baseKey = request.getRequestURI();
        
        switch (rateLimit.type()) {
            case IP:
                return baseKey + ":" + getClientIP(request);
            case USER_ID:
                String userId = request.getParameter("userId");
                return baseKey + ":" + (userId != null ? userId : "anonymous");
            case GLOBAL:
            default:
                return baseKey;
        }
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private boolean isAllowed(String key, RateLimit rateLimit) {
        long currentTime = System.currentTimeMillis();
        long windowStart = currentTime - (rateLimit.time() * 1000L);

        RateLimitCounter counter = counters.computeIfAbsent(key, k -> new RateLimitCounter());
        
        // 清理过期的计数
        counter.cleanExpired(windowStart);
        
        // 检查是否超过限制
        if (counter.getCount() >= rateLimit.count()) {
            return false;
        }
        
        // 增加计数
        counter.increment(currentTime);
        return true;
    }

    private static class RateLimitCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private long lastResetTime = System.currentTimeMillis();

        public void increment(long currentTime) {
            count.incrementAndGet();
            lastResetTime = currentTime;
        }

        public int getCount() {
            return count.get();
        }

        public void cleanExpired(long windowStart) {
            if (lastResetTime < windowStart) {
                count.set(0);
                lastResetTime = System.currentTimeMillis();
            }
        }
    }
} 
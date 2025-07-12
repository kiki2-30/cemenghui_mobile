package com.kiki.springboot_kiki.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimit {
    /**
     * 限流时间窗口（秒）
     */
    int time() default 60;
    
    /**
     * 时间窗口内允许的请求次数
     */
    int count() default 100;
    
    /**
     * 限流类型：IP、用户ID、全局
     */
    LimitType type() default LimitType.IP;
    
    enum LimitType {
        IP,        // 按IP限流
        USER_ID,   // 按用户ID限流
        GLOBAL     // 全局限流
    }
} 
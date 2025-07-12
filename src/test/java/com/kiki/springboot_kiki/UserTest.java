package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class UserTest {

    @Test
    public void testGetterSetterAndToString() {
        User user = new User();
        
        // 测试所有 getter/setter 方法
        user.setUserId(1);
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setPhone("13800138000");
        user.setRealName("张三");
        user.setCompany("测试公司");
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setRole(User.RoleType.ADMIN);
        
        // 验证 getter 方法
        assertEquals(1, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("13800138000", user.getPhone());
        assertEquals("张三", user.getRealName());
        assertEquals("测试公司", user.getCompany());
        assertNotNull(user.getCreateTime());
        assertNotNull(user.getUpdateTime());
        assertEquals(User.RoleType.ADMIN, user.getRole());
        
        // 测试 toString 方法
        String toString = user.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("userId=1"));
        assertTrue(toString.contains("username='testuser'"));
    }
    
    @Test
    public void testRoleTypeEnum() {
        // 测试角色枚举
        assertEquals("USER", User.RoleType.USER.name());
        assertEquals("ADMIN", User.RoleType.ADMIN.name());
        assertEquals("SUPER_ADMIN", User.RoleType.SUPER_ADMIN.name());
    }
} 
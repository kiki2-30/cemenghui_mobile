package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.UserDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserDTOTest {

    @Test
    public void testGetterSetterAndToString() {
        UserDTO dto = new UserDTO();
        
        // 测试所有 getter/setter 方法
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setEmail("test@example.com");
        dto.setPhone("13800138000");
        dto.setRealName("张三");
        dto.setCompany("测试公司");
        
        // 验证 getter 方法
        assertEquals("testuser", dto.getUsername());
        assertEquals("password123", dto.getPassword());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("13800138000", dto.getPhone());
        assertEquals("张三", dto.getRealName());
        assertEquals("测试公司", dto.getCompany());
        
        // 测试 toString 方法
        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("UserDTO"));
        assertTrue(toString.contains("username='testuser'"));
        assertTrue(toString.contains("email='test@example.com'"));
    }
} 
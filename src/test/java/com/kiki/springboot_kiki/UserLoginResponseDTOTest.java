package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.UserLoginResponseDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserInfoDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class UserLoginResponseDTOTest {

    @Test
    public void testGetterSetter() {
        UserLoginResponseDTO dto = new UserLoginResponseDTO();
        
        // 测试所有 getter/setter 方法
        dto.setUserId(1);
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPhone("13800138000");
        dto.setRealName("张三");
        dto.setCompany("测试公司");
        dto.setRole("ADMIN");
        LocalDateTime now = LocalDateTime.now();
        dto.setCreateTime(now);
        dto.setUpdateTime(now.plusDays(1));
        dto.setToken("token123");
        
        // 验证 getter 方法
        assertEquals(1, dto.getUserId());
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("13800138000", dto.getPhone());
        assertEquals("张三", dto.getRealName());
        assertEquals("测试公司", dto.getCompany());
        assertEquals("ADMIN", dto.getRole());
        assertEquals(now, dto.getCreateTime());
        assertEquals(now.plusDays(1), dto.getUpdateTime());
        assertEquals("token123", dto.getToken());
    }

    @Test
    public void testConstructorWithUserInfoDTO() {
        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setUserId(2);
        userInfo.setUsername("user2");
        userInfo.setEmail("user2@example.com");
        userInfo.setPhone("13900139000");
        userInfo.setRealName("李四");
        userInfo.setCompany("公司2");
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime updateTime = createTime.plusDays(2);
        userInfo.setCreateTime(createTime);
        userInfo.setUpdateTime(updateTime);
        
        UserLoginResponseDTO dto = new UserLoginResponseDTO(userInfo, "token456", "USER");
        assertEquals(2, dto.getUserId());
        assertEquals("user2", dto.getUsername());
        assertEquals("user2@example.com", dto.getEmail());
        assertEquals("13900139000", dto.getPhone());
        assertEquals("李四", dto.getRealName());
        assertEquals("公司2", dto.getCompany());
        assertEquals("USER", dto.getRole());
        assertEquals(createTime, dto.getCreateTime());
        assertEquals(updateTime, dto.getUpdateTime());
        assertEquals("token456", dto.getToken());
    }
} 
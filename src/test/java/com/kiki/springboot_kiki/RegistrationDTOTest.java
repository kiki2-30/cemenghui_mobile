package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.RegistrationDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class RegistrationDTOTest {

    @Test
    public void testGetterSetterAndToString() {
        RegistrationDTO dto = new RegistrationDTO();
        
        // 测试所有 getter/setter 方法
        dto.setConferenceId(1);
        dto.setUserId(2);
        dto.setCompany("测试公司");
        dto.setRealName("张三");
        dto.setGender(1);
        dto.setPhone("13800138000");
        dto.setEmail("test@example.com");
        dto.setArrivalMethod("飞机");
        dto.setArrivalTrain("航班号123");
        dto.setArrivalTime(LocalDateTime.now().plusDays(1));
        
        // 验证 getter 方法
        assertEquals(1, dto.getConferenceId());
        assertEquals(2, dto.getUserId());
        assertEquals("测试公司", dto.getCompany());
        assertEquals("张三", dto.getRealName());
        assertEquals(1, dto.getGender());
        assertEquals("13800138000", dto.getPhone());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals("飞机", dto.getArrivalMethod());
        assertEquals("航班号123", dto.getArrivalTrain());
        assertNotNull(dto.getArrivalTime());
        
        // 测试 toString 方法
        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("RegistrationDTO"));
        assertTrue(toString.contains("conferenceId=1"));
        assertTrue(toString.contains("realName='张三'"));
    }
} 
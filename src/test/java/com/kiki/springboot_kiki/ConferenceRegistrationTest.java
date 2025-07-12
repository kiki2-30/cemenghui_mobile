package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.ConferenceRegistration;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class ConferenceRegistrationTest {

    @Test
    public void testGetterSetterAndToString() {
        ConferenceRegistration registration = new ConferenceRegistration();
        
        // 测试所有 getter/setter 方法
        registration.setRegistrationId(1);
        registration.setConferenceId(2);
        registration.setUserId(3);
        registration.setCompany("测试公司");
        registration.setRealName("张三");
        registration.setGender(1);
        registration.setPhone("13800138000");
        registration.setEmail("test@example.com");
        registration.setArrivalMethod("飞机");
        registration.setArrivalTrain("航班号123");
        registration.setArrivalTime(LocalDateTime.now().plusDays(1));
        registration.setRegistrationTime(new Timestamp(System.currentTimeMillis()));
        
        // 验证 getter 方法
        assertEquals(1, registration.getRegistrationId());
        assertEquals(2, registration.getConferenceId());
        assertEquals(3, registration.getUserId());
        assertEquals("测试公司", registration.getCompany());
        assertEquals("张三", registration.getRealName());
        assertEquals(1, registration.getGender());
        assertEquals("13800138000", registration.getPhone());
        assertEquals("test@example.com", registration.getEmail());
        assertEquals("飞机", registration.getArrivalMethod());
        assertEquals("航班号123", registration.getArrivalTrain());
        assertNotNull(registration.getArrivalTime());
        assertNotNull(registration.getRegistrationTime());
        
        // 测试 toString 方法
        String toString = registration.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ConferenceRegistration"));
        assertTrue(toString.contains("registrationId=1"));
        assertTrue(toString.contains("realName='张三'"));
    }
} 
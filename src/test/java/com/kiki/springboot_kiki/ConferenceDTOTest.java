package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.ConferenceDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceCategoryDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class ConferenceDTOTest {

    @Test
    public void testGetterSetterAndToString() {
        ConferenceDTO dto = new ConferenceDTO();
        
        // 测试所有 getter/setter 方法
        dto.setConferenceId(1);
        dto.setCategoryId(2);
        dto.setTitle("测试会议DTO");
        dto.setDescription("会议描述DTO");
        dto.setStartTime(LocalDateTime.now());
        dto.setEndTime(LocalDateTime.now().plusHours(2));
        dto.setLocation("会议室B");
        dto.setOrganizer("主办方DTO");
        dto.setMaxParticipants(200);
        dto.setCurrentParticipants(100);
        dto.setStatus(1);
        dto.setCoverImage("cover_dto.jpg");
        dto.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        
        ConferenceCategoryDTO category = new ConferenceCategoryDTO();
        category.setCategoryId(2);
        category.setCategoryName("技术会议");
        dto.setCategory(category);
        
        // 验证 getter 方法
        assertEquals(1, dto.getConferenceId());
        assertEquals(2, dto.getCategoryId());
        assertEquals("测试会议DTO", dto.getTitle());
        assertEquals("会议描述DTO", dto.getDescription());
        assertNotNull(dto.getStartTime());
        assertNotNull(dto.getEndTime());
        assertEquals("会议室B", dto.getLocation());
        assertEquals("主办方DTO", dto.getOrganizer());
        assertEquals(200, dto.getMaxParticipants());
        assertEquals(100, dto.getCurrentParticipants());
        assertEquals(1, dto.getStatus());
        assertEquals("cover_dto.jpg", dto.getCoverImage());
        assertNotNull(dto.getCreatedTime());
        assertNotNull(dto.getCategory());
        assertEquals(2, dto.getCategory().getCategoryId());
        
        // 测试 toString 方法
        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ConferenceDTO"));
        assertTrue(toString.contains("conferenceId=1"));
        assertTrue(toString.contains("title='测试会议DTO'"));
    }
    
    @Test
    public void testConstructor() {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        Timestamp createdTime = new Timestamp(System.currentTimeMillis());
        
        ConferenceDTO dto = new ConferenceDTO(1, 2, "构造测试", "描述", 
            startTime, endTime, "地点", "主办方", 100, 50, 1, "cover.jpg", createdTime);
        
        assertEquals(1, dto.getConferenceId());
        assertEquals(2, dto.getCategoryId());
        assertEquals("构造测试", dto.getTitle());
        assertEquals("描述", dto.getDescription());
        assertEquals(startTime, dto.getStartTime());
        assertEquals(endTime, dto.getEndTime());
        assertEquals("地点", dto.getLocation());
        assertEquals("主办方", dto.getOrganizer());
        assertEquals(100, dto.getMaxParticipants());
        assertEquals(50, dto.getCurrentParticipants());
        assertEquals(1, dto.getStatus());
        assertEquals("cover.jpg", dto.getCoverImage());
        assertEquals(createdTime, dto.getCreatedTime());
    }
} 
package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.Conference;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class ConferenceTest {

    @Test
    public void testGetterSetterAndToString() {
        Conference conference = new Conference();
        
        // 测试所有 getter/setter 方法
        conference.setConferenceId(1);
        conference.setCategoryId(2);
        conference.setTitle("测试会议");
        conference.setDescription("会议描述");
        conference.setStartTime(LocalDateTime.now());
        conference.setEndTime(LocalDateTime.now().plusHours(2));
        conference.setLocation("会议室A");
        conference.setOrganizer("主办方");
        conference.setMaxParticipants(100);
        conference.setCurrentParticipants(50);
        conference.setStatus(1);
        conference.setCoverImage("cover.jpg");
        conference.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        
        // 验证 getter 方法
        assertEquals(1, conference.getConferenceId());
        assertEquals(2, conference.getCategoryId());
        assertEquals("测试会议", conference.getTitle());
        assertEquals("会议描述", conference.getDescription());
        assertNotNull(conference.getStartTime());
        assertNotNull(conference.getEndTime());
        assertEquals("会议室A", conference.getLocation());
        assertEquals("主办方", conference.getOrganizer());
        assertEquals(100, conference.getMaxParticipants());
        assertEquals(50, conference.getCurrentParticipants());
        assertEquals(1, conference.getStatus());
        assertEquals("cover.jpg", conference.getCoverImage());
        assertNotNull(conference.getCreatedTime());
        
        // 测试 toString 方法
        String toString = conference.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Conference"));
        assertTrue(toString.contains("conferenceId=1"));
        assertTrue(toString.contains("title='测试会议'"));
    }
} 
package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.NewsDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class NewsDTOTest {

    @Test
    public void testGetterSetterAndToString() {
        NewsDTO dto = new NewsDTO();
        
        // 测试所有 getter/setter 方法
        dto.setTitle("新闻标题");
        dto.setSummary("新闻摘要");
        dto.setContent("新闻内容");
        dto.setCoverImage("news_cover.jpg");
        dto.setAuthor("作者");
        dto.setPublishTime(LocalDateTime.now());
        
        // 验证 getter 方法
        assertEquals("新闻标题", dto.getTitle());
        assertEquals("新闻摘要", dto.getSummary());
        assertEquals("新闻内容", dto.getContent());
        assertEquals("news_cover.jpg", dto.getCoverImage());
        assertEquals("作者", dto.getAuthor());
        assertNotNull(dto.getPublishTime());
        
        // 测试 toString 方法
        String toString = dto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("NewsDTO"));
        assertTrue(toString.contains("title='新闻标题'"));
        assertTrue(toString.contains("author='作者'"));
    }
} 
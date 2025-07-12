package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.IndustryNews;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class IndustryNewsTest {

    @Test
    public void testGetterSetterAndToString() {
        IndustryNews news = new IndustryNews();
        
        // 测试所有 getter/setter 方法
        news.setNewsId(1);
        news.setTitle("行业新闻标题");
        news.setSummary("新闻摘要");
        news.setContent("新闻内容");
        news.setCoverImage("news_cover.jpg");
        news.setAuthor("作者");
        news.setPublishTime(LocalDateTime.now());
        news.setViewCount(100);
        news.setStatus(1);
        news.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        news.setTop(1);
        news.setSortOrder(10);
        
        // 验证 getter 方法
        assertEquals(1, news.getNewsId());
        assertEquals("行业新闻标题", news.getTitle());
        assertEquals("新闻摘要", news.getSummary());
        assertEquals("新闻内容", news.getContent());
        assertEquals("news_cover.jpg", news.getCoverImage());
        assertEquals("作者", news.getAuthor());
        assertNotNull(news.getPublishTime());
        assertEquals(100, news.getViewCount());
        assertEquals(1, news.getStatus());
        assertNotNull(news.getCreatedTime());
        assertEquals(1, news.getTop());
        assertEquals(10, news.getSortOrder());
        
        // 测试 toString 方法
        String toString = news.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("IndustryNews"));
        assertTrue(toString.contains("newsId=1"));
        assertTrue(toString.contains("title='行业新闻标题'"));
    }
} 
package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.Conference;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceDTO;
import com.kiki.springboot_kiki.Pojo.IndustryNews;
import com.kiki.springboot_kiki.Pojo.DTO.NewsDetailDTO;
import com.kiki.springboot_kiki.util.DTOConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DTOConverterTest {
    @Test
    public void testToConferenceDTO() {
        Conference conf = new Conference();
        conf.setConferenceId(1);
        conf.setTitle("测试会议");
        ConferenceDTO dto = DTOConverter.toConferenceDTO(conf);
        assertNotNull(dto);
        assertEquals(1, dto.getConferenceId());
        assertEquals("测试会议", dto.getTitle());
    }

    @Test
    public void testToNewsDetailDTO() {
        IndustryNews news = new IndustryNews();
        news.setNewsId(1);
        news.setTitle("测试新闻");
        NewsDetailDTO dto = DTOConverter.toNewsDetailDTO(news);
        assertNotNull(dto);
        assertEquals(1, dto.getNewsId());
        assertEquals("测试新闻", dto.getTitle());
    }
} 
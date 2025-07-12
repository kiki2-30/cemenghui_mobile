package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.NewsDetailDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsStatsDTO;
import com.kiki.springboot_kiki.service.IndustryNewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IndustryNewsServiceTest {
    @Autowired
    private IndustryNewsService industryNewsService;

    @Test
    public void testGetNewsDetail() {
        NewsDetailDTO detail = industryNewsService.getNewsDetail(1);
        assertNotNull(detail);
        assertEquals(1, detail.getNewsId());
    }

    @Test
    public void testGetNewsStats() {
        NewsStatsDTO stats = industryNewsService.getNewsStats(1);
        assertNotNull(stats);
        assertEquals(1, stats.getNewsId());
    }

    @Test
    public void testGetNewsStatsBatch() {
        List<NewsStatsDTO> statsList = industryNewsService.getNewsStatsBatch(List.of(1,2));
        assertNotNull(statsList);
        assertTrue(statsList.size() >= 1);
    }
} 
package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.*;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import com.kiki.springboot_kiki.Pojo.IndustryNews;
import com.kiki.springboot_kiki.repository.IndustryNewsRepository;
import com.kiki.springboot_kiki.service.IIndustryNewsBehaviorService;
import com.kiki.springboot_kiki.service.IIndustryNewsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class IndustryNewsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IIndustryNewsService industryNewsService;
    @MockBean
    private IIndustryNewsBehaviorService industryNewsBehaviorService;
    @MockBean
    private IndustryNewsRepository industryNewsRepository;

    @Test
    void testGetNewsList() throws Exception {
        Mockito.when(industryNewsService.getNewsList(anyInt(), anyInt(), any(), eq(false))).thenReturn(new PageResult<>());
        mockMvc.perform(get("/api/news/list?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetPublicNewsList() throws Exception {
        Mockito.when(industryNewsService.getNewsList(anyInt(), anyInt(), any(), eq(true))).thenReturn(new PageResult<>());
        mockMvc.perform(get("/api/news/public-list?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetNewsDetail() throws Exception {
        NewsDetailDTO dto = new NewsDetailDTO();
        dto.setNewsId(1);
        Mockito.when(industryNewsService.getNewsDetail(1)).thenReturn(dto);
        mockMvc.perform(get("/api/news/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.newsId").value(1));
    }

    @Test
    void testGetNewsDetailNotFound() throws Exception {
        Mockito.when(industryNewsService.getNewsDetail(2)).thenReturn(null);
        mockMvc.perform(get("/api/news/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void testGetNewsStats() throws Exception {
        NewsStatsDTO stats = new NewsStatsDTO();
        stats.setNewsId(1);
        Mockito.when(industryNewsService.getNewsStats(1)).thenReturn(stats);
        mockMvc.perform(get("/api/news/1/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.newsId").value(1));
    }
} 
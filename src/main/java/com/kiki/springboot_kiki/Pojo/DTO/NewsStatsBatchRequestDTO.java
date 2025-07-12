package com.kiki.springboot_kiki.Pojo.DTO;

import java.util.List;

public class NewsStatsBatchRequestDTO {
    private List<Integer> newsIds;

    public NewsStatsBatchRequestDTO() {}

    public NewsStatsBatchRequestDTO(List<Integer> newsIds) {
        this.newsIds = newsIds;
    }

    public List<Integer> getNewsIds() {
        return newsIds;
    }

    public void setNewsIds(List<Integer> newsIds) {
        this.newsIds = newsIds;
    }
} 
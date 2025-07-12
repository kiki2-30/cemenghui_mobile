package com.kiki.springboot_kiki.Pojo.DTO;

public class NewsStatsDTO {
    private Integer newsId;
    private Long viewCount;
    private Long likeCount;
    private Long favoriteCount;

    public NewsStatsDTO() {}

    public NewsStatsDTO(Integer newsId, Long viewCount, Long likeCount, Long favoriteCount) {
        this.newsId = newsId;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.favoriteCount = favoriteCount;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Long favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
} 
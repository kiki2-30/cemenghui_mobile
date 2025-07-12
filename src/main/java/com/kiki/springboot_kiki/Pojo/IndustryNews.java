package com.kiki.springboot_kiki.Pojo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Table(name = "tb_industry_news")
@Entity
public class IndustryNews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Integer newsId;
    
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    
    @Column(name = "summary", length = 500)
    private String summary;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "cover_image", length = 500)
    private String coverImage;
    
    @Column(name = "author", length = 100)
    private String author;
    
    @Column(name = "publish_time")
    private LocalDateTime publishTime;
    
    @Column(name = "view_count")
    private Integer viewCount;
    
    @Column(name = "status")
    private Integer status;
    
    @Column(name = "created_time")
    private java.sql.Timestamp createdTime;

    @Column(name = "top")
    private Integer top; // 1-置顶 0-不置顶

    @Column(name = "sort_order")
    private Integer sortOrder; // 排序权重

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public java.sql.Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(java.sql.Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public String toString() {
        return "IndustryNews{" +
                "newsId=" + newsId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", author='" + author + '\'' +
                ", publishTime=" + publishTime +
                ", viewCount=" + viewCount +
                ", status=" + status +
                ", createdTime=" + createdTime +
                ", top=" + top +
                ", sortOrder=" + sortOrder +
                '}';
    }
} 
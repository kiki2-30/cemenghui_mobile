package com.kiki.springboot_kiki.Pojo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class NewsDTO {
    @NotBlank(message = "动态标题不能为空")
    @Size(min = 1, max = 200, message = "动态标题长度必须在1到200个字符之间")
    private String title;

    @Size(max = 500, message = "动态简介长度不能超过500个字符")
    private String summary;

    @Size(max = 10000, message = "动态内容长度不能超过10000个字符")
    private String content;

    private String coverImage;

    @Size(max = 100, message = "作者长度不能超过100个字符")
    private String author;

    private LocalDateTime publishTime;

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

    @Override
    public String toString() {
        return "NewsDTO{" +
                "title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", coverImage='" + coverImage + '\'' +
                ", author='" + author + '\'' +
                ", publishTime=" + publishTime +
                '}';
    }
} 
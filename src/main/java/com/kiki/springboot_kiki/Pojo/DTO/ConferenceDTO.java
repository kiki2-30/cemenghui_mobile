package com.kiki.springboot_kiki.Pojo.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class ConferenceDTO {
    // 请求字段
    @NotNull(message = "分类ID不能为空")
    private Integer categoryId;

    @NotBlank(message = "会议标题不能为空")
    @Size(min = 1, max = 200, message = "会议标题长度必须在1到200个字符之间")
    private String title;

    @Size(max = 2000, message = "会议描述长度不能超过2000个字符")
    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Size(max = 200, message = "地点长度不能超过200个字符")
    private String location;

    @Size(max = 200, message = "主办单位长度不能超过200个字符")
    private String organizer;

    private Integer maxParticipants;

    private String coverImage;

    // 响应字段
    private Integer conferenceId;
    private Integer currentParticipants;
    private Integer status;
    private java.sql.Timestamp createdTime;
    private ConferenceCategoryDTO category;

    // 构造函数
    public ConferenceDTO() {
    }

    // 用于响应的构造函数
    public ConferenceDTO(Integer conferenceId, Integer categoryId, String title, String description, 
                        LocalDateTime startTime, LocalDateTime endTime, String location, String organizer,
                        Integer maxParticipants, Integer currentParticipants, Integer status, 
                        String coverImage, java.sql.Timestamp createdTime) {
        this.conferenceId = conferenceId;
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.organizer = organizer;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.status = status;
        this.coverImage = coverImage;
        this.createdTime = createdTime;
    }

    // Getter和Setter方法
    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Integer getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(Integer currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public java.sql.Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(java.sql.Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public ConferenceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(ConferenceCategoryDTO category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ConferenceDTO{" +
                "conferenceId=" + conferenceId +
                ", categoryId=" + categoryId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", location='" + location + '\'' +
                ", organizer='" + organizer + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", currentParticipants=" + currentParticipants +
                ", status=" + status +
                ", coverImage='" + coverImage + '\'' +
                ", createdTime=" + createdTime +
                '}';
    }
} 
package com.kiki.springboot_kiki.Pojo.DTO;

import java.time.LocalDateTime;

/**
 * 用户登录响应DTO（包含token）
 */
public class UserLoginResponseDTO {
    
    private Integer userId;
    private String username;
    private String email;
    private String phone;
    private String realName;
    private String company;
    private String role;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String token;
    
    public UserLoginResponseDTO() {
    }
    
    public UserLoginResponseDTO(UserInfoDTO userInfo, String token, String role) {
        this.userId = userInfo.getUserId();
        this.username = userInfo.getUsername();
        this.email = userInfo.getEmail();
        this.phone = userInfo.getPhone();
        this.realName = userInfo.getRealName();
        this.company = userInfo.getCompany();
        this.role = role;
        this.createTime = userInfo.getCreateTime();
        this.updateTime = userInfo.getUpdateTime();
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
} 
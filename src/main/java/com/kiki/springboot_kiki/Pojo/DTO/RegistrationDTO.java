package com.kiki.springboot_kiki.Pojo.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public class RegistrationDTO {
    @NotNull(message = "会议ID不能为空")
    private Integer conferenceId;

    private Integer userId;

    @Size(max = 200, message = "单位长度不能超过200个字符")
    private String company;

    @NotBlank(message = "姓名不能为空")
    @Size(min = 1, max = 50, message = "姓名长度必须在1到50个字符之间")
    private String realName;

    private Integer gender;

    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;

    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @Size(max = 50, message = "到达方式长度不能超过50个字符")
    private String arrivalMethod;

    @Size(max = 100, message = "到达车次长度不能超过100个字符")
    private String arrivalTrain;

    private LocalDateTime arrivalTime;

    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Integer conferenceId) {
        this.conferenceId = conferenceId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArrivalMethod() {
        return arrivalMethod;
    }

    public void setArrivalMethod(String arrivalMethod) {
        this.arrivalMethod = arrivalMethod;
    }

    public String getArrivalTrain() {
        return arrivalTrain;
    }

    public void setArrivalTrain(String arrivalTrain) {
        this.arrivalTrain = arrivalTrain;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "conferenceId=" + conferenceId +
                ", userId=" + userId +
                ", company='" + company + '\'' +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", arrivalMethod='" + arrivalMethod + '\'' +
                ", arrivalTrain='" + arrivalTrain + '\'' +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
} 
package com.kiki.springboot_kiki.Pojo.DTO;

import java.time.LocalDateTime;

public class RegistrationResponseDTO {
    private Integer registrationId;
    private Integer conferenceId;
    private Integer userId;
    private String company;
    private String realName;
    private Integer gender;
    private String phone;
    private String email;
    private String arrivalMethod;
    private String arrivalTrain;
    private LocalDateTime arrivalTime;
    private java.sql.Timestamp registrationTime;

    public RegistrationResponseDTO() {
    }

    public RegistrationResponseDTO(Integer registrationId, Integer conferenceId, Integer userId, 
                                 String company, String realName, Integer gender, String phone, 
                                 String email, String arrivalMethod, String arrivalTrain, 
                                 LocalDateTime arrivalTime, java.sql.Timestamp registrationTime) {
        this.registrationId = registrationId;
        this.conferenceId = conferenceId;
        this.userId = userId;
        this.company = company;
        this.realName = realName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.arrivalMethod = arrivalMethod;
        this.arrivalTrain = arrivalTrain;
        this.arrivalTime = arrivalTime;
        this.registrationTime = registrationTime;
    }

    public Integer getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

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

    public java.sql.Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(java.sql.Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }
} 
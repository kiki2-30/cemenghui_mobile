package com.kiki.springboot_kiki.Pojo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Table(name = "tb_conference_registration")
@Entity
public class ConferenceRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registration_id")
    private Integer registrationId;
    
    @Column(name = "conference_id", nullable = false)
    private Integer conferenceId;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "company", length = 200)
    private String company;
    
    @Column(name = "real_name", nullable = false, length = 50)
    private String realName;
    
    @Column(name = "gender")
    private Integer gender;
    
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @Column(name = "arrival_method", length = 50)
    private String arrivalMethod;
    
    @Column(name = "arrival_train", length = 100)
    private String arrivalTrain;
    
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;
    
    @Column(name = "registration_time")
    private java.sql.Timestamp registrationTime;

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

    @Override
    public String toString() {
        return "ConferenceRegistration{" +
                "registrationId=" + registrationId +
                ", conferenceId=" + conferenceId +
                ", userId=" + userId +
                ", company='" + company + '\'' +
                ", realName='" + realName + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", arrivalMethod='" + arrivalMethod + '\'' +
                ", arrivalTrain='" + arrivalTrain + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", registrationTime=" + registrationTime +
                '}';
    }
} 
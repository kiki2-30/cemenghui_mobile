package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.DTO.RegistrationDTO;
import com.kiki.springboot_kiki.Pojo.DTO.RegistrationResponseDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceRegistrationStatsDTO;
import java.util.List;

public interface IConferenceRegistrationService {
    /**
     * 提交会议注册
     * @param registrationDTO 注册信息
     * @return 注册结果
     */
    RegistrationResponseDTO register(RegistrationDTO registrationDTO);
    
    /**
     * 根据会议ID获取注册列表
     * @param conferenceId 会议ID
     * @return 注册列表
     */
    List<RegistrationResponseDTO> getRegistrationsByConferenceId(Integer conferenceId);
    
    /**
     * 根据用户ID获取注册列表
     * @param userId 用户ID
     * @return 注册列表
     */
    List<RegistrationResponseDTO> getRegistrationsByUserId(Integer userId);
    
    /**
     * 检查用户是否已注册该会议
     * @param conferenceId 会议ID
     * @param phone 手机号
     * @return 是否已注册
     */
    boolean isAlreadyRegistered(Integer conferenceId, String phone);
    
    /**
     * 会议报名统计，支持时间范围筛选
     */
    ConferenceRegistrationStatsDTO getRegistrationStats(Integer conferenceId, String startDate, String endDate);
} 
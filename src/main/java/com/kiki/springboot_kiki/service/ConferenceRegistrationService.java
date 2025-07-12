package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.ConferenceRegistration;
import com.kiki.springboot_kiki.Pojo.DTO.RegistrationDTO;
import com.kiki.springboot_kiki.Pojo.DTO.RegistrationResponseDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceRegistrationStatsDTO;
import com.kiki.springboot_kiki.repository.ConferenceRegistrationRepository;
import com.kiki.springboot_kiki.util.DTOConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ConferenceRegistrationService implements IConferenceRegistrationService {

    @Autowired
    private ConferenceRegistrationRepository conferenceRegistrationRepository;

    @Autowired
    private IConferenceService conferenceService;

    @Override
    public RegistrationResponseDTO register(RegistrationDTO registrationDTO) {
        // 检查是否已注册
        if (isAlreadyRegistered(registrationDTO.getConferenceId(), registrationDTO.getPhone())) {
            throw new RuntimeException("您已经注册过该会议");
        }
        
        // 检查会议是否存在且状态正常
        if (conferenceService.getConferenceById(registrationDTO.getConferenceId()) == null) {
            throw new RuntimeException("会议不存在");
        }
        
        // 创建注册记录
        ConferenceRegistration registration = new ConferenceRegistration();
        BeanUtils.copyProperties(registrationDTO, registration);
        registration.setRegistrationTime(new java.sql.Timestamp(System.currentTimeMillis()));
        
        // 保存注册记录
        ConferenceRegistration savedRegistration = conferenceRegistrationRepository.save(registration);
        
        // 增加会议当前参与人数
        conferenceService.incrementCurrentParticipants(registrationDTO.getConferenceId());
        
        // 转换为DTO返回
        return DTOConverter.toRegistrationResponseDTO(savedRegistration);
    }

    @Override
    public List<RegistrationResponseDTO> getRegistrationsByConferenceId(Integer conferenceId) {
        Iterable<ConferenceRegistration> registrations = conferenceRegistrationRepository.findAll();
        List<ConferenceRegistration> registrationList = StreamSupport.stream(registrations.spliterator(), false)
                .filter(registration -> conferenceId.equals(registration.getConferenceId()))
                .collect(Collectors.toList());
        
        return registrationList.stream()
                .map(DTOConverter::toRegistrationResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistrationResponseDTO> getRegistrationsByUserId(Integer userId) {
        Iterable<ConferenceRegistration> registrations = conferenceRegistrationRepository.findAll();
        List<ConferenceRegistration> registrationList = StreamSupport.stream(registrations.spliterator(), false)
                .filter(registration -> userId != null && userId.equals(registration.getUserId()))
                .collect(Collectors.toList());
        
        return registrationList.stream()
                .map(DTOConverter::toRegistrationResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAlreadyRegistered(Integer conferenceId, String phone) {
        Iterable<ConferenceRegistration> registrations = conferenceRegistrationRepository.findAll();
        return StreamSupport.stream(registrations.spliterator(), false)
                .anyMatch(registration -> 
                    conferenceId.equals(registration.getConferenceId()) && 
                    phone.equals(registration.getPhone())
                );
    }

    @Override
    public ConferenceRegistrationStatsDTO getRegistrationStats(Integer conferenceId, String startDate, String endDate) {
        List<ConferenceRegistration> registrations;
        if (startDate != null && endDate != null) {
            // 解析日期字符串，假设格式为yyyy-MM-dd
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);
            registrations = conferenceRegistrationRepository.findByConferenceIdAndRegistrationTimeBetween(
                    conferenceId,
                    Timestamp.valueOf(start),
                    Timestamp.valueOf(end)
            );
        } else {
            registrations = conferenceRegistrationRepository.findByConferenceId(conferenceId);
        }
        List<RegistrationResponseDTO> dtoList = registrations.stream()
                .map(DTOConverter::toRegistrationResponseDTO)
                .collect(Collectors.toList());
        return new ConferenceRegistrationStatsDTO(dtoList.size(), dtoList);
    }
} 
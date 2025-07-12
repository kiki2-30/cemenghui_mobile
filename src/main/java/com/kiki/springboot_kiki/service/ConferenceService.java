package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.Conference;
import com.kiki.springboot_kiki.Pojo.ConferenceCategory;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceEditDTO;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceCategoryDTO;
import com.kiki.springboot_kiki.repository.ConferenceRepository;
import com.kiki.springboot_kiki.repository.ConferenceCategoryRepository;
import com.kiki.springboot_kiki.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ConferenceService implements IConferenceService {

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private ConferenceCategoryRepository conferenceCategoryRepository;

    @Autowired
    private StandardDetailService standardDetailService;
    @Autowired
    private TrainingDetailService trainingDetailService;
    @Autowired
    private ToolDetailService toolDetailService;

    @Override
    public PageResult<ConferenceDTO> getConferenceList(int page, int size, Integer categoryId, String keyword) {
        // 创建分页请求
        Pageable pageable = PageRequest.of(page, size);
        
        // 获取所有会议数据
        Iterable<Conference> allConferences = conferenceRepository.findAll();
        List<Conference> conferenceList = StreamSupport.stream(allConferences.spliterator(), false)
                .collect(Collectors.toList());
        
        // 根据条件过滤
        List<Conference> filteredList = conferenceList.stream()
                .filter(conference -> {
                    // 过滤状态为正常的会议
                    if (conference.getStatus() == null || conference.getStatus() != 1) {
                        return false;
                    }
                    
                    // 按分类过滤
                    if (categoryId != null && !categoryId.equals(conference.getCategoryId())) {
                        return false;
                    }
                    
                    // 按关键词搜索
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        String searchKeyword = keyword.toLowerCase();
                        return (conference.getTitle() != null && conference.getTitle().toLowerCase().contains(searchKeyword)) ||
                               (conference.getDescription() != null && conference.getDescription().toLowerCase().contains(searchKeyword)) ||
                               (conference.getLocation() != null && conference.getLocation().toLowerCase().contains(searchKeyword)) ||
                               (conference.getOrganizer() != null && conference.getOrganizer().toLowerCase().contains(searchKeyword));
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
        
        // 计算分页
        int totalElements = filteredList.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);
        
        List<Conference> pageContent = filteredList.subList(startIndex, endIndex);
        
        // 转换为DTO
        List<ConferenceDTO> dtoContent = pageContent.stream()
                .map(DTOConverter::toConferenceDTO)
                .collect(Collectors.toList());
        
        return new PageResult<>(
                dtoContent,
                totalElements,
                totalPages,
                page,
                size,
                page < totalPages - 1,
                page > 0
        );
    }

    @Override
    public ConferenceDTO getConferenceById(Integer conferenceId) {
        Optional<Conference> conference = conferenceRepository.findById(conferenceId);
        return conference.map(DTOConverter::toConferenceDTO).orElse(null);
    }

    @Override
    public List<ConferenceDTO> getHotConferences(int limit) {
        // 获取所有正常状态的会议
        Iterable<Conference> allConferences = conferenceRepository.findAll();
        List<Conference> conferenceList = StreamSupport.stream(allConferences.spliterator(), false)
                .filter(conference -> conference.getStatus() != null && conference.getStatus() == 1)
                .collect(Collectors.toList());
        
        // 按当前参与人数排序，取前limit个
        List<Conference> hotConferences = conferenceList.stream()
                .sorted((c1, c2) -> {
                    Integer participants1 = c1.getCurrentParticipants() != null ? c1.getCurrentParticipants() : 0;
                    Integer participants2 = c2.getCurrentParticipants() != null ? c2.getCurrentParticipants() : 0;
                    return participants2.compareTo(participants1); // 降序排列
                })
                .limit(limit)
                .collect(Collectors.toList());
        
        // 转换为DTO
        return hotConferences.stream()
                .map(DTOConverter::toConferenceDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean incrementCurrentParticipants(Integer conferenceId) {
        Optional<Conference> conferenceOpt = conferenceRepository.findById(conferenceId);
        if (conferenceOpt.isPresent()) {
            Conference conference = conferenceOpt.get();
            Integer currentParticipants = conference.getCurrentParticipants();
            conference.setCurrentParticipants(currentParticipants != null ? currentParticipants + 1 : 1);
            conferenceRepository.save(conference);
            return true;
        }
        return false;
    }

    @Override
    public Integer addConference(ConferenceEditDTO dto) {
        Conference conf = new Conference();
        conf.setTitle(dto.getTitle());
        conf.setDescription(dto.getContent());
        conf.setStartTime(dto.getStartTime());
        conf.setEndTime(dto.getEndTime());
        conf.setLocation(dto.getLocation());
        conf.setOrganizer(dto.getOrganizer());
        conf.setCategoryId(dto.getCategoryId());
        conf.setCoverImage(dto.getCoverImage());
        conf.setStatus(dto.getStatus());
        conf.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        Conference saved = conferenceRepository.save(conf);
        return saved.getConferenceId();
    }

    @Override
    public void updateConference(Integer conferenceId, ConferenceEditDTO dto) {
        Conference conf = conferenceRepository.findById(conferenceId).orElseThrow(() -> new IllegalArgumentException("会议不存在"));
        conf.setTitle(dto.getTitle());
        conf.setDescription(dto.getContent());
        conf.setStartTime(dto.getStartTime());
        conf.setEndTime(dto.getEndTime());
        conf.setLocation(dto.getLocation());
        conf.setOrganizer(dto.getOrganizer());
        conf.setCategoryId(dto.getCategoryId());
        conf.setCoverImage(dto.getCoverImage());
        conf.setStatus(dto.getStatus());
        conferenceRepository.save(conf);
    }

    @Override
    public void deleteConference(Integer conferenceId) {
        standardDetailService.deleteByConferenceId(conferenceId);
        trainingDetailService.deleteByConferenceId(conferenceId);
        toolDetailService.deleteByConferenceId(conferenceId);
        conferenceRepository.deleteById(conferenceId);
    }
} 
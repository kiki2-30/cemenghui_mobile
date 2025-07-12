package com.kiki.springboot_kiki.util;

import com.kiki.springboot_kiki.Pojo.Conference;
import com.kiki.springboot_kiki.Pojo.ConferenceCategory;
import com.kiki.springboot_kiki.Pojo.ConferenceRegistration;
import com.kiki.springboot_kiki.Pojo.IndustryNews;
import com.kiki.springboot_kiki.Pojo.UserBehavior;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceCategoryDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsDetailDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsListDTO;
import com.kiki.springboot_kiki.Pojo.DTO.RegistrationResponseDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserBehaviorDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DTOConverter {

    /**
     * 将ConferenceCategory实体转换为ConferenceCategoryDTO
     */
    public static ConferenceCategoryDTO toCategoryDTO(ConferenceCategory category) {
        if (category == null) {
            return null;
        }
        return new ConferenceCategoryDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getCategoryDesc(),
                category.getSortOrder()
        );
    }

    /**
     * 将Conference实体转换为ConferenceDTO
     */
    public static ConferenceDTO toConferenceDTO(Conference conference) {
        if (conference == null) {
            return null;
        }
        
        return new ConferenceDTO(
                conference.getConferenceId(),
                conference.getCategoryId(),
                conference.getTitle(),
                conference.getDescription(),
                conference.getStartTime(),
                conference.getEndTime(),
                conference.getLocation(),
                conference.getOrganizer(),
                conference.getMaxParticipants(),
                conference.getCurrentParticipants(),
                conference.getStatus(),
                conference.getCoverImage(),
                conference.getCreatedTime()
        );
    }

    /**
     * 将ConferenceRegistration实体转换为RegistrationResponseDTO
     */
    public static RegistrationResponseDTO toRegistrationResponseDTO(ConferenceRegistration registration) {
        if (registration == null) {
            return null;
        }
        return new RegistrationResponseDTO(
                registration.getRegistrationId(),
                registration.getConferenceId(),
                registration.getUserId(),
                registration.getCompany(),
                registration.getRealName(),
                registration.getGender(),
                registration.getPhone(),
                registration.getEmail(),
                registration.getArrivalMethod(),
                registration.getArrivalTrain(),
                registration.getArrivalTime(),
                registration.getRegistrationTime()
        );
    }

    /**
     * 将ConferenceCategory列表转换为ConferenceCategoryDTO列表
     */
    public static List<ConferenceCategoryDTO> toCategoryDTOList(List<ConferenceCategory> categories) {
        if (categories == null) {
            return null;
        }
        return categories.stream()
                .map(DTOConverter::toCategoryDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将Conference列表转换为ConferenceDTO列表
     */
    public static List<ConferenceDTO> toConferenceDTOList(List<Conference> conferences) {
        if (conferences == null) {
            return null;
        }
        return conferences.stream()
                .map(DTOConverter::toConferenceDTO)
                .collect(Collectors.toList());
    }

    /**
     * 将ConferenceRegistration列表转换为RegistrationResponseDTO列表
     */
    public static List<RegistrationResponseDTO> toRegistrationResponseDTOList(List<ConferenceRegistration> registrations) {
        if (registrations == null) {
            return null;
        }
        return registrations.stream()
                .map(DTOConverter::toRegistrationResponseDTO)
                .collect(Collectors.toList());
    }

    public static NewsListDTO toNewsListDTO(IndustryNews news) {
        if (news == null) return null;
        NewsListDTO dto = new NewsListDTO();
        dto.setNewsId(news.getNewsId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setCoverImage(news.getCoverImage());
        dto.setPublishTime(news.getPublishTime());
        dto.setViewCount(news.getViewCount());
        dto.setSortOrder(news.getSortOrder());
        dto.setTop(news.getTop());
        dto.setStatus(news.getStatus());
        return dto;
    }

    public static NewsDetailDTO toNewsDetailDTO(IndustryNews news) {
        if (news == null) return null;
        NewsDetailDTO dto = new NewsDetailDTO();
        dto.setNewsId(news.getNewsId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setContent(news.getContent());
        dto.setCoverImage(news.getCoverImage());
        dto.setAuthor(news.getAuthor());
        dto.setPublishTime(news.getPublishTime());
        dto.setViewCount(news.getViewCount());
        dto.setStatus(news.getStatus());
        dto.setCreatedTime(news.getCreatedTime());
        return dto;
    }

    public static UserBehaviorDTO toUserBehaviorDTO(UserBehavior behavior) {
        if (behavior == null) return null;
        UserBehaviorDTO dto = new UserBehaviorDTO();
        dto.setBehaviorId(behavior.getBehaviorId());
        dto.setUserId(behavior.getUserId());
        dto.setBehaviorType(behavior.getBehaviorType());
        dto.setTargetType(behavior.getTargetType());
        dto.setTargetId(behavior.getTargetId());
        dto.setIpAddress(behavior.getIpAddress());
        dto.setUserAgent(behavior.getUserAgent());
        dto.setCreatedTime(behavior.getCreatedTime());
        return dto;
    }
} 
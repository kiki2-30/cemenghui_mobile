package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.Conference;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceDTO;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceEditDTO;
import java.util.List;

public interface IConferenceService {
    /**
     * 分页获取会议列表
     * @param page 页码（从0开始）
     * @param size 每页大小
     * @param categoryId 分类ID（可选）
     * @param keyword 搜索关键词（可选）
     * @return 分页结果
     */
    PageResult<ConferenceDTO> getConferenceList(int page, int size, Integer categoryId, String keyword);
    
    /**
     * 根据ID获取会议详情
     * @param conferenceId 会议ID
     * @return 会议详情
     */
    ConferenceDTO getConferenceById(Integer conferenceId);
    
    /**
     * 获取热门会议列表
     * @param limit 限制数量
     * @return 热门会议列表
     */
    List<ConferenceDTO> getHotConferences(int limit);
    
    /**
     * 增加会议当前参与人数
     * @param conferenceId 会议ID
     * @return 是否成功
     */
    boolean incrementCurrentParticipants(Integer conferenceId);

    /**
     * 新增会议
     */
    Integer addConference(ConferenceEditDTO dto);

    /**
     * 编辑会议
     */
    void updateConference(Integer conferenceId, ConferenceEditDTO dto);

    /**
     * 删除会议
     */
    void deleteConference(Integer conferenceId);
} 
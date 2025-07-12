package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.DTO.UserBehaviorDTO;
import java.util.List;
import com.kiki.springboot_kiki.Pojo.DTO.BehaviorTrendDTO;

public interface IIndustryNewsBehaviorService {
    /**
     * 记录行业动态浏览行为
     * @param newsId 动态ID
     * @param userId 用户ID（可选）
     * @param ipAddress IP地址
     * @param userAgent User-Agent
     * @return 行为DTO
     */
    UserBehaviorDTO recordView(Integer newsId, Integer userId, String ipAddress, String userAgent);

    /**
     * 点赞
     */
    UserBehaviorDTO like(Integer newsId, Integer userId, String ipAddress, String userAgent);

    /**
     * 取消点赞
     */
    boolean unlike(Integer newsId, Integer userId);

    /**
     * 收藏
     */
    UserBehaviorDTO favorite(Integer newsId, Integer userId, String ipAddress, String userAgent);

    /**
     * 取消收藏
     */
    boolean unfavorite(Integer newsId, Integer userId);

    /**
     * 查询点赞状态
     */
    boolean isLiked(Integer newsId, Integer userId);

    /**
     * 查询收藏状态
     */
    boolean isFavorited(Integer newsId, Integer userId);

    /**
     * 按天统计用户行为趋势
     */
    List<BehaviorTrendDTO> getBehaviorTrend(String behaviorType, String startDate, String endDate);
} 
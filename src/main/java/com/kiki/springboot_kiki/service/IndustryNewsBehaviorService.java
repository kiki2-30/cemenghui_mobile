package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.UserBehavior;
import com.kiki.springboot_kiki.Pojo.DTO.UserBehaviorDTO;
import com.kiki.springboot_kiki.repository.UserBehaviorRepository;
import com.kiki.springboot_kiki.repository.UserRepository;
import com.kiki.springboot_kiki.util.DTOConverter;
import com.kiki.springboot_kiki.Pojo.DTO.BehaviorTrendDTO;
import java.util.List;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.StreamSupport;

@Service
public class IndustryNewsBehaviorService implements IIndustryNewsBehaviorService {

    @Autowired
    private UserBehaviorRepository userBehaviorRepository;

    @Autowired
    private UserRepository userRepository;

    private void checkUserExists(Integer userId) {
        if (userId == null || !userRepository.existsById(userId)) {
            throw new IllegalArgumentException("用户ID不存在，请先注册用户");
        }
    }

    @Override
    public UserBehaviorDTO recordView(Integer newsId, Integer userId, String ipAddress, String userAgent) {
        checkUserExists(userId);
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setBehaviorType("view");
        behavior.setTargetType("news");
        behavior.setTargetId(newsId);
        behavior.setIpAddress(ipAddress);
        behavior.setUserAgent(userAgent);
        behavior.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        UserBehavior saved = userBehaviorRepository.save(behavior);
        return DTOConverter.toUserBehaviorDTO(saved);
    }

    @Override
    public UserBehaviorDTO like(Integer newsId, Integer userId, String ipAddress, String userAgent) {
        checkUserExists(userId);
        // 已点赞则不重复插入
        if (isLiked(newsId, userId)) {
            return null;
        }
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setBehaviorType("like");
        behavior.setTargetType("news");
        behavior.setTargetId(newsId);
        behavior.setIpAddress(ipAddress);
        behavior.setUserAgent(userAgent);
        behavior.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        UserBehavior saved = userBehaviorRepository.save(behavior);
        return DTOConverter.toUserBehaviorDTO(saved);
    }

    @Override
    public boolean unlike(Integer newsId, Integer userId) {
        checkUserExists(userId);
        Iterable<UserBehavior> all = userBehaviorRepository.findAll();
        for (UserBehavior behavior : all) {
            if (userId != null && userId.equals(behavior.getUserId()) &&
                newsId.equals(behavior.getTargetId()) &&
                "like".equals(behavior.getBehaviorType()) &&
                "news".equals(behavior.getTargetType())) {
                userBehaviorRepository.delete(behavior);
                return true;
            }
        }
        return false;
    }

    @Override
    public UserBehaviorDTO favorite(Integer newsId, Integer userId, String ipAddress, String userAgent) {
        checkUserExists(userId);
        if (isFavorited(newsId, userId)) {
            return null;
        }
        UserBehavior behavior = new UserBehavior();
        behavior.setUserId(userId);
        behavior.setBehaviorType("favorite");
        behavior.setTargetType("news");
        behavior.setTargetId(newsId);
        behavior.setIpAddress(ipAddress);
        behavior.setUserAgent(userAgent);
        behavior.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
        UserBehavior saved = userBehaviorRepository.save(behavior);
        return DTOConverter.toUserBehaviorDTO(saved);
    }

    @Override
    public boolean unfavorite(Integer newsId, Integer userId) {
        checkUserExists(userId);
        Iterable<UserBehavior> all = userBehaviorRepository.findAll();
        for (UserBehavior behavior : all) {
            if (userId != null && userId.equals(behavior.getUserId()) &&
                newsId.equals(behavior.getTargetId()) &&
                "favorite".equals(behavior.getBehaviorType()) &&
                "news".equals(behavior.getTargetType())) {
                userBehaviorRepository.delete(behavior);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isLiked(Integer newsId, Integer userId) {
        checkUserExists(userId);
        Iterable<UserBehavior> all = userBehaviorRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .anyMatch(behavior -> userId != null && userId.equals(behavior.getUserId()) &&
                        newsId.equals(behavior.getTargetId()) &&
                        "like".equals(behavior.getBehaviorType()) &&
                        "news".equals(behavior.getTargetType()));
    }

    @Override
    public boolean isFavorited(Integer newsId, Integer userId) {
        checkUserExists(userId);
        Iterable<UserBehavior> all = userBehaviorRepository.findAll();
        return StreamSupport.stream(all.spliterator(), false)
                .anyMatch(behavior -> userId != null && userId.equals(behavior.getUserId()) &&
                        newsId.equals(behavior.getTargetId()) &&
                        "favorite".equals(behavior.getBehaviorType()) &&
                        "news".equals(behavior.getTargetType()));
    }

    @Override
    public List<BehaviorTrendDTO> getBehaviorTrend(String behaviorType, String startDate, String endDate) {
        Timestamp start = null, end = null;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (startDate != null && !startDate.isEmpty()) {
            start = Timestamp.valueOf(LocalDate.parse(startDate, dtf).atStartOfDay());
        }
        if (endDate != null && !endDate.isEmpty()) {
            end = Timestamp.valueOf(LocalDate.parse(endDate, dtf).plusDays(1).atStartOfDay().minusNanos(1));
        }
        List<Object[]> rows = userBehaviorRepository.countBehaviorTrendByDay(behaviorType, start, end);
        List<BehaviorTrendDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            result.add(new BehaviorTrendDTO((String)row[0], (Long)row[1]));
        }
        return result;
    }
} 
package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.ConferenceRegistration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ConferenceRegistrationRepository extends CrudRepository<ConferenceRegistration, Integer> {
    List<ConferenceRegistration> findByConferenceId(Integer conferenceId);
    List<ConferenceRegistration> findByConferenceIdAndRegistrationTimeBetween(Integer conferenceId, Timestamp start, Timestamp end);
    
    /**
     * 根据用户ID删除会议注册记录
     */
    void deleteByUserId(Integer userId);
    
    /**
     * 根据用户ID查找会议注册记录
     */
    List<ConferenceRegistration> findByUserId(Integer userId);
} 
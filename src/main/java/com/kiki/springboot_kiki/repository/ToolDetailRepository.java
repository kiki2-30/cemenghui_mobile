package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.ToolDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolDetailRepository extends CrudRepository<ToolDetail, Integer> {
    ToolDetail findByConferenceId(Integer conferenceId);
} 
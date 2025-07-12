package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.StandardDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandardDetailRepository extends CrudRepository<StandardDetail, Integer> {
    StandardDetail findByConferenceId(Integer conferenceId);
} 
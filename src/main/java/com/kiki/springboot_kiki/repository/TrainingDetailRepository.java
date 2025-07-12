package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.TrainingDetail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingDetailRepository extends CrudRepository<TrainingDetail, Integer> {
    TrainingDetail findByConferenceId(Integer conferenceId);
} 
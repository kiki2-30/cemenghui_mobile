package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.TrainingDetail;
import com.kiki.springboot_kiki.repository.TrainingDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingDetailService {
    @Autowired
    private TrainingDetailRepository trainingDetailRepository;

    public TrainingDetail getByConferenceId(Integer conferenceId) {
        return trainingDetailRepository.findByConferenceId(conferenceId);
    }

    public TrainingDetail addOrUpdate(TrainingDetail detail) {
        TrainingDetail existing = trainingDetailRepository.findByConferenceId(detail.getConferenceId());
        if (existing != null) {
            // 更新所有结构化字段
            existing.setStandardName(detail.getStandardName());
            existing.setStandardDesc(detail.getStandardDesc());
            existing.setRequirements(detail.getRequirements());
            existing.setProcess(detail.getProcess());
            existing.setTimeline(detail.getTimeline());
            return trainingDetailRepository.save(existing);
        } else {
            return trainingDetailRepository.save(detail);
        }
    }

    public void deleteByConferenceId(Integer conferenceId) {
        TrainingDetail detail = trainingDetailRepository.findByConferenceId(conferenceId);
        if (detail != null) {
            trainingDetailRepository.delete(detail);
        }
    }
} 
package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.StandardDetail;
import com.kiki.springboot_kiki.repository.StandardDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StandardDetailService {
    @Autowired
    private StandardDetailRepository standardDetailRepository;

    public StandardDetail getByConferenceId(Integer conferenceId) {
        return standardDetailRepository.findByConferenceId(conferenceId);
    }

    public StandardDetail addOrUpdate(StandardDetail detail) {
        StandardDetail existing = standardDetailRepository.findByConferenceId(detail.getConferenceId());
        if (existing != null) {
            existing.setStandardName(detail.getStandardName());
            existing.setStandardDesc(detail.getStandardDesc());
            existing.setRequirements(detail.getRequirements());
            existing.setProcess(detail.getProcess());
            existing.setTimeline(detail.getTimeline());
            return standardDetailRepository.save(existing);
        } else {
            return standardDetailRepository.save(detail);
        }
    }

    public void deleteByConferenceId(Integer conferenceId) {
        StandardDetail detail = standardDetailRepository.findByConferenceId(conferenceId);
        if (detail != null) {
            standardDetailRepository.delete(detail);
        }
    }
} 
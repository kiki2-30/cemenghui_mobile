package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.ToolDetail;
import com.kiki.springboot_kiki.repository.ToolDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ToolDetailService {
    @Autowired
    private ToolDetailRepository toolDetailRepository;

    public ToolDetail getByConferenceId(Integer conferenceId) {
        return toolDetailRepository.findByConferenceId(conferenceId);
    }

    public ToolDetail addOrUpdate(ToolDetail detail) {
        ToolDetail existing = toolDetailRepository.findByConferenceId(detail.getConferenceId());
        if (existing != null) {
            // 更新所有结构化字段
            existing.setStandardName(detail.getStandardName());
            existing.setStandardDesc(detail.getStandardDesc());
            existing.setRequirements(detail.getRequirements());
            existing.setProcess(detail.getProcess());
            existing.setTimeline(detail.getTimeline());
            return toolDetailRepository.save(existing);
        } else {
            return toolDetailRepository.save(detail);
        }
    }

    public void deleteByConferenceId(Integer conferenceId) {
        ToolDetail detail = toolDetailRepository.findByConferenceId(conferenceId);
        if (detail != null) {
            toolDetailRepository.delete(detail);
        }
    }
} 
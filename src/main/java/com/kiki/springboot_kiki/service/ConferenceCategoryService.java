package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.ConferenceCategory;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceCategoryDTO;
import com.kiki.springboot_kiki.repository.ConferenceCategoryRepository;
import com.kiki.springboot_kiki.util.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConferenceCategoryService implements IConferenceCategoryService {

    @Autowired
    private ConferenceCategoryRepository conferenceCategoryRepository;

    @Override
    public List<ConferenceCategoryDTO> getAllCategories() {
        // 获取所有分类并按排序字段排序
        List<ConferenceCategory> categories = (List<ConferenceCategory>) conferenceCategoryRepository.findAll();
        // 按sortOrder排序
        categories.sort((c1, c2) -> {
            Integer order1 = c1.getSortOrder() != null ? c1.getSortOrder() : 0;
            Integer order2 = c2.getSortOrder() != null ? c2.getSortOrder() : 0;
            return order1.compareTo(order2);
        });
        
        // 转换为DTO
        return categories.stream()
                .map(DTOConverter::toCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ConferenceCategoryDTO getCategoryById(Integer categoryId) {
        Optional<ConferenceCategory> category = conferenceCategoryRepository.findById(categoryId);
        return category.map(DTOConverter::toCategoryDTO).orElse(null);
    }
} 
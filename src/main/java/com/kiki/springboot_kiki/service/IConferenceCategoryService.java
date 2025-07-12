package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.DTO.ConferenceCategoryDTO;
import java.util.List;

public interface IConferenceCategoryService {
    /**
     * 获取所有会议分类列表
     * @return 分类列表
     */
    List<ConferenceCategoryDTO> getAllCategories();
    
    /**
     * 根据ID获取分类
     * @param categoryId 分类ID
     * @return 分类信息
     */
    ConferenceCategoryDTO getCategoryById(Integer categoryId);
} 
package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.DTO.NewsDetailDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsListDTO;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.Pojo.DTO.NewsStatsDTO;
import java.util.List;
import com.kiki.springboot_kiki.Pojo.DTO.NewsEditDTO;

public interface IIndustryNewsService {
    /**
     * 分页获取行业动态列表，支持模糊搜索
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可选）
     * @return 分页结果
     */
    PageResult<NewsListDTO> getNewsList(int page, int size, String keyword);

    /**
     * 分页获取行业动态列表，支持模糊搜索和状态过滤
     * @param page 页码（从0开始）
     * @param size 每页数量
     * @param keyword 搜索关键词（可选）
     * @param onlyPublished 是否只显示已发布的新闻
     * @return 分页结果
     */
    PageResult<NewsListDTO> getNewsList(int page, int size, String keyword, boolean onlyPublished);

    /**
     * 获取行业动态详情
     * @param newsId 动态ID
     * @return 详情DTO
     */
    NewsDetailDTO getNewsDetail(Integer newsId);

    /**
     * 获取某条新闻的统计数据（浏览量、点赞量、收藏量）
     */
    NewsStatsDTO getNewsStats(Integer newsId);

    /**
     * 批量获取新闻统计数据
     */
    List<NewsStatsDTO> getNewsStatsBatch(List<Integer> newsIds);

    /**
     * 新增行业动态
     */
    Integer addNews(NewsEditDTO dto);

    /**
     * 编辑行业动态
     */
    void updateNews(Integer newsId, NewsEditDTO dto);

    /**
     * 删除行业动态
     */
    void deleteNews(Integer newsId);

    /**
     * 修改行业动态上下架状态
     */
    void updateNewsStatus(Integer newsId, Integer status);

    /**
     * 设置/取消置顶
     */
    void updateNewsTop(Integer newsId, Integer top);

    /**
     * 修改排序权重
     */
    void updateNewsSortOrder(Integer newsId, Integer sortOrder);
} 
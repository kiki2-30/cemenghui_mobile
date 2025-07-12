package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.IndustryNews;
import com.kiki.springboot_kiki.Pojo.DTO.NewsDetailDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsListDTO;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.repository.IndustryNewsRepository;
import com.kiki.springboot_kiki.repository.UserBehaviorRepository;
import com.kiki.springboot_kiki.util.DTOConverter;
import com.kiki.springboot_kiki.Pojo.DTO.NewsStatsDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsEditDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.time.LocalDateTime;

@Service
public class IndustryNewsService implements IIndustryNewsService {

    @Autowired
    private IndustryNewsRepository industryNewsRepository;

    @Autowired
    private UserBehaviorRepository userBehaviorRepository;

    @Override
    public PageResult<NewsListDTO> getNewsList(int page, int size, String keyword) {
        return getNewsList(page, size, keyword, false);
    }

    @Override
    public PageResult<NewsListDTO> getNewsList(int page, int size, String keyword, boolean onlyPublished) {
        Iterable<IndustryNews> allNews = industryNewsRepository.findAll();
        List<IndustryNews> newsList = StreamSupport.stream(allNews.spliterator(), false)
                .filter(news -> !onlyPublished || (news.getStatus() != null && news.getStatus() == 1))
                .filter(news -> {
                    if (keyword == null || keyword.trim().isEmpty()) return true;
                    String k = keyword.toLowerCase();
                    return (news.getTitle() != null && news.getTitle().toLowerCase().contains(k))
                            || (news.getSummary() != null && news.getSummary().toLowerCase().contains(k));
                })
                .sorted(Comparator.comparing(IndustryNews::getTop, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(IndustryNews::getSortOrder, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(IndustryNews::getPublishTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());

        int totalElements = newsList.size();
        int totalPages = (int) Math.ceil((double) totalElements / size);
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);
        List<IndustryNews> pageContent = newsList.subList(startIndex, endIndex);
        List<NewsListDTO> dtoList = pageContent.stream().map(DTOConverter::toNewsListDTO).collect(Collectors.toList());

        return new PageResult<>(
                dtoList,
                totalElements,
                totalPages,
                page,
                size,
                page < totalPages - 1,
                page > 0
        );
    }

    @Override
    public NewsDetailDTO getNewsDetail(Integer newsId) {
        Optional<IndustryNews> newsOpt = industryNewsRepository.findById(newsId);
        return newsOpt.map(DTOConverter::toNewsDetailDTO).orElse(null);
    }

    @Override
    public NewsStatsDTO getNewsStats(Integer newsId) {
        Long viewCount = userBehaviorRepository.countByNewsIdAndBehaviorType(newsId, "view");
        Long likeCount = userBehaviorRepository.countByNewsIdAndBehaviorType(newsId, "like");
        Long favoriteCount = userBehaviorRepository.countByNewsIdAndBehaviorType(newsId, "favorite");
        return new NewsStatsDTO(newsId, viewCount, likeCount, favoriteCount);
    }

    @Override
    public List<NewsStatsDTO> getNewsStatsBatch(List<Integer> newsIds) {
        if (newsIds == null || newsIds.isEmpty()) {
            return List.of();
        }
        return newsIds.stream()
                .map(this::getNewsStats)
                .collect(Collectors.toList());
    }

    @Override
    public Integer addNews(NewsEditDTO dto) {
        IndustryNews news = new IndustryNews();
        news.setTitle(dto.getTitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setCoverImage(dto.getImageUrl());
        if (dto.getPublishTime() != null) {
            news.setPublishTime(dto.getPublishTime().toLocalDateTime());
        }
        news.setStatus(dto.getStatus());
        IndustryNews saved = industryNewsRepository.save(news);
        return saved.getNewsId();
    }

    @Override
    public void updateNews(Integer newsId, NewsEditDTO dto) {
        IndustryNews news = industryNewsRepository.findById(newsId).orElseThrow(() -> new IllegalArgumentException("新闻不存在"));
        news.setTitle(dto.getTitle());
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setCoverImage(dto.getImageUrl());
        if (dto.getPublishTime() != null) {
            news.setPublishTime(dto.getPublishTime().toLocalDateTime());
        }
        news.setStatus(dto.getStatus());
        industryNewsRepository.save(news);
    }

    @Override
    public void deleteNews(Integer newsId) {
        industryNewsRepository.deleteById(newsId);
    }

    @Override
    public void updateNewsStatus(Integer newsId, Integer status) {
        IndustryNews news = industryNewsRepository.findById(newsId).orElseThrow(() -> new IllegalArgumentException("新闻不存在"));
        news.setStatus(status);
        industryNewsRepository.save(news);
    }

    @Override
    public void updateNewsTop(Integer newsId, Integer top) {
        IndustryNews news = industryNewsRepository.findById(newsId).orElseThrow(() -> new IllegalArgumentException("新闻不存在"));
        news.setTop(top);
        industryNewsRepository.save(news);
    }

    @Override
    public void updateNewsSortOrder(Integer newsId, Integer sortOrder) {
        IndustryNews news = industryNewsRepository.findById(newsId).orElseThrow(() -> new IllegalArgumentException("新闻不存在"));
        news.setSortOrder(sortOrder);
        industryNewsRepository.save(news);
    }
} 
package com.kiki.springboot_kiki.controller;

import com.kiki.springboot_kiki.Pojo.DTO.NewsDetailDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsListDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserBehaviorDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsStatsDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsStatsBatchRequestDTO;
import com.kiki.springboot_kiki.Pojo.DTO.NewsEditDTO;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import com.kiki.springboot_kiki.Pojo.DTO.StatusDTO;
import com.kiki.springboot_kiki.Pojo.DTO.TopDTO;
import com.kiki.springboot_kiki.Pojo.DTO.SortOrderDTO;
import com.kiki.springboot_kiki.Pojo.DTO.BehaviorTrendDTO;
import com.kiki.springboot_kiki.service.IIndustryNewsBehaviorService;
import com.kiki.springboot_kiki.service.IIndustryNewsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.kiki.springboot_kiki.annotation.RateLimit;
import com.kiki.springboot_kiki.Pojo.IndustryNews;
import com.kiki.springboot_kiki.repository.IndustryNewsRepository;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class IndustryNewsController {

    @Autowired
    private IIndustryNewsService industryNewsService;

    @Autowired
    private IIndustryNewsBehaviorService industryNewsBehaviorService;

    @Autowired
    private IndustryNewsRepository industryNewsRepository;

    /**
     * 获取行业动态分页列表（管理端 - 显示所有新闻）
     */
    @GetMapping("/list")
    public ResponseMessage<PageResult<NewsListDTO>> getNewsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        PageResult<NewsListDTO> result = industryNewsService.getNewsList(page, size, keyword, false);
        return ResponseMessage.success(result);
    }

    /**
     * 获取行业动态分页列表（用户端 - 只显示已上架的新闻）
     */
    @GetMapping("/public-list")
    public ResponseMessage<PageResult<NewsListDTO>> getPublicNewsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        PageResult<NewsListDTO> result = industryNewsService.getNewsList(page, size, keyword, true);
        return ResponseMessage.success(result);
    }

    /**
     * 获取行业动态详情
     */
    @GetMapping("/{newsId}")
    public ResponseMessage<NewsDetailDTO> getNewsDetail(@PathVariable Integer newsId) {
        NewsDetailDTO detail = industryNewsService.getNewsDetail(newsId);
        if (detail == null) {
            return new ResponseMessage<>(404, "动态不存在", null);
        }
        return ResponseMessage.success(detail);
    }

    /**
     * 记录行业动态浏览行为
     */
    @PostMapping("/{newsId}/view")
    public ResponseMessage<UserBehaviorDTO> recordView(@PathVariable Integer newsId,
                                                       @RequestParam(required = false) Integer userId,
                                                       HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        UserBehaviorDTO dto = industryNewsBehaviorService.recordView(newsId, userId, ip, userAgent);
        return ResponseMessage.success(dto);
    }

    /**
     * 点赞
     */
    @PostMapping("/{newsId}/like")
    @RateLimit(time = 60, count = 10, type = RateLimit.LimitType.USER_ID)
    public ResponseMessage<UserBehaviorDTO> like(@PathVariable Integer newsId,
                                                 @RequestParam Integer userId,
                                                 HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        UserBehaviorDTO dto = industryNewsBehaviorService.like(newsId, userId, ip, userAgent);
        return ResponseMessage.success(dto);
    }

    /**
     * 取消点赞
     */
    @PostMapping("/{newsId}/unlike")
    public ResponseMessage<Boolean> unlike(@PathVariable Integer newsId,
                                           @RequestParam Integer userId) {
        boolean result = industryNewsBehaviorService.unlike(newsId, userId);
        return ResponseMessage.success(result);
    }

    /**
     * 收藏
     */
    @PostMapping("/{newsId}/favorite")
    @RateLimit(time = 60, count = 10, type = RateLimit.LimitType.USER_ID)
    public ResponseMessage<UserBehaviorDTO> favorite(@PathVariable Integer newsId,
                                                     @RequestParam Integer userId,
                                                     HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");
        UserBehaviorDTO dto = industryNewsBehaviorService.favorite(newsId, userId, ip, userAgent);
        return ResponseMessage.success(dto);
    }

    /**
     * 取消收藏
     */
    @PostMapping("/{newsId}/unfavorite")
    public ResponseMessage<Boolean> unfavorite(@PathVariable Integer newsId,
                                               @RequestParam Integer userId) {
        boolean result = industryNewsBehaviorService.unfavorite(newsId, userId);
        return ResponseMessage.success(result);
    }

    /**
     * 查询点赞状态
     */
    @GetMapping("/{newsId}/like-status")
    public ResponseMessage<Boolean> isLiked(@PathVariable Integer newsId,
                                            @RequestParam Integer userId) {
        boolean liked = industryNewsBehaviorService.isLiked(newsId, userId);
        return ResponseMessage.success(liked);
    }

    /**
     * 查询收藏状态
     */
    @GetMapping("/{newsId}/favorite-status")
    public ResponseMessage<Boolean> isFavorited(@PathVariable Integer newsId,
                                                @RequestParam Integer userId) {
        boolean favorited = industryNewsBehaviorService.isFavorited(newsId, userId);
        return ResponseMessage.success(favorited);
    }

    /**
     * 获取行业动态统计数据（浏览量、点赞量、收藏量）
     */
    @GetMapping("/{newsId}/stats")
    public ResponseMessage<NewsStatsDTO> getNewsStats(@PathVariable Integer newsId) {
        NewsStatsDTO stats = industryNewsService.getNewsStats(newsId);
        return ResponseMessage.success(stats);
    }

    /**
     * 批量获取行业动态统计数据
     */
    @PostMapping("/stats")
    public ResponseMessage<List<NewsStatsDTO>> getNewsStatsBatch(@RequestBody NewsStatsBatchRequestDTO request) {
        List<NewsStatsDTO> statsList = industryNewsService.getNewsStatsBatch(request.getNewsIds());
        return ResponseMessage.success(statsList);
    }

    /**
     * 新增行业动态（管理端）
     */
    @PostMapping
    public ResponseMessage<Integer> addNews(@RequestBody NewsEditDTO dto) {
        Integer newsId = industryNewsService.addNews(dto);
        return ResponseMessage.success(newsId);
    }

    /**
     * 编辑行业动态（管理端）
     */
    @PutMapping("/{newsId}")
    public ResponseMessage<Void> updateNews(@PathVariable Integer newsId, @RequestBody NewsEditDTO dto) {
        industryNewsService.updateNews(newsId, dto);
        return ResponseMessage.success(null);
    }

    /**
     * 删除行业动态（管理端）
     */
    @DeleteMapping("/{newsId}")
    public ResponseMessage<Void> deleteNews(@PathVariable Integer newsId) {
        industryNewsService.deleteNews(newsId);
        return ResponseMessage.success(null);
    }

    /**
     * 修改行业动态上下架状态
     */
    @PutMapping("/{newsId}/status")
    public ResponseMessage<Void> updateNewsStatus(@PathVariable Integer newsId, @RequestBody StatusDTO dto) {
        industryNewsService.updateNewsStatus(newsId, dto.getStatus());
        return ResponseMessage.success(null);
    }

    /**
     * 设置/取消置顶
     */
    @PutMapping("/{newsId}/top")
    public ResponseMessage<Void> updateNewsTop(@PathVariable Integer newsId, @RequestBody TopDTO dto) {
        industryNewsService.updateNewsTop(newsId, dto.getTop());
        return ResponseMessage.success(null);
    }

    /**
     * 修改排序权重
     */
    @PutMapping("/{newsId}/sort-order")
    public ResponseMessage<Void> updateNewsSortOrder(@PathVariable Integer newsId, @RequestBody SortOrderDTO dto) {
        industryNewsService.updateNewsSortOrder(newsId, dto.getSortOrder());
        return ResponseMessage.success(null);
    }

    /**
     * 用户行为趋势统计（按天）
     */
    @GetMapping("/behavior/trend")
    public ResponseMessage<List<BehaviorTrendDTO>> getBehaviorTrend(
            @RequestParam(required = false) String behaviorType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<BehaviorTrendDTO> trend = industryNewsBehaviorService.getBehaviorTrend(behaviorType, startDate, endDate);
        return ResponseMessage.success(trend);
    }

    /**
     * 手动更新行业动态封面图片链接
     */
    @PostMapping("/update-cover-images")
    public ResponseMessage<String> updateCoverImages() {
        try {
            // 获取所有行业动态
            Iterable<IndustryNews> allNews = industryNewsRepository.findAll();
            int updateCount = 0;
            
            for (IndustryNews news : allNews) {
                // 强制更新所有新闻的封面图片链接
                String coverImageUrl = getCoverImageUrlByNewsId(news.getNewsId());
                news.setCoverImage(coverImageUrl);
                industryNewsRepository.save(news);
                updateCount++;
            }
            
            return ResponseMessage.success("成功更新 " + updateCount + " 条新闻的封面图片");
        } catch (Exception e) {
            return new ResponseMessage<>(500, "更新失败: " + e.getMessage(), null);
        }
    }
    
    /**
     * 根据新闻ID获取合适的封面图片URL
     */
    private String getCoverImageUrlByNewsId(Integer newsId) {
        // 根据新闻ID返回不同的图片链接 - 使用Unsplash高质量图片资源
        switch (newsId) {
            case 1:
                return "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&h=300&fit=crop"; // 人工智能相关图片
            case 2:
                return "https://images.unsplash.com/photo-1519125323398-675f0ddb6308?w=400&h=300&fit=crop"; // 云计算相关图片
            case 3:
                return "https://images.unsplash.com/photo-1465101046530-73398c7f28ca?w=400&h=300&fit=crop"; // 移动开发相关图片
            default:
                return "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=400&h=300&fit=crop"; // 默认图片
        }
    }
} 
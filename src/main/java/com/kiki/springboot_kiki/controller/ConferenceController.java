package com.kiki.springboot_kiki.controller;

import com.kiki.springboot_kiki.Pojo.DTO.ConferenceCategoryDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceDTO;
import com.kiki.springboot_kiki.Pojo.DTO.RegistrationDTO;
import com.kiki.springboot_kiki.Pojo.DTO.RegistrationResponseDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceEditDTO;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import com.kiki.springboot_kiki.Pojo.StandardDetail;
import com.kiki.springboot_kiki.Pojo.TrainingDetail;
import com.kiki.springboot_kiki.Pojo.ToolDetail;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceRegistrationStatsDTO;
import com.kiki.springboot_kiki.service.IConferenceCategoryService;
import com.kiki.springboot_kiki.service.IConferenceRegistrationService;
import com.kiki.springboot_kiki.service.IConferenceService;
import com.kiki.springboot_kiki.service.StandardDetailService;
import com.kiki.springboot_kiki.service.TrainingDetailService;
import com.kiki.springboot_kiki.service.ToolDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.kiki.springboot_kiki.annotation.RateLimit;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
public class ConferenceController {

    @Autowired
    private IConferenceCategoryService conferenceCategoryService;

    @Autowired
    private IConferenceService conferenceService;

    @Autowired
    private IConferenceRegistrationService conferenceRegistrationService;

    @Autowired
    private StandardDetailService standardDetailService;

    @Autowired
    private TrainingDetailService trainingDetailService;

    @Autowired
    private ToolDetailService toolDetailService;

    /**
     * 获取会议分类列表
     */
    @GetMapping("/categories")
    public ResponseMessage<List<ConferenceCategoryDTO>> getCategories() {
        List<ConferenceCategoryDTO> categories = conferenceCategoryService.getAllCategories();
        return ResponseMessage.success(categories);
    }

    /**
     * 获取会议列表（支持分页、分类筛选、关键词搜索）
     */
    @GetMapping
    public ResponseMessage<PageResult<ConferenceDTO>> getConferenceList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword) {
        
        PageResult<ConferenceDTO> result = conferenceService.getConferenceList(page, size, categoryId, keyword);
        return ResponseMessage.success(result);
    }

    /**
     * 获取会议详情
     */
    @GetMapping("/{conferenceId}")
    public ResponseMessage<ConferenceDTO> getConferenceDetail(@PathVariable Integer conferenceId) {
        ConferenceDTO conference = conferenceService.getConferenceById(conferenceId);
        if (conference == null) {
            return new ResponseMessage<>(404, "会议不存在", null);
        }
        return ResponseMessage.success(conference);
    }

    /**
     * 获取热门会议列表
     */
    @GetMapping("/hot")
    public ResponseMessage<List<ConferenceDTO>> getHotConferences(
            @RequestParam(defaultValue = "5") int limit) {
        List<ConferenceDTO> hotConferences = conferenceService.getHotConferences(limit);
        return ResponseMessage.success(hotConferences);
    }

    /**
     * 提交会议注册
     */
    @PostMapping("/register")
    @RateLimit(time = 60, count = 20, type = RateLimit.LimitType.IP)
    public ResponseMessage<RegistrationResponseDTO> register(@Validated @RequestBody RegistrationDTO registrationDTO) {
        try {
            RegistrationResponseDTO registration = conferenceRegistrationService.register(registrationDTO);
            return ResponseMessage.success(registration);
        } catch (RuntimeException e) {
            return new ResponseMessage<>(400, e.getMessage(), null);
        }
    }

    /**
     * 获取会议的注册列表
     */
    @GetMapping("/{conferenceId}/registrations")
    public ResponseMessage<List<RegistrationResponseDTO>> getConferenceRegistrations(@PathVariable Integer conferenceId) {
        List<RegistrationResponseDTO> registrations = conferenceRegistrationService.getRegistrationsByConferenceId(conferenceId);
        return ResponseMessage.success(registrations);
    }

    /**
     * 检查是否已注册
     */
    @GetMapping("/{conferenceId}/check-registration")
    public ResponseMessage<Boolean> checkRegistration(
            @PathVariable Integer conferenceId,
            @RequestParam String phone) {
        boolean isRegistered = conferenceRegistrationService.isAlreadyRegistered(conferenceId, phone);
        return ResponseMessage.success(isRegistered);
    }

    /**
     * 新增会议（管理端）
     */
    @PostMapping
    public ResponseMessage<Integer> addConference(@RequestBody ConferenceEditDTO dto) {
        Integer id = conferenceService.addConference(dto);
        return ResponseMessage.success(id);
    }

    /**
     * 编辑会议（管理端）
     */
    @PutMapping("/{conferenceId}")
    public ResponseMessage<Void> updateConference(@PathVariable Integer conferenceId, @RequestBody ConferenceEditDTO dto) {
        conferenceService.updateConference(conferenceId, dto);
        return ResponseMessage.success(null);
    }

    /**
     * 删除会议（管理端）
     */
    @DeleteMapping("/{conferenceId}")
    public ResponseMessage<Void> deleteConference(@PathVariable Integer conferenceId) {
        conferenceService.deleteConference(conferenceId);
        return ResponseMessage.success(null);
    }

    /**
     * 获取标准定制详情
     */
    @GetMapping("/{conferenceId}/standard-detail")
    public ResponseMessage<StandardDetail> getStandardDetail(@PathVariable Integer conferenceId) {
        StandardDetail detail = standardDetailService.getByConferenceId(conferenceId);
        return ResponseMessage.success(detail);
    }

    /**
     * 获取技术培训详情
     */
    @GetMapping("/{conferenceId}/training-detail")
    public ResponseMessage<TrainingDetail> getTrainingDetail(@PathVariable Integer conferenceId) {
        TrainingDetail detail = trainingDetailService.getByConferenceId(conferenceId);
        return ResponseMessage.success(detail);
    }

    /**
     * 获取工具研发详情
     */
    @GetMapping("/{conferenceId}/tool-detail")
    public ResponseMessage<ToolDetail> getToolDetail(@PathVariable Integer conferenceId) {
        ToolDetail detail = toolDetailService.getByConferenceId(conferenceId);
        return ResponseMessage.success(detail);
    }

    /**
     * 新增或编辑标准定制详情
     */
    @PostMapping("/{conferenceId}/standard-detail")
    public ResponseMessage<StandardDetail> addOrUpdateStandardDetail(@PathVariable Integer conferenceId, @RequestBody StandardDetail detail) {
        detail.setConferenceId(conferenceId);
        StandardDetail saved = standardDetailService.addOrUpdate(detail);
        return ResponseMessage.success(saved);
    }
    @PutMapping("/{conferenceId}/standard-detail")
    public ResponseMessage<StandardDetail> updateStandardDetail(@PathVariable Integer conferenceId, @RequestBody StandardDetail detail) {
        detail.setConferenceId(conferenceId);
        StandardDetail saved = standardDetailService.addOrUpdate(detail);
        return ResponseMessage.success(saved);
    }
    @DeleteMapping("/{conferenceId}/standard-detail")
    public ResponseMessage<Void> deleteStandardDetail(@PathVariable Integer conferenceId) {
        standardDetailService.deleteByConferenceId(conferenceId);
        return ResponseMessage.success(null);
    }
    /**
     * 新增或编辑技术培训详情
     */
    @PostMapping("/{conferenceId}/training-detail")
    public ResponseMessage<TrainingDetail> addOrUpdateTrainingDetail(@PathVariable Integer conferenceId, @RequestBody TrainingDetail detail) {
        detail.setConferenceId(conferenceId);
        TrainingDetail saved = trainingDetailService.addOrUpdate(detail);
        return ResponseMessage.success(saved);
    }
    @PutMapping("/{conferenceId}/training-detail")
    public ResponseMessage<TrainingDetail> updateTrainingDetail(@PathVariable Integer conferenceId, @RequestBody TrainingDetail detail) {
        detail.setConferenceId(conferenceId);
        TrainingDetail saved = trainingDetailService.addOrUpdate(detail);
        return ResponseMessage.success(saved);
    }
    @DeleteMapping("/{conferenceId}/training-detail")
    public ResponseMessage<Void> deleteTrainingDetail(@PathVariable Integer conferenceId) {
        trainingDetailService.deleteByConferenceId(conferenceId);
        return ResponseMessage.success(null);
    }
    /**
     * 新增或编辑工具研发详情
     */
    @PostMapping("/{conferenceId}/tool-detail")
    public ResponseMessage<ToolDetail> addOrUpdateToolDetail(@PathVariable Integer conferenceId, @RequestBody ToolDetail detail) {
        detail.setConferenceId(conferenceId);
        ToolDetail saved = toolDetailService.addOrUpdate(detail);
        return ResponseMessage.success(saved);
    }
    @PutMapping("/{conferenceId}/tool-detail")
    public ResponseMessage<ToolDetail> updateToolDetail(@PathVariable Integer conferenceId, @RequestBody ToolDetail detail) {
        detail.setConferenceId(conferenceId);
        ToolDetail saved = toolDetailService.addOrUpdate(detail);
        return ResponseMessage.success(saved);
    }
    @DeleteMapping("/{conferenceId}/tool-detail")
    public ResponseMessage<Void> deleteToolDetail(@PathVariable Integer conferenceId) {
        toolDetailService.deleteByConferenceId(conferenceId);
        return ResponseMessage.success(null);
    }

    /**
     * 会议报名统计接口
     * @param conferenceId 会议ID
     * @param startDate 开始日期（yyyy-MM-dd，可选）
     * @param endDate 结束日期（yyyy-MM-dd，可选）
     */
    @GetMapping("/{conferenceId}/registration-stats")
    public ResponseMessage<ConferenceRegistrationStatsDTO> getRegistrationStats(
            @PathVariable Integer conferenceId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        ConferenceRegistrationStatsDTO stats = conferenceRegistrationService.getRegistrationStats(conferenceId, startDate, endDate);
        return ResponseMessage.success(stats);
    }
} 
package com.kiki.springboot_kiki.controller;

import com.kiki.springboot_kiki.Pojo.DTO.UserInfoDTO;
import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import com.kiki.springboot_kiki.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
public class SuperAdminController {
    
    @Autowired
    private IUserService userService;
    
    /**
     * 获取所有用户
     */
    @GetMapping("/users")
    public ResponseMessage<List<UserInfoDTO>> getAllUsers() {
        List<UserInfoDTO> users = userService.getAllUsers();
        return ResponseMessage.success(users);
    }
    
    /**
     * 用户行为分析（待实现）
     */
    @GetMapping("/user-behavior/analysis")
    public ResponseMessage<Object> getUserBehaviorAnalysis(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        // TODO: 实现用户行为分析逻辑
        return ResponseMessage.success("用户行为分析功能待实现");
    }
    
    /**
     * 系统统计（待实现）
     */
    @GetMapping("/system/stats")
    public ResponseMessage<Object> getSystemStats() {
        // TODO: 实现系统统计逻辑
        return ResponseMessage.success("系统统计功能待实现");
    }
} 
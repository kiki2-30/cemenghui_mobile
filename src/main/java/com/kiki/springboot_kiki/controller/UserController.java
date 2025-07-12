package com.kiki.springboot_kiki.controller;

import com.kiki.springboot_kiki.Pojo.DTO.UserDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserLoginDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserInfoDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserLoginResponseDTO;
import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import com.kiki.springboot_kiki.service.IUserService;
import com.kiki.springboot_kiki.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseMessage<UserInfoDTO> register(@Validated @RequestBody UserDTO userDTO) {
        try {
            UserInfoDTO userInfo = userService.register(userDTO);
            return ResponseMessage.success(userInfo);
        } catch (RuntimeException e) {
            return new ResponseMessage<>(400, e.getMessage(), null);
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseMessage<UserLoginResponseDTO> login(@Validated @RequestBody UserLoginDTO loginDTO) {
        try {
            UserInfoDTO userInfo = userService.login(loginDTO);
            
            // 获取用户角色
            String role = userService.getUserRole(userInfo.getUserId());
            
            // 生成包含角色的token
            String token = jwtUtil.generateToken(userInfo.getUserId(), userInfo.getUsername(), role);
            
            UserLoginResponseDTO response = new UserLoginResponseDTO(userInfo, token, role);
            return ResponseMessage.success(response);
        } catch (RuntimeException e) {
            return new ResponseMessage<>(400, e.getMessage(), null);
        }
    }

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/{userId}")
    public ResponseMessage<UserInfoDTO> getUserById(@PathVariable Integer userId) {
        try {
            UserInfoDTO userInfo = userService.getUserById(userId);
            return ResponseMessage.success(userInfo);
        } catch (RuntimeException e) {
            return new ResponseMessage<>(404, e.getMessage(), null);
        }
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/username/{username}")
    public ResponseMessage<UserInfoDTO> getUserByUsername(@PathVariable String username) {
        try {
            UserInfoDTO userInfo = userService.getUserByUsername(username);
            return ResponseMessage.success(userInfo);
        } catch (RuntimeException e) {
            return new ResponseMessage<>(404, e.getMessage(), null);
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{userId}")
    public ResponseMessage<UserInfoDTO> updateUser(@PathVariable Integer userId, 
                                                   @Validated @RequestBody UserDTO userDTO) {
        try {
            UserInfoDTO userInfo = userService.updateUser(userId, userDTO);
            return ResponseMessage.success(userInfo);
        } catch (RuntimeException e) {
            return new ResponseMessage<>(400, e.getMessage(), null);
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public ResponseMessage<Boolean> deleteUser(@PathVariable Integer userId) {
        try {
            boolean result = userService.deleteUser(userId);
            return ResponseMessage.success(result);
        } catch (RuntimeException e) {
            return new ResponseMessage<>(400, e.getMessage(), null);
        }
    }

    /**
     * 检查用户名是否存在
     */
    @GetMapping("/check-username/{username}")
    public ResponseMessage<Boolean> checkUsername(@PathVariable String username) {
        boolean exists = userService.existsByUsername(username);
        return ResponseMessage.success(exists);
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/check-email/{email}")
    public ResponseMessage<Boolean> checkEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseMessage.success(exists);
    }

    /**
     * 检查手机号是否存在
     */
    @GetMapping("/check-phone/{phone}")
    public ResponseMessage<Boolean> checkPhone(@PathVariable String phone) {
        boolean exists = userService.existsByPhone(phone);
        return ResponseMessage.success(exists);
    }
}

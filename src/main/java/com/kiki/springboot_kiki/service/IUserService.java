package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.DTO.UserDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserLoginDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserInfoDTO;
import com.kiki.springboot_kiki.Pojo.User;

import java.util.List;

public interface IUserService {
    /**
     * 用户注册
     */
    UserInfoDTO register(UserDTO userDTO);
    
    /**
     * 用户登录
     */
    UserInfoDTO login(UserLoginDTO loginDTO);
    
    /**
     * 根据用户ID获取用户信息
     */
    UserInfoDTO getUserById(Integer userId);
    
    /**
     * 根据用户名获取用户信息
     */
    UserInfoDTO getUserByUsername(String username);
    
    /**
     * 更新用户信息
     */
    UserInfoDTO updateUser(Integer userId, UserDTO userDTO);
    
    /**
     * 删除用户
     */
    boolean deleteUser(Integer userId);
    
    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);
    
    /**
     * 获取用户角色
     */
    String getUserRole(Integer userId);
    
    /**
     * 获取所有用户（管理员功能）
     */
    List<UserInfoDTO> getAllUsers();
}

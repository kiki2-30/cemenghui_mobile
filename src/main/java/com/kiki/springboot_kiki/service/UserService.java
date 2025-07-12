package com.kiki.springboot_kiki.service;

import com.kiki.springboot_kiki.Pojo.DTO.UserDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserLoginDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserInfoDTO;
import com.kiki.springboot_kiki.Pojo.User;
import com.kiki.springboot_kiki.Pojo.ConferenceRegistration;
import com.kiki.springboot_kiki.Pojo.UserBehavior;
import com.kiki.springboot_kiki.repository.UserRepository;
import com.kiki.springboot_kiki.repository.ConferenceRegistrationRepository;
import com.kiki.springboot_kiki.repository.UserBehaviorRepository;
import com.kiki.springboot_kiki.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRegistrationRepository conferenceRegistrationRepository;

    @Autowired
    private UserBehaviorRepository userBehaviorRepository;

    @Override
    public UserInfoDTO register(UserDTO userDTO) {
        // 检查用户名是否已存在
        if (existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 检查手机号是否已存在
        if (existsByPhone(userDTO.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }
        
        // 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        
        // 加密密码
        user.setPassword(PasswordUtil.encode(userDTO.getPassword()));
        
        // 设置创建时间
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        // 保存用户
        User savedUser = userRepository.save(user);
        
        // 转换为DTO返回
        return convertToUserInfoDTO(savedUser);
    }

    @Override
    public UserInfoDTO login(UserLoginDTO loginDTO) {
        // 根据用户名查找用户
        Optional<User> userOpt = userRepository.findByUsername(loginDTO.getUsername());
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        User user = userOpt.get();
        
        // 验证密码
        if (!PasswordUtil.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 更新最后登录时间
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
        
        // 转换为DTO返回
        return convertToUserInfoDTO(user);
    }

    @Override
    public UserInfoDTO getUserById(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserInfoDTO(userOpt.get());
    }

    @Override
    public UserInfoDTO getUserByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        return convertToUserInfoDTO(userOpt.get());
    }

    @Override
    public UserInfoDTO updateUser(Integer userId, UserDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        
        User user = userOpt.get();
        
        // 检查用户名是否被其他用户使用
        if (!user.getUsername().equals(userDTO.getUsername()) && existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱是否被其他用户使用
        if (!user.getEmail().equals(userDTO.getEmail()) && existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 检查手机号是否被其他用户使用
        if (!user.getPhone().equals(userDTO.getPhone()) && existsByPhone(userDTO.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }
        
        // 更新用户信息
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setRealName(userDTO.getRealName());
        user.setCompany(userDTO.getCompany());
        user.setUpdateTime(LocalDateTime.now());
        
        // 如果密码不为空，则更新密码
        if (userDTO.getPassword() != null && !userDTO.getPassword().trim().isEmpty()) {
            user.setPassword(PasswordUtil.encode(userDTO.getPassword()));
        }
        
        User savedUser = userRepository.save(user);
        return convertToUserInfoDTO(savedUser);
    }

    @Override
    @Transactional
    public boolean deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("用户不存在");
        }
        
        // 删除用户相关的会议注册记录
        conferenceRegistrationRepository.deleteByUserId(userId);
        
        // 删除用户相关的行为记录
        userBehaviorRepository.deleteByUserId(userId);
        
        // 最后删除用户
        userRepository.deleteById(userId);
        return true;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }
    
    /**
     * 获取用户角色
     */
    public String getUserRole(Integer userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get().getRole().name();
        }
        return User.RoleType.USER.name(); // 默认角色
    }
    
    /**
     * 获取所有用户（管理员功能）
     */
    public List<UserInfoDTO> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .map(this::convertToUserInfoDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将User实体转换为UserInfoDTO
     */
    private UserInfoDTO convertToUserInfoDTO(User user) {
        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRealName(user.getRealName());
        dto.setCompany(user.getCompany());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        return dto;
    }
}

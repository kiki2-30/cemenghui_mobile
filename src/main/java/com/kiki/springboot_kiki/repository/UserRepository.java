package com.kiki.springboot_kiki.repository;

import com.kiki.springboot_kiki.Pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository   //spring bean 都是component 只是为了好区分

public interface UserRepository extends CrudRepository <User,Integer >{
    
    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);
    
    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);
    
    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);
    
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
     * 根据角色查找用户
     */
    List<User> findByRole(User.RoleType role);
    
    /**
     * 统计不同角色的用户数量
     */
    long countByRole(User.RoleType role);
}

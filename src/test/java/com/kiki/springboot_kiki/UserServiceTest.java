package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.UserDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserInfoDTO;
import com.kiki.springboot_kiki.Pojo.DTO.UserLoginDTO;
import com.kiki.springboot_kiki.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testRegisterAndLogin() {
        String username = "testuser" + System.currentTimeMillis();
        String email = username + "@test.com";
        String phone = "138" + (int)(Math.random()*100000000);
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword("123456");
        userDTO.setEmail(email);
        userDTO.setPhone(phone);
        UserInfoDTO info = userService.register(userDTO);
        assertNotNull(info);
        assertEquals(username, info.getUsername());
        UserLoginDTO loginDTO = new UserLoginDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword("123456");
        UserInfoDTO loginInfo = userService.login(loginDTO);
        assertNotNull(loginInfo);
        assertEquals(username, loginInfo.getUsername());
    }

    @Test
    public void testGetUserById_NotFound() {
        assertThrows(RuntimeException.class, () -> {
            userService.getUserById(999999);
        });
    }

    @Test
    public void testUpdateAndDeleteUser() {
        String username = "testuser" + System.currentTimeMillis();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword("123456");
        userDTO.setEmail(username + "@test.com");
        userDTO.setPhone("138" + (int)(Math.random()*100000000));
        UserInfoDTO info = userService.register(userDTO);
        assertNotNull(info);
        userDTO.setEmail("update@test.com");
        UserInfoDTO updated = userService.updateUser(info.getUserId(), userDTO);
        assertEquals("update@test.com", updated.getEmail());
        boolean deleted = userService.deleteUser(info.getUserId());
        assertTrue(deleted);
    }
} 
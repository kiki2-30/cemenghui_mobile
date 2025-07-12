package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.*;
import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import com.kiki.springboot_kiki.service.IUserService;
import com.kiki.springboot_kiki.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserService userService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testRegister() throws Exception {
        UserInfoDTO userInfo = new UserInfoDTO();
        Mockito.when(userService.register(any())).thenReturn(userInfo);
        String json = "{\"username\":\"testuser\",\"password\":\"password123\",\"email\":\"test@example.com\",\"phone\":\"13800138000\",\"realName\":\"张三\"}";
        mockMvc.perform(post("/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testLogin() throws Exception {
        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setUserId(1);
        userInfo.setUsername("testuser");
        Mockito.when(userService.login(any())).thenReturn(userInfo);
        Mockito.when(userService.getUserRole(anyInt())).thenReturn("USER");
        Mockito.when(jwtUtil.generateToken(anyInt(), anyString(), anyString())).thenReturn("token123");
        String json = "{\"username\":\"testuser\",\"password\":\"password123\"}";
        mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetUserById() throws Exception {
        UserInfoDTO userInfo = new UserInfoDTO();
        userInfo.setUserId(1);
        Mockito.when(userService.getUserById(1)).thenReturn(userInfo);
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(1));
    }

    @Test
    void testCheckUsername() throws Exception {
        Mockito.when(userService.existsByUsername("testuser")).thenReturn(true);
        mockMvc.perform(get("/user/check-username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }
} 
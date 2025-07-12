package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.controller.SuperAdminController;
import com.kiki.springboot_kiki.Pojo.DTO.UserInfoDTO;
import com.kiki.springboot_kiki.service.IUserService;
import com.kiki.springboot_kiki.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SuperAdminController.class)
public class SuperAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUserService userService;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testGetAllUsers() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/super-admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetUserBehaviorAnalysis() throws Exception {
        mockMvc.perform(get("/api/super-admin/user-behavior/analysis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetSystemStats() throws Exception {
        mockMvc.perform(get("/api/super-admin/system/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }
} 
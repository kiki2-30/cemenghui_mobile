package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.*;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.Pojo.ResponseMessage;
import com.kiki.springboot_kiki.Pojo.StandardDetail;
import com.kiki.springboot_kiki.Pojo.TrainingDetail;
import com.kiki.springboot_kiki.Pojo.ToolDetail;
import com.kiki.springboot_kiki.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ConferenceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IConferenceCategoryService conferenceCategoryService;
    @MockBean
    private IConferenceService conferenceService;
    @MockBean
    private IConferenceRegistrationService conferenceRegistrationService;
    @MockBean
    private StandardDetailService standardDetailService;
    @MockBean
    private TrainingDetailService trainingDetailService;
    @MockBean
    private ToolDetailService toolDetailService;

    @Test
    void testGetCategories() throws Exception {
        Mockito.when(conferenceCategoryService.getAllCategories()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/conferences/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetConferenceList() throws Exception {
        Mockito.when(conferenceService.getConferenceList(anyInt(), anyInt(), any(), any())).thenReturn(new PageResult<>());
        mockMvc.perform(get("/api/conferences?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetConferenceDetail() throws Exception {
        ConferenceDTO dto = new ConferenceDTO();
        dto.setConferenceId(1);
        Mockito.when(conferenceService.getConferenceById(1)).thenReturn(dto);
        mockMvc.perform(get("/api/conferences/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.conferenceId").value(1));
    }

    @Test
    void testGetConferenceDetailNotFound() throws Exception {
        Mockito.when(conferenceService.getConferenceById(2)).thenReturn(null);
        mockMvc.perform(get("/api/conferences/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void testGetHotConferences() throws Exception {
        Mockito.when(conferenceService.getHotConferences(anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/conferences/hot?limit=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testRegister() throws Exception {
        RegistrationResponseDTO response = new RegistrationResponseDTO();
        Mockito.when(conferenceRegistrationService.register(any())).thenReturn(response);
        String json = "{\"conferenceId\":1,\"realName\":\"张三\",\"phone\":\"13800138000\"}";
        mockMvc.perform(post("/api/conferences/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testGetConferenceRegistrations() throws Exception {
        Mockito.when(conferenceRegistrationService.getRegistrationsByConferenceId(anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/conferences/1/registrations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void testCheckRegistration() throws Exception {
        Mockito.when(conferenceRegistrationService.isAlreadyRegistered(anyInt(), anyString())).thenReturn(true);
        mockMvc.perform(get("/api/conferences/1/check-registration?phone=13800138000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }
} 
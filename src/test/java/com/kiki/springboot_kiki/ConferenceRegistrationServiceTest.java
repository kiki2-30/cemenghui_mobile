package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.RegistrationDTO;
import com.kiki.springboot_kiki.repository.ConferenceRegistrationRepository;
import com.kiki.springboot_kiki.service.ConferenceRegistrationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ConferenceRegistrationServiceTest {
    @Autowired
    private ConferenceRegistrationService conferenceRegistrationService;

    @MockBean
    private ConferenceRegistrationRepository conferenceRegistrationRepository;

    @Test
    void testIsAlreadyRegistered() {
        when(conferenceRegistrationRepository.findAll()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> {
            conferenceRegistrationService.isAlreadyRegistered(1, "13800000000");
        });
    }

    @Test
    void testRegisterSuccess() {
        when(conferenceRegistrationRepository.findAll()).thenReturn(Collections.emptyList());
        assertDoesNotThrow(() -> {
            RegistrationDTO dto = new RegistrationDTO();
            dto.setConferenceId(1);
            dto.setPhone("13800000000");
            conferenceRegistrationService.register(dto);
        });
    }
} 
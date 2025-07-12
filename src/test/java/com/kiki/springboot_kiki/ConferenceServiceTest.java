package com.kiki.springboot_kiki;

import com.kiki.springboot_kiki.Pojo.DTO.ConferenceDTO;
import com.kiki.springboot_kiki.Pojo.DTO.ConferenceEditDTO;
import com.kiki.springboot_kiki.Pojo.PageResult;
import com.kiki.springboot_kiki.service.ConferenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConferenceServiceTest {
    @Autowired
    private ConferenceService conferenceService;

    @Test
    public void testGetConferenceList() {
        PageResult<ConferenceDTO> result = conferenceService.getConferenceList(0, 10, null, null);
        assertNotNull(result);
        assertTrue(result.getContent().size() >= 0);
    }

    @Test
    public void testGetConferenceById() {
        ConferenceDTO dto = conferenceService.getConferenceById(1);
        assertNotNull(dto);
        assertEquals(1, dto.getConferenceId());
    }

    @Test
    public void testAddUpdateDeleteConference() {
        ConferenceEditDTO dto = new ConferenceEditDTO();
        dto.setTitle("单元测试会议");
        dto.setCategoryId(1);
        Integer id = conferenceService.addConference(dto);
        assertNotNull(id);
        dto.setTitle("修改后会议");
        conferenceService.updateConference(id, dto);
        ConferenceDTO updated = conferenceService.getConferenceById(id);
        assertEquals("修改后会议", updated.getTitle());
        conferenceService.deleteConference(id);
        assertNull(conferenceService.getConferenceById(id));
    }
} 
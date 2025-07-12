package com.kiki.springboot_kiki.Pojo.DTO;

import java.util.List;

public class ConferenceRegistrationStatsDTO {
    private Integer totalCount;
    private List<RegistrationResponseDTO> registrationList;

    public ConferenceRegistrationStatsDTO() {}
    public ConferenceRegistrationStatsDTO(Integer totalCount, List<RegistrationResponseDTO> registrationList) {
        this.totalCount = totalCount;
        this.registrationList = registrationList;
    }
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    public List<RegistrationResponseDTO> getRegistrationList() { return registrationList; }
    public void setRegistrationList(List<RegistrationResponseDTO> registrationList) { this.registrationList = registrationList; }
} 
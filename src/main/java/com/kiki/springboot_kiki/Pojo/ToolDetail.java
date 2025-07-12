package com.kiki.springboot_kiki.Pojo;

import jakarta.persistence.*;

@Table(name = "tb_tool_detail")
@Entity
public class ToolDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "conference_id", nullable = false)
    private Integer conferenceId;

    @Column(name = "standard_name", length = 255)
    private String standardName;

    @Column(name = "standard_desc", columnDefinition = "TEXT")
    private String standardDesc;

    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;

    @Column(name = "process", columnDefinition = "TEXT")
    private String process;

    @Column(name = "timeline", columnDefinition = "TEXT")
    private String timeline;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getConferenceId() { return conferenceId; }
    public void setConferenceId(Integer conferenceId) { this.conferenceId = conferenceId; }
    public String getStandardName() { return standardName; }
    public void setStandardName(String standardName) { this.standardName = standardName; }
    public String getStandardDesc() { return standardDesc; }
    public void setStandardDesc(String standardDesc) { this.standardDesc = standardDesc; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public String getProcess() { return process; }
    public void setProcess(String process) { this.process = process; }
    public String getTimeline() { return timeline; }
    public void setTimeline(String timeline) { this.timeline = timeline; }
} 
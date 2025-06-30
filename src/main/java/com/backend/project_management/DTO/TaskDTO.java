package com.backend.project_management.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {
    private Long id;
    private String subject;
    private String description;
    private String projectName;
    private String priority;
    private String status;
    private String statusBar;
    private int days;

    private String imageUrl;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String timeFrame; // "today", "7days", "30days", "custom"
    private LocalDate customStartDate;
    private LocalDate customEndDate;



    private Long assignedByAdminId;
    private Long assignedByLeaderId;
    private Long assignedToTeamMember;
    private Long assignedToTeamLeader;

    private String assignedToName;

}

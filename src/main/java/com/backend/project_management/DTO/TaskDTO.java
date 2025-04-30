package com.backend.project_management.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
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

    private LocalDate startDate;
    private LocalDate endDate;


    private Long assignedByAdminId;
    private Long assignedByLeaderId;
    private Long assignedToTeamMember;
    private Long assignedToTeamLeader;

}

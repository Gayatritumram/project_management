package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSummaryDTO {
    private String subject;
    private String description;
    private String priority;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private String assignedToNameofLeader;
    private String assignedToNameofMember;
}

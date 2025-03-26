package com.backend.project_management.DTO;

import com.backend.project_management.TaskPriority;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TaskDTO {
    private Long id;
    private String description;
    private String projectName;
    private int days;
    private int hour;
    private String status;
    private double statusBar;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String imageUrl;
    private long durationInMinutes;
    private String subject;
    private TaskPriority priority;
    private Long assignedToId;  // Instead of exposing TeamMember entity
    private Long assignedBy;
}

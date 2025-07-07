package com.backend.project_management.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskReportDTO {
        private long totalTasks;
        private long completedTasks;
        private long pendingTasks;
        private long inProgressTasks;
        private List<TaskDTO> taskDetails;
    }


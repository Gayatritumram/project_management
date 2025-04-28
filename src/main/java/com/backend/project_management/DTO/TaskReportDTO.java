package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskReportDTO {
        private long totalTasks;
        private long completedTasks;
        private long pendingTasks;
        private long inProgressTasks;
        private List<TaskDTO> taskDetails;
    }


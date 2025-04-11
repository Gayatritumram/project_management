package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class TaskReportDTO {
    private Map<String, Long> tasksByDateRange;
    private Map<String, Long > taskByStatus;
}

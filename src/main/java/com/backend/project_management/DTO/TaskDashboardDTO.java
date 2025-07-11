package com.backend.project_management.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDashboardDTO {
    private long completed;
    private long inProgress;
    private long delay;
    private long onHold;
    private long todaysTask;
}


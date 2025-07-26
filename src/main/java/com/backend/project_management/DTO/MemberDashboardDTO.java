package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDashboardDTO {
    private long totalTasks;
    private long inProgress;
    private long completed;
    private long delay;
    private long onHold;

    private long today;
    private long last7Days;
    private long last30Days;
    private long last365Days;
}

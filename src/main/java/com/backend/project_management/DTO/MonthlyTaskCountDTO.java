package com.backend.project_management.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyTaskCountDTO {
    private long total;
    private long completed;
    private long inProgress;
    private long delay;
    private long onHold;
}


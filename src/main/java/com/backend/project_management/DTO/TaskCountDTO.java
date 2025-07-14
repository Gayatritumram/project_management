package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskCountDTO {
    private long total;
    private long last7Days;
    private long last30Days;
    private long last365Days;

}


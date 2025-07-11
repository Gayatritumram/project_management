package com.backend.project_management.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectStatusCountDTO {
    private long completed;
    private long inProgress;
    private long delay;
    private long onHold;
    private long todaysProject;
}

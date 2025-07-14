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
    private long Completed;
    private long In_Progress;
    private long Delay;
    private long On_Hold;
    private long Todays_Project;
}

package com.backend.project_management.DTO;

import com.backend.project_management.Entity.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamDTO {
    private Long id;
    private String teamName;
    private String branch;
    private String department;


}
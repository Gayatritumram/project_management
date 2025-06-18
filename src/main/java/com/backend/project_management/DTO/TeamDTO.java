package com.backend.project_management.DTO;

import com.backend.project_management.Entity.Project;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class TeamDTO {
    private Long id;
    private String teamName;
    private String branchName;
    private String department;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;

    private Long teamLeaderId;
    private List<TeamMemberDTO> teamMemberList;

}
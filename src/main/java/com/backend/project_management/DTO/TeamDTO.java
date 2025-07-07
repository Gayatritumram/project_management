package com.backend.project_management.DTO;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class TeamDTO {
    private Long id;
    private String teamName;
    private String branchName;
    private String departmentName;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;

    private Long teamLeaderId;
    private List<TeamMemberDTO> teamMemberList;

}
package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.TeamMember;

import java.util.Optional;

public interface ProjectAdminService {
    ProjectAdmin registerAdmin(ProjectAdminDTO adminDTO);
    String loginAdmin(String email, String password);
    ProjectAdminDTO findAdminByEmail(String email);
    TeamMember createTeamLeader(TeamMemberDTO teamMemberDTO);
    TeamMember createTeamMember(TeamMemberDTO teamMemberDTO);
    Task assignTask(TaskDTO taskDTO);
}

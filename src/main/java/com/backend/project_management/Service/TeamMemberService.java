package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamMemberDTO;
import java.util.List;

public interface TeamMemberService {
    TeamMemberDTO createTeamMember(TeamMemberDTO teamMemberDTO);
    TeamMemberDTO getTeamMemberById(Long id);
    List<TeamMemberDTO> getAllTeamMembers();
    TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO);
    void deleteTeamMember(Long id);
}

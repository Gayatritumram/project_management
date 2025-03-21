package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamMember;

import java.util.List;

public interface TeamService {
    TeamDTO createTeam(TeamDTO teamDTO);
    TeamDTO getTeamById(Long id);
    List<TeamDTO> getAllTeams();
    TeamDTO updateTeam(Long id, TeamDTO teamDTO);
    void deleteTeam(Long id);


}
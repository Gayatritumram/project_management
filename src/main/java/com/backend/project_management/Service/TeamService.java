package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Entity.Team;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeamService {
    TeamDTO createTeam(TeamDTO teamDTO,String role, String email);
    TeamDTO getTeamById(Long id,String role, String email);
    List<TeamDTO> getAllTeams(String role, String email, String branchCode);
    TeamDTO updateTeam(Long id, TeamDTO teamDTO,String role, String email);
    void deleteTeam(Long id, String role, String email);

    Page<TeamDTO> getAllTeams(String role, String email,
                              Team filter, int page, int size,
                              String sortBy, String sortDir);



}
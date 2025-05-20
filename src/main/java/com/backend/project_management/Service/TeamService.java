package com.backend.project_management.Service;

import com.backend.project_management.DTO.TeamDTO;

import java.util.List;

public interface TeamService {
    TeamDTO createTeam(TeamDTO teamDTO,String role, String email);
    TeamDTO getTeamById(Long id,String role, String email);
    List<TeamDTO> getAllTeams(String role, String email, String branchCode);
    TeamDTO updateTeam(Long id, TeamDTO teamDTO,String role, String email);
    void deleteTeam(Long id, String role, String email);



}
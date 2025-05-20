package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Entity.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamDTO toDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setTeamName(team.getTeamName());
        dto.setBranchName(team.getBranchName());
        dto.setDepartment(team.getDepartment());
        dto.setRole(team.getRole());
        dto.setCreatedByEmail(team.getCreatedByEmail());
        dto.setBranchCode(team.getBranchCode());
        return dto;
    }

    public Team toEntity(TeamDTO teamDTO) {
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setTeamName(teamDTO.getTeamName());
        team.setBranchName(teamDTO.getBranchName());
        team.setDepartment(teamDTO.getDepartment());
        team.setRole(teamDTO.getRole());
        team.setCreatedByEmail(teamDTO.getCreatedByEmail());
        team.setBranchCode(teamDTO.getBranchCode());

        return team;
    }
}
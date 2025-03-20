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
        dto.setBranch(team.getBranch());
        dto.setDepartment(team.getDepartment());

        return dto;
    }

    public Team toEntity(TeamDTO teamDTO) {
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setTeamName(teamDTO.getTeamName());
        team.setBranch(team.getBranch());
        team.setDepartment(team.getDepartment());
        return team;
    }
}
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
        return dto;
    }

    public Team toEntity(TeamDTO teamDTO) {
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setTeamName(teamDTO.getTeamName());
        return team;
    }
}
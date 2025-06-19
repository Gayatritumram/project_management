package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component

public class TeamMapper {

    public static TeamDTO toDTO(Team team) {
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setTeamName(team.getTeamName());
        dto.setBranchName(team.getBranchName());
        dto.setDepartmentName(team.getDepartmentName());
        dto.setRole(team.getRole());
        dto.setCreatedByEmail(team.getCreatedByEmail());
        dto.setBranchCode(team.getBranchCode());
        // ✅ Map team members
        if (team.getMemberList() != null) {
            dto.setTeamMemberList(
                    team.getMemberList()
                            .stream()
                            .map(TeamMemberMapper::mapToTeamMemberDTO)
                            .toList()
            );
        }

        // ✅ Map team leader ID
        if (team.getTeamLeader() != null) {
            dto.setTeamLeaderId(team.getTeamLeader().getId());
        }

        return dto;
    }

    public static Team toEntity(TeamDTO teamDTO) {
        Team team = new Team();
        team.setId(teamDTO.getId());
        team.setTeamName(teamDTO.getTeamName());
        team.setBranchName(teamDTO.getBranchName());
        team.setDepartmentName(teamDTO.getDepartmentName());
        team.setRole(teamDTO.getRole());
        team.setCreatedByEmail(teamDTO.getCreatedByEmail());
        team.setBranchCode(teamDTO.getBranchCode());

        return team;
    }
}
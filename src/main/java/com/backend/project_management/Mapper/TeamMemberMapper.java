package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamMember;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberMapper {

    public static TeamMember mapToTeamMember(TeamMemberDTO dto, Team team) {
        TeamMember teamMember = new TeamMember();
        teamMember.setId(dto.getId());
        teamMember.setName(dto.getName());
        teamMember.setEmail(dto.getEmail());
        teamMember.setPassword(dto.getPassword());
        teamMember.setConfirmPassword(dto.getConfirmPassword());
        teamMember.setJoinDate(dto.getJoinDate());
        teamMember.setDepartment(dto.getDepartment());
        teamMember.setPhone(dto.getPhone());
        teamMember.setAddress(dto.getAddress());
        teamMember.setRole(dto.getRole());
        teamMember.setProjectName(dto.getProjectName());
        teamMember.setBranch(dto.getBranch());
        teamMember.setLeader(dto.isLeader());

        if (team != null) {
            teamMember.setTeam(team);
        }

        return teamMember;
    }

    public static TeamMemberDTO mapToTeamMemberDTO(TeamMember teamMember) {
        TeamMemberDTO dto = new TeamMemberDTO();
        dto.setId(teamMember.getId());
        dto.setName(teamMember.getName());
        dto.setEmail(teamMember.getEmail());
        dto.setJoinDate(teamMember.getJoinDate());
        dto.setDepartment(teamMember.getDepartment());
        dto.setPhone(teamMember.getPhone());
        dto.setAddress(teamMember.getAddress());
        dto.setRole(teamMember.getRole());
        dto.setProjectName(teamMember.getProjectName());
        dto.setBranch(teamMember.getBranch());
        dto.setLeader(teamMember.isLeader());

        if (teamMember.getTeam() != null) {
            dto.setTeamId(teamMember.getTeam().getId());
        }

        return dto;
    }
}

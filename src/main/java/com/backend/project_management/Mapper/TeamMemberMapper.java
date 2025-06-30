package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamMember;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberMapper {

    public static TeamMember mapToTeamMember(TeamMemberDTO dto) {
        TeamMember teamMember = new TeamMember();
        teamMember.setId(dto.getId());
        teamMember.setName(dto.getName());
        teamMember.setEmail(dto.getEmail());
        teamMember.setPassword(dto.getPassword());
        teamMember.setJoinDate(dto.getJoinDate());
        teamMember.setDepartmentName(dto.getDepartmentName());
        teamMember.setPhone(dto.getPhone());
        teamMember.setAddress(dto.getAddress());
        teamMember.setRoleName(dto.getRoleName());
        teamMember.setProjectName(dto.getProjectName());
        teamMember.setBranchName(dto.getBranchName());
        teamMember.setPassword(dto.getPassword());
        teamMember.setImageUrl(dto.getImageUrl());
        teamMember.setCreatedByEmail(dto.getCreatedByEmail());
        teamMember.setBranchCode(dto.getBranchCode());
        teamMember.setRole(dto.getRole());




        return teamMember;
    }

    public static TeamMemberDTO mapToTeamMemberDTO(TeamMember teamMember) {
        TeamMemberDTO dto = new TeamMemberDTO();
        dto.setId(teamMember.getId());
        dto.setName(teamMember.getName());
        dto.setEmail(teamMember.getEmail());
        dto.setJoinDate(teamMember.getJoinDate());
        dto.setDepartmentName(teamMember.getDepartmentName());
        dto.setPhone(teamMember.getPhone());
        dto.setAddress(teamMember.getAddress());
        dto.setRoleName(teamMember.getRoleName());
        dto.setProjectName(teamMember.getProjectName());
        dto.setBranchName(teamMember.getBranchName());
        dto.setPassword(teamMember.getPassword());
        dto.setImageUrl(teamMember.getImageUrl());

        dto.setCreatedByEmail(teamMember.getCreatedByEmail());
        dto.setBranchCode(teamMember.getBranchCode());
        dto.setRole(teamMember.getRole());

        if (teamMember.getTeam() != null) {
            dto.setTeamId(teamMember.getTeam().getId());
        }


        return dto;
    }
}

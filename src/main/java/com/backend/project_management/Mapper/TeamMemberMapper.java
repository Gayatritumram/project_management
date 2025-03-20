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
        teamMember.setJoinDate(dto.getJoinDate());
        teamMember.setDepartment(dto.getDepartment());
        teamMember.setPhone(dto.getPhone());
        teamMember.setAddress(dto.getAddress());
        teamMember.setRole(dto.getRole());
        teamMember.setProjectName(dto.getProjectName());
        teamMember.setBranch(dto.getBranch());
        teamMember.setLeader(dto.isLeader());
        return teamMember;

        }

    public static TeamMemberDTO mapToTeamMemberDTO(TeamMember teamMember) {
        return new TeamMemberDTO(
                teamMember.getId(),
                teamMember.getName(),
                teamMember.getEmail(),
                teamMember.getJoinDate(),
                teamMember.getDepartment(),
                teamMember.getPhone(),
                teamMember.getAddress(),
                teamMember.getRole(),
                teamMember.getProjectName(),
                teamMember.getBranch(),
                teamMember.isLeader()
        );
    }

    }




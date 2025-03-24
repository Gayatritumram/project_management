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
        teamMember.setConfirmPassword(dto.getConfirmPassword());
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

    public static TeamMemberDTO mapToTeamMemberDTO(TeamMember team) {
        TeamMemberDTO teamMember = new TeamMemberDTO();
        teamMember.setId(team.getId());
        teamMember.setName(team.getName());
        teamMember.setEmail(team.getEmail());
        teamMember.setJoinDate(team.getJoinDate());
        teamMember.setDepartment(team.getDepartment());
        teamMember.setPhone(team.getPhone());
        teamMember.setAddress(team.getAddress());
        teamMember.setRole(team.getRole());
        teamMember.setProjectName(team.getProjectName());
        teamMember.setBranch(team.getBranch());
        teamMember.setLeader(team.isLeader());
        return teamMember;
    }

    }




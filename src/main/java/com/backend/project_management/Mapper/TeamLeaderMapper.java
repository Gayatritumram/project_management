package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.TeamLeader;
import org.springframework.stereotype.Component;

@Component
public class TeamLeaderMapper {

    public TeamLeaderDTO toDto(TeamLeader leader) {
        TeamLeaderDTO dto = new TeamLeaderDTO();
        dto.setId(leader.getId());
        dto.setName(leader.getName());
        dto.setEmail(leader.getEmail());
        dto.setPassword(leader.getPassword());
        dto.setPhone(leader.getPhone());
        dto.setAddress(leader.getAddress());
        dto.setDepartment(leader.getDepartment());
        dto.setBranch(leader.getBranch());
        dto.setJoinDate(leader.getJoinDate());
        dto.setTeamId(leader.getTeamId() != null ? leader.getTeamId().getId() : null);
        dto.setUserRole(leader.getUserRole());
        return dto;
    }

    public TeamLeader toEntity(TeamLeaderDTO dto) {
        TeamLeader leader = new TeamLeader();
        leader.setId(dto.getId());
        leader.setName(dto.getName());
        leader.setEmail(dto.getEmail());
        leader.setPassword(dto.getPassword());
        leader.setPhone(dto.getPhone());
        leader.setAddress(dto.getAddress());
        leader.setDepartment(dto.getDepartment());
        leader.setBranch(dto.getBranch());
        leader.setJoinDate(dto.getJoinDate());
        // ❗Set team object separately in ServiceImpl using teamId
        leader.setUserRole(dto.getUserRole());
        return leader;
    }
}

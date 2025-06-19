package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TeamLeaderMapper {

    @Autowired
    private TeamRepository teamRepository;

    public TeamLeaderDTO toDto(TeamLeader leader) {
        TeamLeaderDTO dto = new TeamLeaderDTO();
        dto.setId(leader.getId());
        dto.setName(leader.getName());
        dto.setEmail(leader.getEmail());
        dto.setPassword(leader.getPassword());
        dto.setPhone(leader.getPhone());
        dto.setAddress(leader.getAddress());
        dto.setDepartmentName(leader.getDepartmentName());
        dto.setBranchName(leader.getBranchName());
        dto.setJoinDate(leader.getJoinDate());
        dto.setImageUrl(leader.getImageUrl());
        dto.setTeamId(leader.getTeamId() != null ? leader.getTeamId().getId() : null);
        dto.setCreatedByEmail(leader.getCreatedByEmail());
        dto.setBranchCode(leader.getBranchCode());
        dto.setRole(leader.getRole());

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
        leader.setDepartmentName(dto.getDepartmentName());
        leader.setBranchName(dto.getBranchName());
        leader.setJoinDate(dto.getJoinDate());
        leader.setImageUrl(dto.getImageUrl());
        leader.setCreatedByEmail(dto.getCreatedByEmail());
        leader.setBranchCode(dto.getBranchCode());
        leader.setRole(dto.getRole());

        if (dto.getTeamId() != null) {
            leader.setTeamId(teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found")));
        }

        return leader;
    }
}

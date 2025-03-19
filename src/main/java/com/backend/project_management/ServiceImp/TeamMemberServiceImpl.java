package com.backend.project_management.ServiceImp;


import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TeamMemberMapper;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    @Autowired
    private  TeamMemberRepository repository;


    @Override
    public TeamMemberDTO createTeamMember(TeamMemberDTO teamMemberDTO) {
        TeamMember teamMember = TeamMemberMapper.mapToTeamMember(teamMemberDTO);
        return TeamMemberMapper.mapToTeamMemberDTO(repository.save(teamMember));
    }

    @Override
    public TeamMemberDTO getTeamMemberById(Long id) {
        TeamMember teamMember = repository.findById(id).orElseThrow(() -> new RequestNotFound("Team Member not found"));
        return TeamMemberMapper.mapToTeamMemberDTO(teamMember);
    }

    @Override
    public List<TeamMemberDTO> getAllTeamMembers() {
        return repository.findAll().stream().map(TeamMemberMapper::mapToTeamMemberDTO).collect(Collectors.toList());
    }

    @Override
    public TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO) {
        TeamMember teamMember = repository.findById(id).orElseThrow(() -> new RequestNotFound("Team Member not found"));
        teamMember.setName(teamMemberDTO.getName());
        teamMember.setEmail(teamMemberDTO.getEmail());
        teamMember.setJoinDate(teamMemberDTO.getJoinDate());
        teamMember.setDepartment(teamMemberDTO.getDepartment());
        teamMember.setPhone(teamMemberDTO.getPhone());
        teamMember.setAddress(teamMemberDTO.getAddress());
        teamMember.setRole(teamMemberDTO.getRole());
        teamMember.setProjectName(teamMemberDTO.getProjectName());
        teamMember.setBranch(teamMemberDTO.getBranch());
        teamMember.setLeader(teamMemberDTO.isLeader());
        return TeamMemberMapper.mapToTeamMemberDTO(repository.save(teamMember));
    }

    @Override
    public void deleteTeamMember(Long id) {
        repository.deleteById(id);
    }
}


package com.backend.project_management.ServiceImp;


import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Mapper.TeamMemberMapper;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.TeamMemberService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {
    private final TeamMemberRepository repository;
    private final TeamMemberMapper mapper;

    public TeamMemberServiceImpl(TeamMemberRepository repository, TeamMemberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public TeamMemberDTO createTeamMember(TeamMemberDTO teamMemberDTO) {
        TeamMember teamMember = mapper.toEntity(teamMemberDTO);
        return mapper.toDTO(repository.save(teamMember));
    }

    @Override
    public TeamMemberDTO getTeamMemberById(Long id) {
        TeamMember teamMember = repository.findById(id).orElseThrow(() -> new RuntimeException("Team Member not found"));
        return mapper.toDTO(teamMember);
    }

    @Override
    public List<TeamMemberDTO> getAllTeamMembers() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO) {
        TeamMember teamMember = repository.findById(id).orElseThrow(() -> new RuntimeException("Team Member not found"));
        teamMember.setName(teamMemberDTO.getName());
        teamMember.setEmail(teamMemberDTO.getEmail());
        teamMember.setPassword(teamMemberDTO.getPassword());
        teamMember.setJoinDate(teamMemberDTO.getJoinDate());
        teamMember.setDepartment(teamMemberDTO.getDepartment());
        teamMember.setPhone(teamMemberDTO.getPhone());
        teamMember.setAddress(teamMemberDTO.getAddress());
        teamMember.setRole(teamMemberDTO.getRole());
        teamMember.setProjectName(teamMemberDTO.getProjectName());
        teamMember.setBranch(teamMemberDTO.getBranch());
        teamMember.setLeader(teamMemberDTO.isLeader());
        return mapper.toDTO(repository.save(teamMember));
    }

    @Override
    public void deleteTeamMember(Long id) {
        repository.deleteById(id);
    }
}


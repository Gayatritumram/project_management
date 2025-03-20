package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Mapper.TeamMapper;
import com.backend.project_management.Mapper.TeamMemberMapper;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.Relation;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private final TeamRepository teamRepository;
    @Autowired
    private final TeamMapper teamMapper;

    private final TeamMemberMapper teamMemberMapper;

    @Autowired
    private TeamMemberRepository memberRepository;
    @Autowired
    private TeamDTO teamDTO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper, TeamMemberMapper teamMemberMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.teamMemberMapper = teamMemberMapper;
    }

    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        return teamMapper.toDTO(team);
    }

    @Override
    public TeamDTO getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        existingTeam.setTeamName(teamDTO.getTeamName());
        return teamMapper.toDTO(teamRepository.save(existingTeam));
    }
    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }





}
package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Mapper.TeamMapper;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.TeamService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private  TeamRepository teamRepository;
    @Autowired
    private  TeamMapper teamMapper;



    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team team = teamMapper.toEntity(teamDTO);
        Team team1 = teamRepository.save(team);
        return teamMapper.toDTO(team1);
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
    public TeamDTO updateTeam(Long id,TeamDTO teamDTO) {
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
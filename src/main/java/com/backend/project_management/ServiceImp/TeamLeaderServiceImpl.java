package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Mapper.TeamLeaderMapper;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.TeamLeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private TeamLeaderMapper teamLeaderMapper;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public TeamLeaderDTO createTeamLeader(TeamLeaderDTO dto) {
        TeamLeader leader = teamLeaderMapper.toEntity(dto);

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with ID: " + dto.getTeamId()));
            leader.setTeamId(team);
        }

        TeamLeader saved = teamLeaderRepository.save(leader);
        return teamLeaderMapper.toDto(saved);
    }


    @Override
    public TeamLeaderDTO updateTeamLeader(Long id, TeamLeaderDTO dto) {
        TeamLeader leader = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with id: " + id));

        leader.setName(dto.getName());
        leader.setEmail(dto.getEmail());
        leader.setPassword(dto.getPassword()); // Optional: encode password if using security
        leader.setPhone(dto.getPhone());

        TeamLeader updated = teamLeaderRepository.save(leader);
        return teamLeaderMapper.toDto(updated);
    }

    @Override
    public void deleteTeamLeader(Long id) {
        if (!teamLeaderRepository.existsById(id)) {
            throw new RuntimeException("TeamLeader not found with id: " + id);
        }
        teamLeaderRepository.deleteById(id);
    }

    @Override
    public TeamLeaderDTO getTeamLeaderById(Long id) {
        TeamLeader leader = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with id: " + id));
        return teamLeaderMapper.toDto(leader);
    }

    @Override
    public List<TeamLeaderDTO> getAllTeamLeaders() {
        return teamLeaderRepository.findAll()
                .stream()
                .map(teamLeaderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeamLeaderDTO getTeamLeaderByEmail(String email) {
        TeamLeader leader = teamLeaderRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with email: " + email));
        return teamLeaderMapper.toDto(leader);
    }
}

package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Mapper.TeamMapper;
import com.backend.project_management.Repository.TeamMemberRepository;
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
    private final TeamRepository teamRepository;
    @Autowired
    private final TeamMapper teamMapper;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private TeamMemberDTO teamDTO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
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

    @Override
    public TeamMember createTeamLeader(TeamDTO teamMemberDTO) {
        if (!"TEAM_LEADER".equalsIgnoreCase(teamDTO.getRole())) {
            throw new IllegalArgumentException("Only TEAM_LEADER role is allowed!");
        }

        TeamMember leader = new TeamMember();
        leader.setName(teamDTO.getName());
        leader.setEmail(teamDTO.getEmail());
        leader.setPhone(teamDTO.getPhone());
        leader.setDepartment(teamDTO.getDepartment());
        leader.setBranch(teamDTO.getBranch());
        leader.setProjectName(teamDTO.getProjectName());
        leader.setRole("TEAM_LEADER"); // Assign Team Leader Role
        leader.setLeader(true);
        leader.setPassword(passwordEncoder.encode("default123")); // Set default password

        return memberRepository.save(leader);
    }
}
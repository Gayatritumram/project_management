package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamDTO;
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

    private final TeamRepository teamRepository;
    private final TeamMemberRepository memberRepository;
    private final TeamMapper teamMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TeamMapper teamMapper, TeamMemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create a new Team
    @Override
    public TeamDTO createTeam(TeamDTO teamDTO) {
        Team team = teamMapper.toEntity(teamDTO);
        team = teamRepository.save(team);
        return teamMapper.toDTO(team);
    }

     //Get Team by ID
    @Override
    public TeamDTO getTeamById(Long id) {
        return teamRepository.findById(id)
                .map(teamMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

     // Get All Teams
    @Override
    public List<TeamDTO> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toDTO)
                .collect(Collectors.toList());
    }

     //Update Team
    @Override
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        existingTeam.setTeamName(teamDTO.getTeamName());
        return teamMapper.toDTO(teamRepository.save(existingTeam));
    }

     // Delete Team
    @Override
    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Team not found");
        }
        teamRepository.deleteById(id);
    }

     //Create Team Leader
    @Override
    public TeamMember createTeamLeader(TeamDTO teamDTO) {
        if (teamDTO.getTeamName() == null || teamDTO.getBranch() == null || teamDTO.getDepartment() == null) {
            throw new IllegalArgumentException("Team Name, Branch, and Department are required!");
        }

        // Create a new Team Leader
        TeamMember leader = new TeamMember();
        leader.setName(teamDTO.getTeamName()); // Assuming team name is used as leader name
        leader.setBranch(teamDTO.getBranch());
        leader.setDepartment(teamDTO.getDepartment());
        leader.setRole("TEAM_LEADER"); // Assign Team Leader Role
        leader.setLeader(true);
        leader.setPassword(passwordEncoder.encode("default123")); // Set default password

        return memberRepository.save(leader);
    }
}

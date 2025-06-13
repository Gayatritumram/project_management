package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TeamMapper;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private  TeamRepository teamRepository;
    @Autowired
    private  TeamMapper teamMapper;

    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private ProjectAdminRepo adminRepo;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;


    @Override
    public TeamDTO createTeam(TeamDTO teamDTO, String role, String email) {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create Team");
        }
        System.out.println("Permission granted!");

        Team team = teamMapper.toEntity(teamDTO);

        String branchCode = switch (role) {
            case "ADMIN" -> adminRepo.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
            case "TEAM_LEADER" -> teamLeaderRepository.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Team Leader not found"))
                    .getBranchCode();
            default -> staffValidation.fetchBranchCodeByRole(role, email);
        };
        System.out.println("Fetched branchCode: " + branchCode);

        team.setRole(role);
        team.setCreatedByEmail(email);
        team.setBranchCode(branchCode);

        Team team1 = teamRepository.save(team);
        return teamMapper.toDTO(team1);
    }

    @Override
    public TeamDTO getTeamById(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view getTeamById");
        }
        return teamRepository.findById(id)
                .map(teamMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    @Override
    public List<TeamDTO> getAllTeams(String role, String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view getAllTeams");
        }
        return teamRepository.findAllByBranchCode(branchCode).stream()
                .map(teamMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to view class rooms");
        }
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        existingTeam.setTeamName(teamDTO.getTeamName());
        existingTeam.setBranchName(teamDTO.getBranchName());
        existingTeam.setDepartment(teamDTO.getDepartment());

        return teamMapper.toDTO(teamRepository.save(existingTeam));
    }









    @Override
    public void deleteTeam(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view class rooms");
        }
        teamRepository.deleteById(id);

    }


}
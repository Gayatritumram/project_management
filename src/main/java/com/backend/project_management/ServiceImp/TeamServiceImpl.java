package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TeamMapper;
import com.backend.project_management.Repository.BranchAdminRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
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
    private StaffValidation  staffValidation;

    @Autowired
    private BranchAdminRepository adminRepo;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private TeamMemberRepository teamMemberRepository;


    @Override
    public TeamDTO createTeam(TeamDTO teamDTO, String role, String email) {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create Team");
        }
        System.out.println("Permission granted!");

        Team team = TeamMapper.toEntity(teamDTO);

        String branchCode = switch (role) {
            case "BRANCH" -> adminRepo.findByBranchEmail(email)
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

        // Attach team leader
        if (teamDTO.getTeamLeaderId() != null) {
            TeamLeader leader = teamLeaderRepository.findById(teamDTO.getTeamLeaderId())
                    .orElseThrow(() -> new RuntimeException("Team Leader not found"));
            leader.setTeamId(team); // Set reverse mapping
            team.setTeamLeader(leader);
        }

        // Attach team members
        if (teamDTO.getTeamMemberList() != null && !teamDTO.getTeamMemberList().isEmpty()) {
            List<Long> memberIds = teamDTO.getTeamMemberList().stream()
                    .map(TeamMemberDTO::getId)
                    .toList();

            List<TeamMember> members = teamMemberRepository.findAllById(memberIds);
            for (TeamMember member : members) {
                member.setTeamId(team); // Reverse mapping
            }
            team.setMemberList(members);
        }

        Team team1 = teamRepository.save(team);
        return TeamMapper.toDTO(team1);
    }

    @Override
    public TeamDTO getTeamById(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view getTeamById");
        }
        return teamRepository.findById(id)
                .map(TeamMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Team not found"));
    }

    @Override
    public List<TeamDTO> getAllTeams(String role, String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view getAllTeams");
        }
        return teamRepository.findAllByBranchCode(branchCode).stream()
                .map(TeamMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    public TeamDTO updateTeam(Long id, TeamDTO teamDTO, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to update team details");
        }

        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));

        // 🔁 Update only if not null
        if (teamDTO.getTeamName() != null) {
            existingTeam.setTeamName(teamDTO.getTeamName());
        }

        if (teamDTO.getBranchName() != null) {
            existingTeam.setBranchName(teamDTO.getBranchName());
        }

        if (teamDTO.getTeamLeaderId() != null) {
            TeamLeader teamLeader = teamLeaderRepository.findById(teamDTO.getTeamLeaderId())
                    .orElseThrow(() -> new RuntimeException("Team Leader not found"));
            existingTeam.setTeamLeader(teamLeader);
        }

        if (teamDTO.getTeamMemberList() != null && !teamDTO.getTeamMemberList().isEmpty()) {
            List<TeamMember> members = teamDTO.getTeamMemberList().stream()
                    .map(dto -> teamMemberRepository.findById(dto.getId())
                            .orElseThrow(() -> new RuntimeException("Team Member not found with ID: " + dto.getId())))
                    .collect(Collectors.toList());
            existingTeam.setMemberList(members);
        }

        if (teamDTO.getDepartmentName() != null) {
            existingTeam.setDepartmentName(teamDTO.getDepartmentName());
        }

        // 💾 Save & return updated DTO
        return TeamMapper.toDTO(teamRepository.save(existingTeam));
    }












    @Override
    public void deleteTeam(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view class rooms");
        }
        teamRepository.deleteById(id);

    }


}
package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TeamMemberMapper;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.EmailService;
import com.backend.project_management.Service.OtpService;
import com.backend.project_management.Service.TeamMemberService;
import com.backend.project_management.UserPermission.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {

    @Autowired
    private TeamMemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Override
    public TeamMemberDTO createTeamMember(TeamMemberDTO dto) {
        TeamMember teamMember = TeamMemberMapper.mapToTeamMember(dto);
        teamMember.setPassword(passwordEncoder.encode(dto.getPassword()));
        teamMember = repository.save(teamMember);
        return TeamMemberMapper.mapToTeamMemberDTO(teamMember);
    }

    @Override
    public TeamMemberDTO getTeamMemberById(Long id) {
        TeamMember teamMember = repository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Team Member not found"));
        return TeamMemberMapper.mapToTeamMemberDTO(teamMember);
    }

    @Override
    public List<TeamMemberDTO> getAllNonLeaderTeamMembers() {
        return repository.findAll().stream()
                .filter(teamMember -> !teamMember.isLeader())
                .map(TeamMemberMapper::mapToTeamMemberDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TeamMemberDTO> getAllLeaderTeamMembers() {
        return repository.findAll().stream()
                .filter(TeamMember::isLeader)
                .map(TeamMemberMapper::mapToTeamMemberDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void makeTeamLeader(Long id) {
        TeamMember teamMember = repository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Team Member not found"));
        teamMember.setLeader(true);
        teamMember.setUserRole(UserRole.TEAM_LEADER);
        repository.save(teamMember);
    }

    @Override
    public TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO) {
        TeamMember teamMember = repository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Team Member not found"));

        teamMember.setName(teamMemberDTO.getName());
        teamMember.setEmail(teamMemberDTO.getEmail());
        teamMember.setJoinDate(teamMemberDTO.getJoinDate());
        teamMember.setDepartment(teamMemberDTO.getDepartment());
        teamMember.setPhone(teamMemberDTO.getPhone());
        teamMember.setAddress(teamMemberDTO.getAddress());
        teamMember.setRoleName(teamMemberDTO.getRole());
        teamMember.setProjectName(teamMemberDTO.getProjectName());
        teamMember.setBranchName(teamMemberDTO.getBranchName());
        teamMember.setLeader(teamMemberDTO.isLeader());

        return TeamMemberMapper.mapToTeamMemberDTO(repository.save(teamMember));
    }

    @Override
    public void deleteTeamMember(Long id) {
        repository.deleteById(id);
    }

    @Override
    public String forgotPassword(String email) {
        Optional<TeamMember> optionalMember = repository.findByEmail(email);
        if (optionalMember.isPresent()) {
            boolean otpSent = otpService.generateAndSendOTP(email);
            if (otpSent) {
                return "OTP has been sent to your email: " + email;
            } else {
                throw new IllegalArgumentException("Error in sending OTP. Please try again.");
            }
        } else {
            throw new IllegalArgumentException("Team Member email not found!");
        }
    }

    @Override
    public String verifyOtp(String email, int otp) {
        boolean isOtpValid = otpService.verifyOTP(email, otp);
        if (!isOtpValid) {
            throw new IllegalArgumentException("Invalid or expired OTP!");
        }
        return "OTP is valid. You can now reset your password.";
    }

    @Override
    public String resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        Optional<TeamMember> optionalMember = repository.findByEmail(email);
        if (optionalMember.isPresent()) {
            TeamMember teamMember = optionalMember.get();
            teamMember.setPassword(passwordEncoder.encode(newPassword));
            repository.save(teamMember);
            return "Password successfully reset.";
        } else {
            throw new IllegalArgumentException("Team Member email not found!");
        }
    }
}

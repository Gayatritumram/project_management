package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TeamMemberMapper;
import com.backend.project_management.Repository.*;
import com.backend.project_management.Service.EmailService;
import com.backend.project_management.Service.OtpService;
import com.backend.project_management.Service.TeamMemberService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired private S3Service s3Service;
    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private BranchAdminRepository adminRepo;

    @Autowired private TaskRepository taskRepository;





    @Override
    public TeamMember  createTeamMember(TeamMemberDTO dto, MultipartFile imageFile , String role, String email) throws IOException {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create TeamMember");
        }
        System.out.println("Permission granted!");

        TeamMember teamMember = TeamMemberMapper.mapToTeamMember(dto);

        String branchCode;
        if (role.equals("BRANCH")) {
            branchCode = adminRepo.findByBranchEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
        } else {
            branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        }
        System.out.println("Fetched branchCode: " + branchCode);

        teamMember.setRole("TEAM_MEMBER");
        teamMember.setCreatedByEmail(email);
        teamMember.setBranchCode(branchCode);
        teamMember.setPassword(passwordEncoder.encode(dto.getPassword()));

        if (dto.getTeamId() != null) {
            Team admin = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            teamMember.setTeamId(admin);

        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = s3Service.uploadImage(imageFile); // S3 or local path
            teamMember.setImageUrl(imageUrl);
        }

        return repository.save(teamMember);
    }

    @Override
    public TeamMemberDTO getTeamMemberById(Long id,String role,String  email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view TeamMember");
        }
        TeamMember teamMember = repository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Team Member not found"));
        return TeamMemberMapper.mapToTeamMemberDTO(teamMember);
    }
    @Override
    public TeamMemberDTO getTeamMemberByEmail(String memberEmail, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view TeamMember");
        }
        TeamMember teamMember = repository.findByEmail(memberEmail)
                .orElseThrow(() -> new RequestNotFound("Team Member not found with email: " + memberEmail));

        return TeamMemberMapper.mapToTeamMemberDTO(teamMember);
    }


    @Override
    public List<TeamMemberDTO> getAllTeamMembers(String role,String  email,String  branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view TeamMember");
        }
        return repository.findAllByBranchCode(branchCode)
                .stream()
                .map(TeamMemberMapper::mapToTeamMemberDTO)

                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void makeTeamLeader(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to promote team member");
        }

        TeamMember teamMember = repository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Team Member not found"));

        // Step 1: Remove references from tasks
        List<Task> assignedTasks = taskRepository.findByAssignedToTeamMember(teamMember);
        for (Task task : assignedTasks) {
            task.setAssignedToTeamMember(null); // or reassign to someone else if needed
        }
        taskRepository.saveAll(assignedTasks);

        // Step 2: Create and save TeamLeader
        TeamLeader teamLeader = new TeamLeader();
        teamLeader.setName(teamMember.getName());
        teamLeader.setEmail(teamMember.getEmail());
        teamLeader.setPassword(passwordEncoder.encode(teamMember.getPassword()));
        teamLeader.setTeamId(teamMember.getTeamId());
        teamLeader.setBranchName(teamMember.getBranchName());
        teamLeader.setJoinDate(teamMember.getJoinDate());
        teamLeader.setDepartment(teamMember.getDepartment());
        teamLeader.setAddress(teamMember.getAddress());
        teamLeader.setPhone(teamMember.getPhone());
        teamLeader.setCreatedByEmail(teamMember.getCreatedByEmail());
        teamLeader.setBranchCode(teamMember.getBranchCode());
        teamLeader.setRole("TEAM_LEADER"); // 🔁 update role
        teamLeader.setImageUrl(teamMember.getImageUrl());

        teamLeaderRepository.save(teamLeader);

        // Step 3: Now safely delete the team member
        repository.delete(teamMember);
    }

    @Override
    public TeamMemberDTO updateTeamMember(Long id, TeamMemberDTO teamMemberDTO,String role,String  email) {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to view TeamMember");
        }
        TeamMember teamMember = repository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Team Member not found"));

        teamMember.setName(teamMemberDTO.getName());
        teamMember.setEmail(teamMemberDTO.getEmail());
        teamMember.setJoinDate(teamMemberDTO.getJoinDate());
        teamMember.setDepartment(teamMemberDTO.getDepartment());
        teamMember.setPhone(teamMemberDTO.getPhone());
        teamMember.setAddress(teamMemberDTO.getAddress());
        teamMember.setRoleName(teamMemberDTO.getRoleName());
        teamMember.setProjectName(teamMemberDTO.getProjectName());
        teamMember.setBranchName(teamMemberDTO.getBranchName());


        return TeamMemberMapper.mapToTeamMemberDTO(repository.save(teamMember));
    }

    @Override
    public void deleteTeamMember(Long id,String role,String  email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view TeamMember");
        }

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



    @Override
    public void updateTeamMemberProfilePicture(Long memberId, MultipartFile imageFile, String role, String email) {
        // Permission check
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to update the profile picture of a Team Member");
        }

        // Fetch the Team Member
        TeamMember teamMember = repository.findById(memberId)
                .orElseThrow(() -> new RequestNotFound("Team Member with ID " + memberId + " not found"));

        // Validate the image file
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Profile picture cannot be empty");
        }

        try {
            // Upload image and get the URL
            String newImageUrl = s3Service.uploadImage(imageFile);

            // Optional: delete the old image from S3 if needed
            String oldImageUrl = teamMember.getImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                s3Service.deleteImage(oldImageUrl);
            }

            // Update profile picture URL
            teamMember.setImageUrl(newImageUrl);
            repository.save(teamMember); // Persist the update

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture: " + e.getMessage(), e);
        }
    }



}

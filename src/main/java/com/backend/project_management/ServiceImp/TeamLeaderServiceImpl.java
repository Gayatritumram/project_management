package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TeamDTO;
import com.backend.project_management.DTO.TeamLeaderDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.BranchAdmin;
import com.backend.project_management.Entity.Team;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TeamLeaderMapper;
import com.backend.project_management.Mapper.TeamMapper;
import com.backend.project_management.Pagination.TeamLeaderSpecification;
import com.backend.project_management.Repository.BranchAdminRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamRepository;
import com.backend.project_management.Service.EmailService;
import com.backend.project_management.Service.OtpService;
import com.backend.project_management.Service.TeamLeaderService;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamLeaderServiceImpl implements TeamLeaderService {

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private TeamLeaderMapper teamLeaderMapper;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired private S3Service s3Service;

    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private BranchAdminRepository adminRepo;




    @Override
    public TeamLeaderDTO createTeamLeader(TeamLeaderDTO dto, MultipartFile imageFile,String role, String email)throws IOException {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("Access denied: You are not allowed to create a Team Leader.");

        }
        System.out.println("Permission granted!");

        TeamLeader leader = new TeamLeader();
        String branchCode;
        if (role.equals("BRANCH")) {
            branchCode = adminRepo.findByBranchEmail(email)
                    .orElseThrow(() -> new RequestNotFound("BRANCH_Admin not found"))
                    .getBranchCode();
        } else {
            branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        }

        leader.setRole("TEAM_LEADER");
        leader.setCreatedByEmail(email);
        leader.setBranchCode(branchCode);


        leader.setName(dto.getName());
        leader.setEmail(dto.getEmail());
        leader.setPassword(passwordEncoder.encode(dto.getPassword())); // Hash if needed
        leader.setPhone(dto.getPhone());
        leader.setAddress(dto.getAddress());
        leader.setDepartmentName(dto.getDepartmentName());
        leader.setBranchName(dto.getBranchName());
        if (dto.getJoinDate() != null) {
            leader.setJoinDate(dto.getJoinDate());
        }


        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RequestNotFound("Team with ID " + dto.getTeamId() + " not found"));
            leader.setTeam(team);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = s3Service.uploadImage(imageFile); // S3 or local path
            leader.setImageUrl(imageUrl);
        }

        TeamLeader savedLeader = teamLeaderRepository.save(leader);
        return teamLeaderMapper.toDto(savedLeader);
    }


    @Override
    public TeamLeaderDTO updateTeamLeader(Long id, TeamLeaderDTO teamLeaderDTO,String  role, String email) {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to view TeamLeader");
        }


        TeamLeader leader = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with id: " + id));

        leader.setName(teamLeaderDTO.getName());
        leader.setEmail(teamLeaderDTO.getEmail());

        if (teamLeaderDTO.getPassword() != null && !teamLeaderDTO.getPassword().isBlank()) {
            leader.setPassword(passwordEncoder.encode(teamLeaderDTO.getPassword()));
        }

        leader.setPhone(teamLeaderDTO.getPhone());
        leader.setAddress(teamLeaderDTO.getAddress());
        leader.setDepartmentName(teamLeaderDTO.getDepartmentName());
        leader.setBranchName(teamLeaderDTO.getBranchName());

        TeamLeader updated = teamLeaderRepository.save(leader);
        return teamLeaderMapper.toDto(updated);
    }

    @Override
    public void deleteTeamLeader(Long id,String role, String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view TeamLeader");
        }

        if (!teamLeaderRepository.existsById(id)) {
            throw new RuntimeException("TeamLeader not found with id: " + id);
        }
        teamLeaderRepository.deleteById(id);
    }

    @Override
    public TeamLeaderDTO getTeamLeaderById(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view TeamLeader");
        }


        return teamLeaderRepository.findById(id)
                .map(teamLeaderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with id: " + id));
    }


    @Override
    public List<TeamLeaderDTO> getAllTeamLeaders(String role, String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view TeamLeader");
        }


        return teamLeaderRepository.findAllByBranchCode(branchCode)
                .stream()
                .map(teamLeaderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TeamLeaderDTO getTeamLeaderByEmail(String email,String role,String emailFind) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view TeamLeader");
        }


        return teamLeaderRepository.findByEmail(emailFind)
                .map(teamLeaderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("TeamLeader not found with email: " + email));
    }

    @Override
    public String forgotPassword(String email) {
        Optional<TeamLeader> optionalTeamLeader = teamLeaderRepository.findByEmail(email);
        if (optionalTeamLeader.isPresent()) {
            boolean otpSent = otpService.generateAndSendOTP(email);
            if (otpSent) {
                return "OTP has been sent to your email: " + email;
            } else {
                throw new IllegalArgumentException("Error in sending OTP. Please try again.");
            }
        } else {
            throw new IllegalArgumentException("Team Leader email not found!");
        }
    }

    @Override
    public String verifyOtp(String email, int otp) {
        if (otpService.verifyOTP(email, otp)) {
            return "OTP is valid. You can now reset your password.";
        }
        throw new IllegalArgumentException("Invalid or expired OTP!");
    }

    @Override
    public String resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        Optional<TeamLeader> optionalLeader = teamLeaderRepository.findByEmail(email);
        if (optionalLeader.isPresent()) {
            TeamLeader teamLeader = optionalLeader.get();
            teamLeader.setPassword(passwordEncoder.encode(newPassword));
            teamLeaderRepository.save(teamLeader);
            return "Password successfully reset.";
        } else {
            throw new IllegalArgumentException("Team Leader email not found!");
        }
    }

    @Override
    public void updateTeamMemberProfilePicture(Long leaderId, MultipartFile imageFile, String role, String email) {
        // Permission check
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to update the profile picture of a Team Member");
        }

        // Fetch the Team Member
        TeamLeader teamLeader = teamLeaderRepository.findById(leaderId)
                .orElseThrow(() -> new RequestNotFound("Team Member with ID " + leaderId + " not found"));

        // Validate the image file
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Profile picture cannot be empty");
        }

        try {
            // Upload image and get the URL
            String newImageUrl = s3Service.uploadImage(imageFile);

            // Optional: delete the old image from S3 if needed
            String oldImageUrl = teamLeader.getImageUrl();
            if (oldImageUrl != null && !oldImageUrl.isEmpty()) {
                s3Service.deleteImage(oldImageUrl);
            }

            // Update profile picture URL
            teamLeader.setImageUrl(newImageUrl);
            teamLeaderRepository.save(teamLeader); // Persist the update

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture: " + e.getMessage(), e);
        }
    }

    @Override
    public TeamLeaderDTO getTeamLeaderByName(String name, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("Access denied: Not allowed to view Team Leader info.");
        }

        TeamLeader teamLeader = teamLeaderRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Team Leader not found with name: " + name));
        return teamLeaderMapper.toDto(teamLeader);
    }





    @Override
    public Page<TeamLeaderDTO> getAllTeamLeaders(String role, String email, String branchCode,
                                                 String searchBy, int page, int size,
                                                 String sortBy, String sortDir) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("Access denied");
        }

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<TeamLeader> spec = TeamLeaderSpecification.filter(branchCode, searchBy)
                .and((root, query, cb) -> cb.notEqual(root.get("email"), email));

        Page<TeamLeader> result = teamLeaderRepository.findAll(spec, pageable);

        List<TeamLeaderDTO> dtoList = result.getContent()
                .stream()
                .map(teamLeaderMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, result.getTotalElements());
    }

    @Override
    public List<TeamMemberDTO> getAllTeamMemberByLeaderId(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("Access denied");
        }
        TeamLeader teamLeader = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RequestNotFound("Team Member with ID " + id + " not found"));

        TeamDTO team = teamRepository.findById(teamLeader.getTeam().getId())
                .map(TeamMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Team not found"));


        return team.getTeamMemberList();
    }

}

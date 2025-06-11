package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Entity.ProjectAdmin;

import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.ProjectAdminMapper;

import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Service.EmailService;
import com.backend.project_management.Service.OtpService;
import com.backend.project_management.Service.ProjectAdminService;


import com.backend.project_management.Util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.backend.project_management.Exception.EmailAlreadyExistsException;

@Service
public class ProjectAdminServiceImpl implements ProjectAdminService {

    @Autowired
    private ProjectAdminRepo adminRepo;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TeamLeaderRepository teamLeaderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private JwtHelper  jwtHelper;

    @Autowired
    private TeamMemberRepository teamMemberRepository;






    @Override
    public ProjectAdminDTO registerAdmin(ProjectAdminDTO adminDTO,String role,String  email) {
        // Check if email already exists in any user table
        if (adminRepo.findByEmail(email).isPresent() ){
            throw new EmailAlreadyExistsException("Email is already registered, please use a different email");
        }

        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to create ProjectAdmin");
        }
        System.out.println("Permission granted!");

        ProjectAdmin projectAdmin = ProjectAdminMapper.toEntity(adminDTO);

        String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        System.out.println("Fetched branchCode: " + branchCode);

        projectAdmin.setCreatedByRole(role);
        projectAdmin.setRole("ADMIN");
        projectAdmin.setCreatedByEmail(email);
        projectAdmin.setBranchCode(branchCode);
        projectAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));

        ProjectAdmin saveAdmin = adminRepo.save(projectAdmin);
        System.out.println("Project Admin saved with id: " + saveAdmin.getId());

        return ProjectAdminMapper.toDTO(adminRepo.save(saveAdmin));
    }


    @Override
    public ProjectAdminDTO findAdminByEmail(String role,String emailFind,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("No permission to view ProjectAdmin");
        }

        Optional<ProjectAdmin> optionalAdmin = adminRepo.findByEmail(emailFind);
        if (optionalAdmin.isEmpty()) {
            throw new IllegalArgumentException("Admin not found!");
        }

        ProjectAdmin projectAdmin = optionalAdmin.get();
        return ProjectAdminMapper.toDTO(projectAdmin);

    }

    @Override
    public List<ProjectAdminDTO> findAllAdmin(String role, String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("No permission to view ProjectAdmin");
        }
        List<ProjectAdmin> projectAdmin = adminRepo.findAllByBranchCode(branchCode);
        return  projectAdmin.stream().map( ProjectAdminMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public void deleteProjectAdmin(Long id,String role, String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view projectAdmin");
        }

        ProjectAdmin projectAdmin = adminRepo.findById(id)
                .orElseThrow(() -> new RequestNotFound("projectAdmin not found with id: " + id));
        adminRepo.delete(projectAdmin);
    }





    public String forgotPassword(String email) {
        Optional<ProjectAdmin> optionalAdmin = adminRepo.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            boolean otpSent = otpService.generateAndSendOTP(email);
            if (otpSent) {
                return "OTP has been sent to your email: " + email;
            } else {
                throw new IllegalArgumentException("Error in sending OTP. Please try again.");
            }
        } else {
            throw new IllegalArgumentException("Admin email not found!");
        }
    }

    public String verifyOtp(String email, int otp) {
        boolean isOtpValid = otpService.verifyOTP(email, otp);
        if (!isOtpValid) {
            throw new IllegalArgumentException("Invalid or expired OTP!");
        }
        return "OTP is valid. You can now reset your password.";
    }

    public String resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        Optional<ProjectAdmin> optionalAdmin = adminRepo.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            ProjectAdmin projectAdminDTO = optionalAdmin.get();
            projectAdminDTO.setPassword(passwordEncoder.encode(newPassword)); // Consider encrypting the password before saving
            adminRepo.save(projectAdminDTO);
            return "Password successfully reset.";
        } else {
            throw new IllegalArgumentException("Admin email not found!");
        }
    }


}

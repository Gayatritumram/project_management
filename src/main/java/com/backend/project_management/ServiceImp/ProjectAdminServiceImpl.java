package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.ProjectAdminMapper;

import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.EmailService;
import com.backend.project_management.Service.OtpService;
import com.backend.project_management.Service.ProjectAdminService;


import com.backend.project_management.UserPermission.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectAdminServiceImpl implements ProjectAdminService {

    @Autowired
    private ProjectAdminRepo adminRepo;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpService otpService;




    @Override
    public ProjectAdminDTO registerAdmin(ProjectAdminDTO adminDTO) {
        ProjectAdmin projectAdmin=ProjectAdminMapper.toEntity(adminDTO);
        projectAdmin.setEmail(adminDTO.getEmail());
        projectAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));

        // Ensure userRole1 is not null
        projectAdmin.setUserRole1(adminDTO.getUserRole1() != null ? adminDTO.getUserRole1() : UserRole.ADMIN);


        return ProjectAdminMapper.toDTO(adminRepo.save(projectAdmin));
    }


    @Override
    public ProjectAdminDTO findAdminByEmail(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String email1 = userDetails.getUsername();
        String password = userDetails.getPassword();
        ProjectAdminDTO projectAdminDTO = new ProjectAdminDTO();
        projectAdminDTO.setEmail(email1);
        projectAdminDTO.setPassword(password);


        return projectAdminDTO;

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
            projectAdminDTO.setPassword(newPassword); // Consider encrypting the password before saving
            adminRepo.save(projectAdminDTO);
            return "Password successfully reset.";
        } else {
            throw new IllegalArgumentException("Admin email not found!");
        }
    }




}

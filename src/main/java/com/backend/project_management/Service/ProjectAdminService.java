package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Model.JwtRequest;
import com.backend.project_management.Model.JwtResponse;

import java.util.List;


public interface ProjectAdminService {
    ProjectAdminDTO registerAdmin(ProjectAdminDTO adminDTO,String role,String  email);

    ProjectAdminDTO findAdminByEmail(String role,String emailFind,String email);
    List<ProjectAdminDTO> findAllAdmin(String role, String email, String branchCode);

    String forgotPassword(String email);

    String verifyOtp(String email, int otp);

    String resetPassword(String email, String newPassword, String confirmPassword);

    void deleteProjectAdmin(Long id,String role, String email);
    JwtResponse login(JwtRequest request);

}

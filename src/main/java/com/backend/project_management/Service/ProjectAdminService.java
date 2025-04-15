package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectAdminDTO;


public interface ProjectAdminService {
    ProjectAdminDTO registerAdmin(ProjectAdminDTO adminDTO);

    ProjectAdminDTO findAdminByEmail(String email);

    String forgotPassword(String email);

    String verifyOtp(String email, int otp);

    String resetPassword(String email, String newPassword, String confirmPassword);


}

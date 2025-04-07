package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TeamMemberDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.TeamMember;

import java.util.Optional;

public interface ProjectAdminService {
    ProjectAdminDTO registerAdmin(ProjectAdminDTO adminDTO);

    ProjectAdminDTO findAdminByEmail(String email);

    String forgotPassword(String email);

    String verifyOtp(String email, int otp);

    String resetPassword(String email, String newPassword, String confirmPassword);


}

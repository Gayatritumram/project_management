package com.backend.project_management.Service;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Entity.ProjectAdmin;

import java.util.Optional;

public interface ProjectAdminService {

    ProjectAdmin registerAdmin(ProjectAdminDTO adminDTO);

    Optional<ProjectAdmin> findAdminByEmail(String email);
}

package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Exception.AdminNotFoundException;
import com.backend.project_management.Mapper.ProjectAdminMapper;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Service.ProjectAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectAdminServiceImpl implements ProjectAdminService {

    @Autowired
    private ProjectAdminRepo adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProjectAdminMapper adminMapper;

    @Override
    public ProjectAdmin registerAdmin(ProjectAdminDTO adminDTO) {
        if (!adminDTO.getPassword().equals(adminDTO.getCpassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        // Check if the email is already in use
        if (adminRepo.findByEmail(adminDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        ProjectAdmin admin = adminMapper.toEntity(adminDTO);

        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        return adminRepo.save(admin);
    }

    @Override
    public Optional<ProjectAdmin> findAdminByEmail(String email) {
        return adminRepo.findByEmail(email);
    }


}


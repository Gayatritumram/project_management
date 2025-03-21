package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.ProjectAdminDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Service.ProjectAdminService;
import com.backend.project_management.Util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectAdminServiceImpl implements ProjectAdminService {
    private final ProjectAdminRepo adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ProjectAdminServiceImpl(ProjectAdminRepo adminRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public ProjectAdmin registerAdmin(ProjectAdminDTO adminDTO) {
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered!");
        }

        // Create new ProjectAdmin entity
        ProjectAdmin admin = new ProjectAdmin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPhone(adminDTO.getPhone());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword())); // Encrypt password

        return adminRepository.save(admin);
    }

    @Override
    public String loginAdmin(String email, String password) {
        Optional<ProjectAdmin> admin = adminRepository.findByEmail(email);

        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
            return jwtUtil.generateToken(email); // ✅ Generate and return JWT token
        }
        throw new RuntimeException("Invalid credentials!");
    }
}

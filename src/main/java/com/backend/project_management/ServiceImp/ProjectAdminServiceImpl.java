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




}

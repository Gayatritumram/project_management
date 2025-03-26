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
        projectAdmin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        return ProjectAdminMapper.toDTO(adminRepo.save(projectAdmin));
    }

    @Override
    public String loginAdmin(String email, String password) {
        ProjectAdminDTO admin = this.findAdminByEmail(email);
        if (passwordEncoder.matches(password, admin.getPassword())) {
            return "Login Done";
        } else {
            throw new RequestNotFound("Invalid email or password");
        }
    }

    @Override
    public ProjectAdminDTO findAdminByEmail(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String email1 = userDetails.getUsername();
        String password = userDetails.getPassword();
        ProjectAdminDTO projectAdminDTO = new ProjectAdminDTO();
        projectAdminDTO.setEmail(email1);
        projectAdminDTO.setPassword(password);


//        return ProjectAdminMapper.toDTO(
//                adminRepo.findByEmail(email)
//                        .orElseThrow(() -> new RequestNotFound("ProjectAdmin not found with email: " + email)));

        return projectAdminDTO;

    }

    @Override
    public TeamMember createTeamLeader(TeamMemberDTO teamMemberDTO) {
        TeamMember leader = new TeamMember();
        leader.setName(teamMemberDTO.getName());
        leader.setEmail(teamMemberDTO.getEmail());
        leader.setPhone(teamMemberDTO.getPhone());
        leader.setRole("TEAM_LEADER");
        leader.setPassword(passwordEncoder.encode(teamMemberDTO.getPassword()));

        return memberRepository.save(leader);
    }


    @Override
    public TeamMember createTeamMember(TeamMemberDTO teamMemberDTO) {
        TeamMember member = new TeamMember();
        member.setName(teamMemberDTO.getName());
        member.setEmail(teamMemberDTO.getEmail());
        member.setPhone(teamMemberDTO.getPhone());
        member.setRole("TEAM_MEMBER");
        member.setPassword(passwordEncoder.encode(teamMemberDTO.getPassword()));

        return memberRepository.save(member);
    }


}

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
import com.backend.project_management.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
    private JwtUtil jwtUtil;


    @Override
    public ProjectAdmin registerAdmin(ProjectAdminDTO adminDTO) {
        if (!adminDTO.getPassword().equals(adminDTO.getCpassword())) {
            throw new IllegalArgumentException("Passwords do not match!");
        }

        ProjectAdmin admin = new ProjectAdmin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPhone(adminDTO.getPhone());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));

        return adminRepo.save(admin);
    }

    @Override
    public String loginAdmin(String email, String password) {
        Optional<ProjectAdmin> admin = adminRepo.findByEmail(email);

        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
            return jwtUtil.generateToken(email);
        } else {
            throw new RuntimeException("Invalid email or password");
        }
    }

    @Override
    public ProjectAdminDTO findAdminByEmail(String email) {
        return ProjectAdminMapper.toDTO(adminRepo.findByEmail(email) .orElseThrow(() -> new RequestNotFound("ProjectAdmin not found with email: " + email)));
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

    @Override
    public Task assignTask(TaskDTO taskDTO) {
        Optional<TeamMember> assignedMember = memberRepository.findById(taskDTO.getAssignedTo());

        if (assignedMember.isEmpty()) {
            throw new RuntimeException("Assigned member not found!");
        }

        Task task = new Task();
        task.setDescription(taskDTO.getDescription());
        task.setProjectName(taskDTO.getProjectName());
        task.setDays(taskDTO.getDays());
        task.setHour(taskDTO.getHour());
        task.setStatus("Assigned");
        task.setStatusBar(taskDTO.getStatusBar());
        task.setStartDate(taskDTO.getStartDate());
        task.setEndDate(taskDTO.getEndDate());
        task.setStartTime(taskDTO.getStartTime());
        task.setEndTime(taskDTO.getEndTime());
        task.setImageUrl(taskDTO.getImageUrl());
        task.setDurationInMinutes(taskDTO.getDurationInMinutes());
        task.setSubject(taskDTO.getSubject());
        task.setAssignedTo(assignedMember.get()); // Set assigned member

        return taskRepository.save(task);
    }
}

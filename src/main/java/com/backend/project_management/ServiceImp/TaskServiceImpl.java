package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Entity.*;
import com.backend.project_management.Exception.RequestNotFound;
import com.backend.project_management.Mapper.TaskMapper;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.TaskService;
import com.backend.project_management.Util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired private TaskRepository taskRepository;
    @Autowired private TaskMapper taskMapper;
    @Autowired private S3Service s3Service;
    @Autowired private ProjectAdminRepo projectAdminRepo;
    @Autowired private TeamLeaderRepository teamLeaderRepository;
    @Autowired private TeamMemberRepository teamMemberRepository;
    @Autowired private JwtHelper jwtHelper;
    @Autowired
    private StaffValidation  staffValidation;

    @Autowired
    private ProjectAdminRepo adminRepo;






    @Autowired
    private TeamMemberRepository repository;



    @Override
    public TaskDTO createTask(TaskDTO taskDTO, String token, Long id, MultipartFile file, String role, String email) throws IOException {

        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to createTask");
        }
        System.out.println("Permission granted!");

        Task task = taskMapper.toEntity(taskDTO);

        String branchCode = switch (role) {
            case "ADMIN" -> adminRepo.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Admin not found"))
                    .getBranchCode();
            case "TEAM_LEADER" -> teamLeaderRepository.findByEmail(email)
                    .orElseThrow(() -> new RequestNotFound("Team Leader not found"))
                    .getBranchCode();
            default -> staffValidation.fetchBranchCodeByRole(role, email);
        };
        System.out.println("Fetched branchCode: " + branchCode);

        task.setRole(role);
        task.setCreatedByEmail(email);
        task.setBranchCode(branchCode);


        try {
            if (role.contains("ADMIN")) {
                ProjectAdmin currentAdmin = projectAdminRepo.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in admin not found"));
                System.out.println("currentAdmin: " + currentAdmin);
                task.setAssignedByAdmin(currentAdmin);
            } else if (role.contains("TEAM_LEADER")) {
                TeamLeader currentLeader = teamLeaderRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in team leader not found"));
                System.out.println("currentLeader: " + currentLeader);
                task.setAssignedByLeader(currentLeader);
            }
        } catch (RuntimeException e) {
            System.err.println("Warning: " + e.getMessage()); // log and continue
        }

        TeamMember assignedTo = teamMemberRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Team member not found"));
        task.setAssignedToTeamMember(assignedTo);


        if (file != null && !file.isEmpty()) {
           try {
                task.setImageUrl(s3Service.uploadImage(file));
          } catch (IOException e) {
                throw new RuntimeException(e);
           }
        }

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "PUT")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }

        return null;
    }

    @Override
    public TaskDTO getTaskById(Long taskId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDto(task);
    }


    @Override
    public List<TaskDTO> getAllTasks(String role, String email, String branchCode) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        return taskRepository.findAllByBranchCode(branchCode).stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long taskId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "DELETE")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    @Override
    public List<TaskDTO> getTasksAssignedToMemberById(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedToTeamMember_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderId(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByLeader_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminId(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByAdmin_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminEmail(String emailFind, String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByAdminEmail(emailFind);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderEmail(String emailFind,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedByLeaderEmail(emailFind);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedToMemberEmail(String emailFind,String role,String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedToTeamMemberEmail(emailFind);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTodaysLeaderTasksByEmail(String emailFind,String email, String role) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTodaysTasksAssignedToLeader(emailFind);
        return tasks.stream().map(taskMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public TaskDTO createTaskForLeader(TaskDTO taskDTO, String token, Long id, MultipartFile file, String role, String email) throws IOException {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to createTask");
        }
        System.out.println("Permission granted!");

        Task task = taskMapper.toEntity(taskDTO);

        String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        System.out.println("Fetched branchCode: " + branchCode);

        task.setRole(role);
        task.setCreatedByEmail(email);
        task.setBranchCode(branchCode);

        try {
            if (role.contains("ADMIN")) {
                ProjectAdmin currentAdmin = projectAdminRepo.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in admin not found"));
                task.setAssignedByAdmin(currentAdmin);
            } else if (role.contains("TEAM_LEADER")) {
                TeamLeader currentLeader = teamLeaderRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Logged-in team leader not found"));
                task.setAssignedByLeader(currentLeader);
            }
        } catch (RuntimeException e) {
            System.err.println("Warning: " + e.getMessage()); // log and continue
        }

        TeamLeader assignedTo = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team Leader not found"));

        task.setAssignedToTeamLeader(assignedTo);

        if (file != null && !file.isEmpty()) {
            task.setImageUrl(s3Service.uploadImage(file));
        }

        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);

    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminToMember(Long adminId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }

        List<Task> tasks = taskRepository.findTasksAssignedByAdminToMember(adminId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTodaysTasksAssignedByAdminToMember(Long adminId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTodaysTasksAssignedByAdminToMember(adminId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderToMember(Long leaderId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTasksAssignedByLeaderToMember(leaderId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTodaysTasksAssignedByLeaderToMember(Long leaderId, Long memberId, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findTodaysTasksAssignedByLeaderToMember(leaderId, memberId);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public TaskDTO assignTaskFromLeaderToMember(TaskDTO taskDTO, String token, Long leaderId, Long memberId, MultipartFile file, String role, String email) throws IOException {
        System.out.println("Checking permission for role: " + role + ", email: " + email);
        if (!staffValidation.hasPermission(role, email, "POST")) {
            System.out.println("Permission denied!");
            throw new AccessDeniedException("No permission to createTask");
        }
        System.out.println("Permission granted!");

        Task task = taskMapper.toEntity(taskDTO);

        String branchCode = staffValidation.fetchBranchCodeByRole(role, email);
        System.out.println("Fetched branchCode: " + branchCode);

        task.setRole(role);
        task.setCreatedByEmail(email);
        task.setBranchCode(branchCode);

        if (!role.contains("TEAM_LEADER")) {
            throw new RuntimeException("You must be a team leader to assign tasks to members");
        }

        // Find the team leader
        TeamLeader teamLeader = teamLeaderRepository.findById(leaderId)
                .orElseThrow(() -> new RuntimeException("Team leader not found"));

        // Verify the authenticated user is the same leader who's assigning the task
        if (!teamLeader.getEmail().equals(email)) {
            throw new RuntimeException("You can only assign tasks on your own behalf");
        }

        // Find the team member
        TeamMember teamMember = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Team member not found"));

        // Set the assigner and assignee
        task.setAssignedByLeader(teamLeader);
        task.setAssignedToTeamMember(teamMember);

        // Handle file attachment if provided
        if (file != null && !file.isEmpty()) {
            try {
                task.setImageUrl(s3Service.uploadImage(file));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload file attachment", e);
            }
        }

        // Save the task
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);

    }

    @Override
    public List<TaskDTO> getTasksAssignedToLeaderId(Long id, String role, String email) {
        if (!staffValidation.hasPermission(role, email, "GET")) {
            throw new AccessDeniedException("You do not have permission to view task");
        }
        List<Task> tasks = taskRepository.findAllByAssignedToTeamLeader_Id(id);
        return taskMapper.toDtoList(tasks);
    }

}
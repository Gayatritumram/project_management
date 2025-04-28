package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.TeamLeader;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Mapper.TaskMapper;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.TaskService;
import com.backend.project_management.Util.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TeamMemberRepository repository;



    @Override
    public TaskDTO createTask(TaskDTO taskDTO, String token, Long id,MultipartFile file) throws IOException  {
        Task task = taskMapper.toEntity(taskDTO);

        String username = jwtHelper.getUsernameFromToken(token);
        String role = jwtHelper.getRoleFromToken(token);

        if (role != null && role.contains("ADMIN")) {
            ProjectAdmin currentAdmin = projectAdminRepo.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Logged-in admin not found"));
            task.setAssignedByAdmin(currentAdmin);
        } else if (role != null && role.contains("TEAM_LEADER")) {
            TeamLeader currentLeader = teamLeaderRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Logged-in team leader not found"));
            task.setAssignedByLeader(currentLeader);
        }

        TeamMember assignedTo = teamMemberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team member not found"));


        //Image
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
    public TaskDTO createTaskForLeader(TaskDTO taskDTO, String token, Long id, MultipartFile file) throws IOException {
        Task task = taskMapper.toEntity(taskDTO);

        String username = jwtHelper.getUsernameFromToken(token);
        String role = jwtHelper.getRoleFromToken(token);

        if (role != null && role.contains("ADMIN")) {
            ProjectAdmin currentAdmin = projectAdminRepo.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Logged-in admin not found"));
            task.setAssignedByAdmin(currentAdmin);
        } else if (role != null && role.contains("TEAM_LEADER")) {
            TeamLeader currentLeader = teamLeaderRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Logged-in team leader not found"));
            task.setAssignedByLeader(currentLeader);
        }

        TeamLeader assignedTo = teamLeaderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team Leader not found"));


        //Image
        task.setAssignedToTeamLeader(assignedTo);
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
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setDescription(taskDTO.getDescription());
        task.setProjectName(taskDTO.getProjectName());
        task.setDays(taskDTO.getDays());
        task.setHour(taskDTO.getHour());
        task.setStatus(taskDTO.getStatus());
        task.setStatusBar(taskDTO.getStatusBar());
        task.setStartDate(taskDTO.getStartDate());
        task.setEndDate(taskDTO.getEndDate());
        task.setStartTime(taskDTO.getStartTime());
        task.setEndTime(taskDTO.getEndTime());
        task.setSubject(taskDTO.getSubject());
        task.setPriority(taskDTO.getPriority());

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }

    @Override
    public TaskDTO uploadTaskImage(Long taskId, MultipartFile file) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        try {
            String imageUrl = s3Service.uploadImage(file);
            task.setImageUrl(imageUrl);
            return taskMapper.toDto(taskRepository.save(task));
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public TaskDTO deleteTaskImage(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (task.getImageUrl() != null) {
            s3Service.deleteImage(task.getImageUrl());
            task.setImageUrl(null);
            taskRepository.save(task);
        }
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminEmail(String email) {
        List<Task> tasks = taskRepository.findAllByAssignedByAdminEmail(email);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderEmail(String email) {
        List<Task> tasks = taskRepository.findAllByAssignedByLeaderEmail(email);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedToMemberEmail(String email) {
        List<Task> tasks = taskRepository.findAllByAssignedToTeamMemberEmail(email);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTodaysLeaderTasksByEmail(String email) {
        List<Task> tasks = taskRepository.findTodaysLeaderTasksByMemberEmail(email);
        return tasks.stream().map(taskMapper::toDto).collect(Collectors.toList());
    }
    @Override
    public List<TaskDTO> getTasksAssignedToMemberById(Long id) {
        List<Task> tasks = taskRepository.findAllByAssignedToTeamMember_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByLeaderId(Long id) {
        List<Task> tasks = taskRepository.findAllByAssignedByLeader_Id(id);
        return taskMapper.toDtoList(tasks);
    }

    @Override
    public List<TaskDTO> getTasksAssignedByAdminId(Long id) {
        List<Task> tasks = taskRepository.findAllByAssignedByAdmin_Id(id);
        return taskMapper.toDtoList(tasks);
    }


}
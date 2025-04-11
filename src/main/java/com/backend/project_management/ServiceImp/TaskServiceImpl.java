package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TaskReportDTO;
import com.backend.project_management.Entity.ProjectAdmin;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.TeamMember;
import com.backend.project_management.Mapper.TaskMapper;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Repository.TeamMemberRepository;
import com.backend.project_management.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ProjectAdminRepo projectAdminRepo;

    @Autowired
    private  TeamMemberRepository teamMemberRepository;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setProjectName(taskDTO.getProjectName());
        existingTask.setDays(taskDTO.getDays());
        existingTask.setHour(taskDTO.getHour());
        existingTask.setStatus(taskDTO.getStatus());
        existingTask.setStatusBar(taskDTO.getStatusBar());
        existingTask.setStartDate(taskDTO.getStartDate());
        existingTask.setEndDate(taskDTO.getEndDate());
        existingTask.setStartTime(taskDTO.getStartTime());
        existingTask.setEndTime(taskDTO.getEndTime());
        existingTask.setImageUrl(taskDTO.getImageUrl());
        existingTask.setDurationInMinutes(taskDTO.getDurationInMinutes());
        existingTask.setSubject(taskDTO.getSubject());
        existingTask.setPriority(taskDTO.getPriority());
        existingTask.setAssignedBy(taskDTO.getAssignedBy());

        Task updatedTask = taskRepository.save(existingTask);
        return taskMapper.toDto(updatedTask);
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        return taskMapper.toDto(task);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(taskMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        taskRepository.delete(task);
    }

    @Override
    public TaskDTO uploadTaskImage(Long taskId, MultipartFile file) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
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
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        if (task.getImageUrl() != null && !task.getImageUrl().isEmpty()) {
            s3Service.deleteImage(task.getImageUrl());
            task.setImageUrl(null);
            taskRepository.save(task);
        }

        return taskMapper.toDto(task);
    } @Override
    public TaskReportDTO generateReportForAdmin(Long adminId) {
        // Using the injected instance, not a static call
        ProjectAdmin admin = projectAdminRepo.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Map<String, Long> taskByStatus = new HashMap<>();
        for (Object[] result : taskRepository.countByStatusForAdmin(admin)) {
            taskByStatus.put(result[0].toString(), (Long) result[1]);
        }

        LocalDate today = LocalDate.now();
        Map<String, Long> tasksByDateRange = new HashMap<>();
        tasksByDateRange.put("Today", taskRepository.countByDateRangeForAdmin(admin, today, today));
        tasksByDateRange.put("Last 7 Days", taskRepository.countByDateRangeForAdmin(admin, today.minusDays(6), today));
        tasksByDateRange.put("Last 30 Days", taskRepository.countByDateRangeForAdmin(admin, today.minusDays(29), today));
        tasksByDateRange.put("Last 365 Days", taskRepository.countByDateRangeForAdmin(admin, today.minusDays(364), today));

        return new TaskReportDTO(tasksByDateRange, taskByStatus);
    }

    @Override
    public TaskReportDTO generateReportForMember(Long memberId) {
        // Using the injected instance, not a static call
        TeamMember member = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Map<String, Long> taskByStatus = new HashMap<>();
        for (Object[] result : taskRepository.countByStatusForMember(member)) {
            taskByStatus.put(result[0].toString(), (Long) result[1]);
        }

        LocalDate today = LocalDate.now();
        Map<String, Long> tasksByDateRange = new HashMap<>();
        tasksByDateRange.put("Today", taskRepository.countByDateRangeForMember(member, today, today));
        tasksByDateRange.put("Last 7 Days", taskRepository.countByDateRangeForMember(member, today.minusDays(6), today));
        tasksByDateRange.put("Last 30 Days", taskRepository.countByDateRangeForMember(member, today.minusDays(29), today));
        tasksByDateRange.put("Last 365 Days", taskRepository.countByDateRangeForMember(member, today.minusDays(364), today));

        return new TaskReportDTO(tasksByDateRange, taskByStatus);
    }


}

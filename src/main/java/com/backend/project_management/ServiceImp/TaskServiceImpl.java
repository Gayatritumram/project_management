package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Mapper.TaskMapper;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

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
}

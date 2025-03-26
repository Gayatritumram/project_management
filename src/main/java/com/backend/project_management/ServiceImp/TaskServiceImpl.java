package com.backend.project_management.ServiceImp;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Mapper.TaskMapper;
import com.backend.project_management.Repository.ProjectAdminRepo;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private ProjectAdminRepo adminRepo;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = taskMapper.toEntity(taskDTO);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDTO(savedTask);
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(taskMapper::toDTO).orElse(null);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(taskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> existingTask = taskRepository.findById(id);
        if (existingTask.isPresent()) {
            Task task = taskMapper.toEntity(taskDTO);
            task.setId(id);
            Task updatedTask = taskRepository.save(task);
            return taskMapper.toDTO(updatedTask);
        }
        return null;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}

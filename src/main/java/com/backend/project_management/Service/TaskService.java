package com.backend.project_management.Service;

import com.backend.project_management.DTO.TaskDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskService {
    public TaskDTO createTask(TaskDTO taskDTO , String token);

    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);

    TaskDTO getTaskById(Long taskId);

    List<TaskDTO> getAllTasks();

    void deleteTask(Long taskId);

    TaskDTO uploadTaskImage(Long taskId, MultipartFile file);

    TaskDTO deleteTaskImage(Long taskId);
}

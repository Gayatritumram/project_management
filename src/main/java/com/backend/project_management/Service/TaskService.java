package com.backend.project_management.Service;

import com.backend.project_management.DTO.TaskDTO;
import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO taskDTO);
    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);
    TaskDTO getTaskById(Long taskId);
    List<TaskDTO> getAllTasks();
    void deleteTask(Long taskId);
}

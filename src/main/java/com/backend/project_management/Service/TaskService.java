package com.backend.project_management.Service;

import com.backend.project_management.DTO.TaskDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TaskService {

    public TaskDTO createTask(TaskDTO taskDTO, String token, Long id,MultipartFile file)throws IOException  ;

    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);

    TaskDTO getTaskById(Long taskId);

    List<TaskDTO> getAllTasks();

    void deleteTask(Long taskId);

    TaskDTO uploadTaskImage(Long taskId, MultipartFile file);

    TaskDTO deleteTaskImage(Long taskId);

    List<TaskDTO> getTasksAssignedByAdminEmail(String email);

    List<TaskDTO> getTasksAssignedByLeaderEmail(String email);

    List<TaskDTO> getTasksAssignedToMemberEmail(String email);

    public List<TaskDTO> getTodaysLeaderTasksByEmail(String email);
    public TaskDTO createTaskForLeader(TaskDTO taskDTO, String token, Long id,MultipartFile file)throws IOException  ;



}

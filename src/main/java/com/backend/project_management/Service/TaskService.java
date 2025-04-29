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

    List<TaskDTO> getTasksAssignedToMemberById(Long id);

    List<TaskDTO> getTasksAssignedByLeaderId(Long id);

    List<TaskDTO> getTasksAssignedByAdminId(Long id);


    List<TaskDTO> getTasksAssignedByAdminEmail(String email);

    List<TaskDTO> getTasksAssignedByLeaderEmail(String email);

    List<TaskDTO> getTasksAssignedToMemberEmail(String email);

    public List<TaskDTO> getTodaysLeaderTasksByEmail(String email);
    public TaskDTO createTaskForLeader(TaskDTO taskDTO, String token, Long id,MultipartFile file)throws IOException  ;

    // New methods for admin-assigned tasks to a specific member
    List<TaskDTO> getTasksAssignedByAdminToMember(Long adminId, Long memberId);
    
    List<TaskDTO> getTodaysTasksAssignedByAdminToMember(Long adminId, Long memberId);
    
    // New methods for leader-assigned tasks to a specific member
    List<TaskDTO> getTasksAssignedByLeaderToMember(Long leaderId, Long memberId);
    
    List<TaskDTO> getTodaysTasksAssignedByLeaderToMember(Long leaderId, Long memberId);

    // new method for Leader assigns task to member
    TaskDTO assignTaskFromLeaderToMember(TaskDTO taskDTO, String token, Long leaderId, Long memberId, MultipartFile file) throws IOException;
}

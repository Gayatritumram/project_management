package com.backend.project_management.Service;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TaskDashboardDTO;
import com.backend.project_management.DTO.TaskSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TaskService {

    public TaskDTO createTask(TaskDTO taskDTO, String token, Long id,MultipartFile file,String role, String email)throws IOException  ;

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO, String role, String email, MultipartFile file) throws IOException;

    TaskDTO getTaskById(Long taskId,String role,String email);

    List<TaskDTO> getAllTasks(String role,String  email,String  branchCode);

    void deleteTask(Long taskId,String role,String  email);

    List<TaskDTO> getTasksAssignedToMemberById(Long id,String role,String email);

    List<TaskDTO> getTasksAssignedByLeaderId(Long id,String role,String  email);

    List<TaskDTO> getTasksAssignedByAdminId(Long id,String  role,String  email);


    List<TaskDTO> getTasksAssignedByAdminEmail(String emailFind,String role,String email);

    List<TaskDTO> getTasksAssignedByLeaderEmail(String emailFind,String role,String email);

    List<TaskDTO> getTasksAssignedToMemberEmail(String emailFind,String role,String email);

    public List<TaskDTO> getTodaysLeaderTasksByEmail(String emailFind,String email,String  role);
    public TaskDTO createTaskForLeader(TaskDTO taskDTO, String token, Long id,MultipartFile file,String role,String  email)throws IOException  ;

    // New methods for admin-assigned tasks to a specific member
    List<TaskDTO> getTasksAssignedByAdminToMember(Long adminId, Long memberId,String  role,String  email);
    
    List<TaskDTO> getTodaysTasksAssignedByAdminToMember(Long adminId, Long memberId,String  role,String  email);
    
    // New methods for leader-assigned tasks to a specific member
    List<TaskDTO> getTasksAssignedByLeaderToMember(Long leaderId, Long memberId,String role,String  email);
    
    List<TaskDTO> getTodaysTasksAssignedByLeaderToMember(Long leaderId, Long memberId,String  role, String email);

    // new method for Leader assigns task to member
    TaskDTO assignTaskFromLeaderToMember(TaskDTO taskDTO, String token, Long leaderId, Long memberId, MultipartFile file,String role,String  email) throws IOException;

    // Get all tasks assigned to a leader by their ID
    List<TaskDTO> getTasksAssignedToLeaderId(Long id,String role,String email);

    List<TaskDTO> getAllTasks(String role, String email);


    Page<TaskDTO> getAllTasksWithFilter(TaskDTO filter, int page, int size, String sortBy, String sortType, String role, String email,String branchCode);

    List<TaskSummaryDTO> getAllTaskSummaries(String role,String email,String branchCode);

    TaskDashboardDTO getTaskDashboard(String role,String email,String branchCode);

}

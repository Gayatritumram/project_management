package com.backend.project_management.Controller;

import com.backend.project_management.DTO.*;

import com.backend.project_management.Entity.Project;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Repository.TaskRepository;
import com.backend.project_management.Service.ProjectService;
import com.backend.project_management.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "https://pjsofttech.in")
public class DashboardController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectService projectService;


    // dashboard by status
    @GetMapping("/task/monthlyCounts")
    public ResponseEntity<MonthlyTaskCountDTO> getMonthlyTaskCounts(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam String role,
            @RequestParam String email
    ) {
        return ResponseEntity.ok(taskService.getMonthlyTaskCounts(month, year, role, email));
    }

    @GetMapping("/task/countsByTimeFrame")
    public ResponseEntity<TaskCountDTO> getTaskCountsByTimeFrame(
            @RequestParam String branchCode,
            @RequestParam String role,
            @RequestParam String email
    ) {
        return ResponseEntity.ok(taskService.getTaskCountsByTimeFrame(branchCode, role, email));
    }



    @GetMapping("/project/statusCounts")
    public ResponseEntity<Map<String, Object>> getProjectStatusCounts(
            @RequestParam String branchCode,
            @RequestParam String role,
            @RequestParam String email
    ) {
        ProjectStatusCountDTO dto = projectService.getProjectStatusCounts(branchCode, role, email);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("Completed", dto.getCompleted());
        response.put("In Progress", dto.getIn_Progress());
        response.put("On Hold", dto.getOn_Hold());
        response.put("Delay", dto.getDelay());
        response.put("Today's Project", dto.getTodays_Project());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/project/getAllProjects")
    public ResponseEntity<List<Map<String, Object>>> getAllProjectsForDashboard(
            @RequestParam String branchCode,
            @RequestParam String role,
            @RequestParam String email
    ) {
        List<ProjectDTO> projects = projectService.getAllProjectsForDashboard(role, email, branchCode);

        List<Map<String, Object>> filteredResponse = projects.stream().map(p -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("projectName", p.getProjectName());
            map.put("status", p.getStatus());
            map.put("branchName", p.getBranchName());
            map.put("departmentName", p.getDepartmentName());
            map.put("startDate", p.getStartDate());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(filteredResponse);
    }

    @GetMapping("/task/assigned-by-leader")
    public ResponseEntity<?> getTasksAssignedByLeaderToMembers(
            @RequestParam String role,
            @RequestParam String email
    ) {
        List<Map<String, Object>> tasks = taskService.getTasksAssignedByLeaderToMembers(role, email);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/task/assigned-by-admin")
    public ResponseEntity<?> getTasksAssignedByAdmin(
            @RequestParam String role,
            @RequestParam String email
    ) {
        List<Map<String, Object>> tasks = taskService.getTasksAssignedByAdmin(role, email);
        return ResponseEntity.ok(tasks);
    }

}

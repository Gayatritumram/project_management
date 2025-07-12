package com.backend.project_management.Controller;

import com.backend.project_management.DTO.MonthlyTaskCountDTO;
import com.backend.project_management.DTO.ProjectDTO;
import com.backend.project_management.DTO.ProjectStatusCountDTO;
import com.backend.project_management.DTO.TaskCountDTO;

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
    public ResponseEntity<ProjectStatusCountDTO> getProjectStatusCounts(
            @RequestParam String branchCode,
            @RequestParam String role,
            @RequestParam String email
    ) {
        return ResponseEntity.ok(projectService.getProjectStatusCounts(branchCode, role, email));
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





}

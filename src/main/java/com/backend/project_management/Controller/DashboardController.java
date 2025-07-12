package com.backend.project_management.Controller;

import com.backend.project_management.DTO.MonthlyTaskCountDTO;
import com.backend.project_management.DTO.ProjectStatusCountDTO;
import com.backend.project_management.DTO.TaskCountDTO;

import com.backend.project_management.Service.ProjectService;
import com.backend.project_management.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}

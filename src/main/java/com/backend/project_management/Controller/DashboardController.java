package com.backend.project_management.Controller;

import com.backend.project_management.DTO.*;

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




    @GetMapping("/task/all/leader")
    public ResponseEntity<?> getAllTasksForLeader(
            @RequestParam String role,
            @RequestParam String email
    ) {
        List<TaskDTO> tasks = taskService.getAllTasks(role, email);

        List<Map<String, Object>> filterResponse = tasks.stream().map(t -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("subject", t.getSubject());
            map.put("status", t.getStatus());
            map.put("description", t.getDescription());
            map.put("priority", t.getPriority());
            map.put("startDate", t.getStartDate());
            map.put("endDate", t.getEndDate());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(filterResponse);


    }




    @GetMapping("/task/all/member")
    public ResponseEntity<?> getAllTasksForMember(
            @RequestParam String role,
            @RequestParam String email
    ) {
        List<TaskDTO> tasks = taskService.getAllTasks(role, email);

        List<Map<String, Object>> filterResponse = tasks.stream().map(t -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("subject", t.getSubject());
            map.put("status", t.getStatus());
            map.put("description", t.getDescription());
            map.put("priority", t.getPriority());
            map.put("startDate", t.getStartDate());
            map.put("endDate", t.getEndDate());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(filterResponse);

    }



}

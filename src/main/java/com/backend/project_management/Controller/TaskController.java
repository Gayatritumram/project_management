package com.backend.project_management.Controller;

import java.io.IOException;
import java.util.List;

import org.hibernate.boot.jaxb.Origin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:3000")

public class TaskController {

    @Autowired
    private TaskService taskService;

    //Admin Assign A task To Team member Use this api
    @PostMapping("/create/{id}")
    public ResponseEntity<TaskDTO> createTask(
            @RequestParam("task") String taskJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.createTask(taskDTO, token.replace("Bearer ", ""), id, file);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }


    //
    @PostMapping("/create/Leader/{id}")

    public ResponseEntity<TaskDTO> createTask2(
            @RequestParam("task") String taskJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.createTaskForLeader(taskDTO, token.replace("Bearer ", ""), id, file);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(id, taskDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task deleted successfully");
    }


    @GetMapping("/tasks/admin/email/{email}")
    public ResponseEntity<List<TaskDTO>> getTasksByAdminEmail(@PathVariable String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedByAdminEmail(email));
    }

    @GetMapping("/tasks/leader/email/{email}")
    public ResponseEntity<List<TaskDTO>> getTasksByLeaderEmail(@PathVariable String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedByLeaderEmail(email));
    }

    @GetMapping("/tasks/member/email/{email}")
    public ResponseEntity<List<TaskDTO>> getTasksByMemberEmail(@PathVariable String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedToMemberEmail(email));
    }

    //todays task asssigned to leader
    @GetMapping("/leader/today/{email}")
    public ResponseEntity<List<TaskDTO>> getTodaysLeaderTasks(@PathVariable String email) {
        return ResponseEntity.ok(taskService.getTodaysLeaderTasksByEmail(email));
    }
    @GetMapping("/assigned-to/member/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedToMember(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksAssignedToMemberById(id));
    }

    @GetMapping("/assigned-by/leader/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByLeader(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksAssignedByLeaderId(id));
    }

    @GetMapping("/assigned-by/admin/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksAssignedByAdminId(id));
    }

    // New endpoints for admin-assigned tasks to a specific member
    @GetMapping("/admin/{adminId}/member/{memberId}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByAdminToMember(
            @PathVariable Long adminId, 
            @PathVariable Long memberId) {
        return ResponseEntity.ok(taskService.getTasksAssignedByAdminToMember(adminId, memberId));
    }

    @GetMapping("/admin/{adminId}/member/{memberId}/today")
    public ResponseEntity<List<TaskDTO>> getTodaysTasksAssignedByAdminToMember(
            @PathVariable Long adminId, 
            @PathVariable Long memberId) {
        return ResponseEntity.ok(taskService.getTodaysTasksAssignedByAdminToMember(adminId, memberId));
    }

    // New endpoints for leader-assigned tasks to a specific member
    @GetMapping("/leader/{leaderId}/member/{memberId}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByLeaderToMember(
            @PathVariable Long leaderId, 
            @PathVariable Long memberId) {
        return ResponseEntity.ok(taskService.getTasksAssignedByLeaderToMember(leaderId, memberId));
    }

    @GetMapping("/leader/{leaderId}/member/{memberId}/today")
    public ResponseEntity<List<TaskDTO>> getTodaysTasksAssignedByLeaderToMember(
            @PathVariable Long leaderId, 
            @PathVariable Long memberId) {
        return ResponseEntity.ok(taskService.getTodaysTasksAssignedByLeaderToMember(leaderId, memberId));
    }

    // new method for Leader assigns task to member
    @PostMapping("/leader/{leaderId}/assign-to-member/{memberId}")
    public ResponseEntity<TaskDTO> assignTaskFromLeaderToMember(
            @RequestParam("task") String taskJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable Long leaderId,
            @PathVariable Long memberId,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.assignTaskFromLeaderToMember(taskDTO, token.replace("Bearer ", ""), leaderId, memberId, file);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Get all tasks assigned to a leader
    @GetMapping("/assigned-to/leader/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedToLeader(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksAssignedToLeaderId(id));
    }

}

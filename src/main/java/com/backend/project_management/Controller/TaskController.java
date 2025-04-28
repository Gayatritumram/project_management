package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
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
        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.createTask(taskDTO,    token.replace("Bearer ", ""), id, file);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    //
    @PostMapping("/create/Leader/{id}")
    public ResponseEntity<TaskDTO> createTaskForLeader(
            @RequestParam("task") String taskJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.createTaskForLeader(taskDTO,token.replace("Bearer ", ""), id, file);
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

    @PostMapping("/{taskId}/upload-image")
    public ResponseEntity<TaskDTO> uploadImage(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(taskService.uploadTaskImage(taskId, file));
    }

    @DeleteMapping("/{taskId}/delete-image")
    public ResponseEntity<TaskDTO> deleteImage(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.deleteTaskImage(taskId));
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



}

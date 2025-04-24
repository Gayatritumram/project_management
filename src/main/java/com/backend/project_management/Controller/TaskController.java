package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TaskReportDTO;
import com.backend.project_management.Service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @PostMapping("/create/{id}")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO,
                                              HttpServletRequest request,
                                              @PathVariable Long id) {
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
        }

        return ResponseEntity.ok(taskService.createTask(taskDTO, token, id));
    }

    @PostMapping("/create/Leader/{id}")
    public ResponseEntity<TaskDTO> createTaskForLeader (@RequestBody TaskDTO taskDTO,
                                              HttpServletRequest request,
                                              @PathVariable Long id) {
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
        }

        return ResponseEntity.ok(taskService.createTaskForLeader(taskDTO, token, id));
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
}

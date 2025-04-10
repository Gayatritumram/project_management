package com.backend.project_management.Controller;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDTO));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task Deleted Successfully");
    }

    @PostMapping("/{taskId}/upload-image")
    public ResponseEntity<TaskDTO> uploadTaskImage(@PathVariable Long taskId,
                                                   @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(taskService.uploadTaskImage(taskId, file));
    }

    @DeleteMapping("/{taskId}/delete-image")
    public ResponseEntity<TaskDTO> deleteTaskImage(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.deleteTaskImage(taskId));
    }
}

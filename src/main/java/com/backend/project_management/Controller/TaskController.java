package com.backend.project_management.Controller;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "https://pjsofttech.in")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //Admin Assign A task To Team member Use this api
    @PostMapping("/createTaskForMember/{id}")
    public ResponseEntity<TaskDTO> createTask(
            @RequestParam("task") String taskJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.createTask(taskDTO, token.replace("Bearer ", ""), id, file, role, email);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }


    //task created by leader
    @PostMapping("/createTaskForLeader/{id}")
    public ResponseEntity<TaskDTO> createTask2(
            @RequestParam("task") String taskJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam String email,
            @RequestHeader("Authorization") String token
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.createTaskForLeader(taskDTO, token.replace("Bearer ", ""), id, file, role, email);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }



    @PutMapping("/updateTask/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long taskId,
            @RequestParam("task") String taskJson,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam String role,
            @RequestParam String email
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);

        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO, role, email, file);

        return ResponseEntity.ok(updatedTask);
    }



    @GetMapping("/getTaskById/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id,
                                           @RequestParam String role,
                                           @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTaskById(id, role, email));
    }

///

@GetMapping("/getAllTasks")
public ResponseEntity<Page<TaskDTO>> getAllTasks(
        @ModelAttribute TaskDTO filter,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "startDate") String sortBy,
        @RequestParam(defaultValue = "asc") String sortType,
        @RequestParam String role,
        @RequestParam String email,
        @RequestParam String branchCode
) {
    return ResponseEntity.ok(taskService.getAllTasksWithFilter(filter, page, size, sortBy, sortType, role, email,branchCode));
}



    @DeleteMapping("/deleteTaskById/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id,
                                             @RequestParam String role,
                                             @RequestParam String email) {
        taskService.deleteTask(id, role, email);
        return ResponseEntity.ok("Task deleted successfully");
    }


    @GetMapping("/tasks/admin/email/{email}")
    public ResponseEntity<List<TaskDTO>> getTasksByAdminEmail(@PathVariable String emailFind,
                                                              @PathVariable String email,
                                                              @RequestParam String role
    ) {
        return ResponseEntity.ok(taskService.getTasksAssignedByAdminEmail(emailFind, email, role));
    }






    @GetMapping("/tasks/leader/email/{email}")
    public ResponseEntity<List<TaskDTO>> getTasksByLeaderEmail(@PathVariable String emailFind,
                                                                @PathVariable String email,
                                                               @RequestParam String role) {
        return ResponseEntity.ok(taskService.getTasksAssignedByLeaderEmail(emailFind, email, role));
    }


    @GetMapping("/tasks/member/email/{email}")
    public ResponseEntity<List<TaskDTO>> getTasksByMemberEmail(@PathVariable String emailFind,
                                                               @PathVariable String email,
                                                               @RequestParam String role) {
        return ResponseEntity.ok(taskService.getTasksAssignedToMemberEmail(emailFind, email,role));
    }

    @GetMapping("/leader/today/{email}")
    public ResponseEntity<List<TaskDTO>> getTodaysLeaderTasks(@PathVariable String emailFind,
                                                              @PathVariable String email,
                                                              @RequestParam String role) {
        return ResponseEntity.ok(taskService.getTodaysLeaderTasksByEmail(emailFind, email, role));
    }


    @GetMapping("/assigned-to/member/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedToMember(@PathVariable Long id,
                                                                  @RequestParam String role,
                                                                  @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedToMemberById(id, role, email));
    }

    @GetMapping("/assigned-by/leader/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByLeader(@PathVariable Long id,
                                                                  @RequestParam String role,
                                                                  @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedByLeaderId(id, role, email));
    }

    @GetMapping("/assigned-by/admin/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByAdmin(@PathVariable Long id,
                                                                 @RequestParam String role,
                                                                 @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedByAdminId(id, role, email));
    }



    // New endpoints for admin-assigned tasks to a specific member
    @GetMapping("/admin/{adminId}/member/{memberId}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByAdminToMember(@PathVariable Long adminId,
                                                                         @PathVariable Long memberId,
                                                                         @RequestParam String role,
                                                                         @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedByAdminToMember(adminId, memberId, role, email));
    }



    @GetMapping("/admin/{adminId}/member/{memberId}/today")
    public ResponseEntity<List<TaskDTO>> getTodaysTasksAssignedByAdminToMember(@PathVariable Long adminId,
                                                                               @PathVariable Long memberId,
                                                                               @RequestParam String role,
                                                                               @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTodaysTasksAssignedByAdminToMember(adminId, memberId, role, email));
    }

    // New endpoints for leader-assigned tasks to a specific member
    @GetMapping("/leader/{leaderId}/member/{memberId}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedByLeaderToMember(@PathVariable Long leaderId,
                                                                          @PathVariable Long memberId,
                                                                          @RequestParam String role,
                                                                          @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedByLeaderToMember(leaderId, memberId, role, email));
    }

    @GetMapping("/leader/{leaderId}/member/{memberId}/today")
    public ResponseEntity<List<TaskDTO>> getTodaysTasksAssignedByLeaderToMember(@PathVariable Long leaderId,
                                                                                @PathVariable Long memberId,
                                                                                @RequestParam String role,
                                                                                @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTodaysTasksAssignedByLeaderToMember(leaderId, memberId, role, email));
    }

    // new method for Leader assigns task to member
    @PostMapping("/leader/{leaderId}/assign-to-member/{memberId}")
    public ResponseEntity<TaskDTO> assignTaskFromLeaderToMember(@RequestParam("task") String taskJson,
                                                                @RequestParam(value = "file", required = false) MultipartFile file,
                                                                @PathVariable Long leaderId,
                                                                @PathVariable Long memberId,
                                                                @RequestHeader("Authorization") String token,
                                                                @RequestParam String role,
                                                                @RequestParam String email
    ) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TaskDTO taskDTO = objectMapper.readValue(taskJson, TaskDTO.class);
        TaskDTO createdTask = taskService.assignTaskFromLeaderToMember(taskDTO, token.replace("Bearer ", ""), leaderId, memberId, file, role, email);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Get all tasks assigned to a leader
    @GetMapping("/assigned-to/leader/{id}")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedToLeader(@PathVariable Long id,
                                                                  @RequestParam String role,
                                                                  @RequestParam String email) {
        return ResponseEntity.ok(taskService.getTasksAssignedToLeaderId(id, role, email));
    }



}

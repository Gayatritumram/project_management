package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.DTO.TaskSummaryDTO;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Repository.BranchAdminRepository;
import com.backend.project_management.Repository.TeamLeaderRepository;
import com.backend.project_management.Repository.TeamMemberRepository;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    @Autowired
    private BranchAdminRepository adminRepo;

    @Autowired
    TeamLeaderRepository teamLeaderRepository;

    @Autowired
    TeamMemberRepository teamMemberRepository;

    public TaskDTO toDto(@NotNull Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setSubject(task.getSubject());
        dto.setDescription(task.getDescription());
        dto.setProjectName(task.getProjectName());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setStatusBar(task.getStatusBar());
        dto.setDays(task.getDays());
        dto.setImageUrl(task.getImageUrl());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setCreatedByEmail(task.getCreatedByEmail());
        dto.setBranchCode(task.getBranchCode());
        dto.setRole(task.getRole());
        dto.setAssignedToName(task.getAssignedToName());



        dto.setAssignedByAdminId(task.getAssignedByAdmin() != null ? task.getAssignedByAdmin().getId() : null);
        dto.setAssignedByLeaderId(task.getAssignedByLeader() != null ? task.getAssignedByLeader().getId() : null);
        dto.setAssignedToTeamMember(task.getAssignedToTeamMember() != null ? task.getAssignedToTeamMember().getId() : null);
        dto.setAssignedToTeamLeader(task.getAssignedToTeamLeader() != null ? task.getAssignedToTeamLeader().getId() : null);


        return dto;
    }

    public Task toEntity(@NotNull TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setSubject(dto.getSubject());
        task.setDescription(dto.getDescription());
        task.setProjectName(dto.getProjectName());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setStatusBar(dto.getStatusBar());
        task.setDays(dto.getDays());
        task.setImageUrl(dto.getImageUrl());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setCreatedByEmail(dto.getCreatedByEmail());
        task.setBranchCode(dto.getBranchCode());
        task.setRole(dto.getRole());
        task.setAssignedToName(dto.getAssignedToName());
        if (dto.getAssignedByAdminId() != null) {
            task.setAssignedByAdmin(adminRepo.findById(dto.getAssignedByAdminId()).orElse(null));
        }

        if (dto.getAssignedByLeaderId() != null) {
            task.setAssignedByLeader(teamLeaderRepository.findById(dto.getAssignedByLeaderId()).orElse(null));
        }

        if (dto.getAssignedToTeamMember() != null) {
            task.setAssignedToTeamMember(teamMemberRepository.findById(dto.getAssignedToTeamMember()).orElse(null));
        }

        if (dto.getAssignedToTeamLeader() != null) {
            task.setAssignedToTeamLeader(teamLeaderRepository.findById(dto.getAssignedToTeamLeader()).orElse(null));
        }




        return task;
    }

    public List<TaskDTO> toDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public static TaskSummaryDTO mapToTaskSummary(Task task) {
        TaskSummaryDTO dto = new TaskSummaryDTO();
        dto.setSubject(task.getSubject());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());

        if (task.getAssignedToTeamLeader() != null) {
            dto.setAssignedToNameofLeader(task.getAssignedToTeamLeader().getName());
        }

        if (task.getAssignedToTeamMember() != null) {
            dto.setAssignedToNameofMember(task.getAssignedToTeamMember().getName());
        }

        return dto;
    }
}

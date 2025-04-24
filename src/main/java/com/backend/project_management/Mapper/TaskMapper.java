package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setSubject(task.getSubject());
        dto.setDescription(task.getDescription());
        dto.setProjectName(task.getProjectName());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setStatusBar(task.getStatusBar());
        dto.setDays(task.getDays());
        dto.setHour(task.getHour());
        dto.setDurationInMinutes(task.getDurationInMinutes());
        dto.setImageUrl(task.getImageUrl());

        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());

        if (task.getAssignedByAdmin() != null) {
            dto.setAssignedByAdminId(task.getAssignedByAdmin().getId());
        }

        if (task.getAssignedByLeader() != null) {
            dto.setAssignedByLeaderId(task.getAssignedByLeader().getId());
        }

        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
        }

        return dto;
    }

    public Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setSubject(dto.getSubject());
        task.setDescription(dto.getDescription());
        task.setProjectName(dto.getProjectName());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setStatusBar(dto.getStatusBar());
        task.setDays(dto.getDays());
        task.setHour(dto.getHour());
        task.setDurationInMinutes(dto.getDurationInMinutes());
        task.setImageUrl(dto.getImageUrl());

        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setStartTime(dto.getStartTime());
        task.setEndTime(dto.getEndTime());

        return task;
    }

    public List<TaskDTO> toDtoList(List<Task> tasks) {
        return tasks.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}

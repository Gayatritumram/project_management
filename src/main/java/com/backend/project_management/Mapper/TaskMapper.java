package com.backend.project_management.Mapper;

import com.backend.project_management.DTO.TaskDTO;
import com.backend.project_management.Entity.Task;
import com.backend.project_management.Entity.TeamMember;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO toDto(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setProjectName(task.getProjectName());
        dto.setDays(task.getDays());
        dto.setHour(task.getHour());
        dto.setStatus(task.getStatus());
        dto.setStatusBar(task.getStatusBar());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setStartTime(task.getStartTime());
        dto.setEndTime(task.getEndTime());
        dto.setImageUrl(task.getImageUrl());
        dto.setDurationInMinutes(task.getDurationInMinutes());
        dto.setSubject(task.getSubject());
        dto.setPriority(task.getPriority());
        dto.setAssignedBy(task.getAssignedBy());
        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
        }
        return dto;
    }

    public Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setDescription(dto.getDescription());
        task.setProjectName(dto.getProjectName());
        task.setDays(dto.getDays());
        task.setHour(dto.getHour());
        task.setStatus(dto.getStatus());
        task.setStatusBar(dto.getStatusBar());
        task.setStartDate(dto.getStartDate());
        task.setEndDate(dto.getEndDate());
        task.setStartTime(dto.getStartTime());
        task.setEndTime(dto.getEndTime());
        task.setImageUrl(dto.getImageUrl());
        task.setDurationInMinutes(dto.getDurationInMinutes());
        task.setSubject(dto.getSubject());
        task.setPriority(dto.getPriority());
        task.setAssignedBy(dto.getAssignedBy());
        if (dto.getAssignedToId() != null) {
            TeamMember teamMember = new TeamMember();
            teamMember.setId(dto.getAssignedToId());
            task.setAssignedTo(teamMember);
        }
        return task;
    }
}

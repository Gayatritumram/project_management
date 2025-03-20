package com.backend.project_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Task_Table")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String description;
    private String projectName;
    private int days;
    private int hour;
    private String status;

    @Column(name = "statusBar", nullable = false)
    private double statusBar =0;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String imageUrl;
    private long durationInMinutes;
    private String subject;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private ProjectAdmin assignedBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private TeamMember assignedTo;

}
package com.backend.project_management.Entity;

import com.backend.project_management.TaskPriority;
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
    private double statusBar = 0;

    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String imageUrl;
    private long durationInMinutes;
    private String subject;

    @Enumerated(EnumType.STRING)
        private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private TeamMember assignedTo;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private ProjectAdmin assignedBy;

    @PrePersist
    protected void onCreate() {
        this.startDate = LocalDate.now(); // Auto-assign current date
        this.startTime = LocalDateTime.now(); // Auto-assign current time
        if (days > 0) {
            this.endDate = this.startDate.plusDays(days); // Set endDate based on duration
        }
        if (hour > 0) {
            this.endTime = this.startTime.plusHours(hour); // Set endTime based on hours
        }
    }
}

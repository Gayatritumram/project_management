package com.backend.project_management.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "PMTask")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String description;
    private String projectName;//
    private String priority;
    private String status;
    private String statusBar;
    private int days;

    private String imageUrl;

    private LocalDate startDate;
    private LocalDate endDate;








    @ManyToOne
    @JoinColumn(name = "assigned_by_admin_id")
    private ProjectAdmin assignedByAdmin;

    @ManyToOne
    @JoinColumn(name = "assigned_by_leader_id")
    private TeamLeader assignedByLeader;

    @ManyToOne
    @JoinColumn(name = "assigned_to_member_id")
    private TeamMember assignedToTeamMember;

    @ManyToOne
    @JoinColumn(name = "assigned_to_leader_id")
    private TeamLeader assignedToTeamLeader;




}

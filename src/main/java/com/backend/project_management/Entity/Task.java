package com.backend.project_management.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "PM_Task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String description;
    private String projectName;
    private String priority;
    private String status;
    private String statusBar;
    private int days;
    private String imageUrl;

    private LocalDate startDate;
    private LocalDate endDate;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;

    @ManyToOne
    @JoinColumn(name = "assigned_by_admin_id")
    private BranchAdmin assignedByAdmin;

    @ManyToOne
    @JoinColumn(name = "assigned_to_member_id")
    private TeamMember assignedToTeamMember;
    
    @ManyToOne
    @JoinColumn(name = "assigned_to_leader_id")
    @JsonIgnore
    private TeamLeader assignedToTeamLeader;

    @ManyToOne
    @JoinColumn(name = "assignedByLeader_id")
    @JsonBackReference("assigned-by")
    private TeamLeader assignedByLeader;

    private String assignedToName;




}

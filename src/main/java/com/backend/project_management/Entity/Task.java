package com.backend.project_management.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PM_Task")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    private String name;
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
    @JoinColumn(name = "assigned_by_leader_id")
    @JsonIgnore
    private TeamLeader assignedByLeader;

    @ManyToOne
    @JoinColumn(name = "assigned_to_leader_id")
    @JsonIgnore
    private TeamLeader assignedToTeamLeader;

    @ManyToOne
    @JoinColumn(name = "assigned_to_member_id")
    @JsonIgnore
    private TeamMember assignedToTeamMember;

    @ManyToOne
    @JoinColumn(name = "assigned_by_admin_id")
    @JsonIgnore
    private BranchAdmin assignedByAdmin;



    private String assignedToName;




}

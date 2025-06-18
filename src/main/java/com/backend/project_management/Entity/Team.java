package com.backend.project_management.Entity;

import jakarta.persistence.*;//.
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;//.
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PMTeam")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;
    private String branchName;
    private String department;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;


    @OneToMany(mappedBy = "teamId", cascade = CascadeType.ALL)
    private List<TeamMember> memberList = new ArrayList<>();

    @OneToOne(mappedBy = "teamId", cascade = CascadeType.ALL)
    private TeamLeader teamLeader;


}

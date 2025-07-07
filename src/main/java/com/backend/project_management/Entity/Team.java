package com.backend.project_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;//.
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;//.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PM_Team")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String teamName;
    private String branchName;
    private String departmentName;

    @Email
    private String createdByEmail;
    private String role;
    private String branchCode;


    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<TeamMember> memberList = new ArrayList<>();

    @OneToOne(mappedBy = "team")
    @JsonIgnore  // or @JsonBackReference if needed
    private TeamLeader teamLeader;


}

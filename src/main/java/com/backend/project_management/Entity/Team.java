package com.backend.project_management.Entity;

import jakarta.persistence.*;//.
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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


    @OneToMany(mappedBy = "teamId", cascade = CascadeType.ALL, fetch = FetchType.LAZY) //.
    private List<TeamMember> members;


    @OneToMany(mappedBy = "team1", cascade = CascadeType.ALL)
    private List<Project>  assignTeam;
}

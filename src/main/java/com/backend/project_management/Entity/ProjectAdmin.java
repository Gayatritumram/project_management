package com.backend.project_management.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "Project_admin")
public class ProjectAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String phone;

    @Column(nullable = false)
    private String password;
    @Transient
    private String cpassword;

    //@OneToMany(mappedBy = "assignedBy", cascade = CascadeType.ALL)
    //private List<Task> assignedTasks;

}

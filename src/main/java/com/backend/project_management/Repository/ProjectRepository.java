package com.backend.project_management.Repository;

import com.backend.project_management.Entity.Department;
import com.backend.project_management.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    Optional<Project> findByProjectName(String projectName);

    List<Project> findAllByBranchCode(String branchCode);
}


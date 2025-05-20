package com.backend.project_management.Repository;

import com.backend.project_management.Entity.ProjectAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectAdminRepo extends JpaRepository<ProjectAdmin, Long> {
    Optional<ProjectAdmin> findByEmail(String email);
    boolean existsByEmail(String email);


    List<ProjectAdmin> findAllByBranchCode(String branchCode);

}

package com.backend.project_management.Repository;

import com.backend.project_management.Entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
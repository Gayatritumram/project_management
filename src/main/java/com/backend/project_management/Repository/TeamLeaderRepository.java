package com.backend.project_management.Repository;

import com.backend.project_management.Entity.TeamLeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamLeaderRepository extends JpaRepository<TeamLeader, Long> {
    Optional<TeamLeader> findByEmail(String email);

}

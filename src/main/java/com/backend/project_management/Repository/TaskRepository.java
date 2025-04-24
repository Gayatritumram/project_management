package com.backend.project_management.Repository;

import com.backend.project_management.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByAssignedByAdmin_Id(Long adminId);

    List<Task> findAllByAssignedByLeader_Id(Long leaderId);

    List<Task> findAllByAssignedTo_Id(Long memberId);
}


package com.backend.project_management.Repository;

import com.backend.project_management.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByAssignedByAdmin_Id(Long adminId);

    List<Task> findAllByAssignedByLeader_Id(Long leaderId);

    List<Task> findAllByAssignedToTeamMember_Id(Long memberId);

    List<Task> findAllByAssignedToTeamLeader_Id(Long memberId);

    @Query("SELECT t FROM Task t WHERE t.assignedByLeader IS NOT NULL AND t.assignedToTeamMember.email = :email AND t.startDate = CURRENT_DATE")
    List<Task> findTodaysLeaderTasksByMemberEmail(@Param("email") String email);

    @Query("SELECT t FROM Task t WHERE t.assignedToTeamLeader.email = :email AND t.startDate = CURRENT_DATE")
    List<Task> findTodaysTasksAssignedToLeader(@Param("email") String email);


}


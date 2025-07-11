package com.backend.project_management.Repository;

import java.util.List;

import com.backend.project_management.Entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.project_management.Entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    List<Task> findAllByAssignedByAdmin_Id(Long adminId);

    List<Task> findAllByAssignedByLeader_Id(Long leaderId);

    List<Task> findAllByAssignedToTeamMember_Id(Long memberId);

    List<Task> findAllByAssignedToTeamLeader_Id(Long memberId);

    @Query("SELECT t FROM Task t WHERE t.assignedByLeader IS NOT NULL AND t.assignedToTeamMember.email = :email AND t.startDate = CURRENT_DATE")
    List<Task> findTodaysLeaderTasksByMemberEmail(@Param("email") String email);

    @Query("SELECT t FROM Task t WHERE t.assignedToTeamLeader.email = :email AND t.startDate = CURRENT_DATE")
    List<Task> findTodaysTasksAssignedToLeader(@Param("email") String email);
    
    // New methods for admin-assigned tasks to a specific member
    @Query("SELECT t FROM Task t WHERE t.assignedByAdmin.id = :adminId AND t.assignedToTeamMember.id = :memberId")
    List<Task> findTasksAssignedByAdminToMember(@Param("adminId") Long adminId, @Param("memberId") Long memberId);
    
    @Query("SELECT t FROM Task t WHERE t.assignedByAdmin.id = :adminId AND t.assignedToTeamMember.id = :memberId AND t.startDate = CURRENT_DATE")
    List<Task> findTodaysTasksAssignedByAdminToMember(@Param("adminId") Long adminId, @Param("memberId") Long memberId);
    
    // New methods for leader-assigned tasks to a specific member
    @Query("SELECT t FROM Task t WHERE t.assignedByLeader.id = :leaderId AND t.assignedToTeamMember.id = :memberId")
    List<Task> findTasksAssignedByLeaderToMember(@Param("leaderId") Long leaderId, @Param("memberId") Long memberId);
    
    @Query("SELECT t FROM Task t WHERE t.assignedByLeader.id = :leaderId AND t.assignedToTeamMember.id = :memberId AND t.startDate = CURRENT_DATE")
    List<Task> findTodaysTasksAssignedByLeaderToMember(@Param("leaderId") Long leaderId, @Param("memberId") Long memberId);

    List<Task> findAllByAssignedByAdminEmail(String email);

    List<Task> findAllByAssignedByLeaderEmail(String email);

    List<Task> findAllByAssignedToTeamMemberEmail(String email);

    List<Task> findAllByBranchCode(String branchCode);

    List<Task> findByAssignedToTeamMember(TeamMember teamMember);


    long countByStatusAndBranchCode(String status, String branchCode);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.startDate = CURRENT_DATE AND t.branchCode = :branchCode")
    long countTodaysTasks(@Param("branchCode") String branchCode);
}


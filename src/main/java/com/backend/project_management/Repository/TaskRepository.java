package com.backend.project_management.Repository;

import com.backend.project_management.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.assignedBy = :admin GROUP BY t.status")
    List<Object[]> countByStatusForAdmin(@Param("admin") ProjectAdmin admin);

    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.assignedTo = :member GROUP BY t.status")
    List<Object[]> countByStatusForMember(@Param("member") TeamMember member);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedBy = :admin AND t.startDate BETWEEN :start AND :end")
    Long countByDateRangeForAdmin(@Param("admin") ProjectAdmin admin, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.assignedTo = :member AND t.startDate BETWEEN :start AND :end")
    Long countByDateRangeForMember(@Param("member") TeamMember member, @Param("start") LocalDate start, @Param("end") LocalDate end);

}

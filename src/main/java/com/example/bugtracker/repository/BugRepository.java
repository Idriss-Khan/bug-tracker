package com.example.bugtracker.repository;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
    List<Bug> findByPostedBy(User postedBy);

    @Query("SELECT COUNT(b) FROM Bug b")
    int countAllBugs();

    int countByStatusNot(String status);

    int countByStatus(String status);

    int countByDue(LocalDate due);

    int countByDueBetween(LocalDate start, LocalDate end);

    @Query("SELECT COUNT(b) FROM Bug b WHERE b.priority = :severity")
    long countBugsBySeverity(@Param("severity") String severity);

    @Query("SELECT COUNT(b) FROM Bug b WHERE b.status = :status")
    long countBugsByStatus(@Param("status") String status);

    List<Bug> findByCreatedBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT b.created, COUNT(b) FROM Bug b GROUP BY b.created")
    List<Object[]> countBugsByDate();


    List<Bug> findByStatusIn(List<String> aNew);
}
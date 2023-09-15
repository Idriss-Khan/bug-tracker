package com.example.bugtracker.repository;

import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Set<Project> findByProjectManagerOrAssociatedUsers(User user, User user1);

    long countByStatus(String status);

    // Custom query to count projects by severity
    @Query("SELECT COUNT(p) FROM Project p WHERE p.priority = :severity")
    long countProjectsBySeverity(@Param("severity") String severity);
}

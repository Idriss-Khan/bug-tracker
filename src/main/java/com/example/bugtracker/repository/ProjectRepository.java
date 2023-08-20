package com.example.bugtracker.repository;

import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Set<Project> findByProjectManagerOrAssociatedUsers(User user, User user1);
}

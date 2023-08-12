package com.example.bugtracker.repository;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
    List<Bug> findByPostedBy(User postedBy);

}
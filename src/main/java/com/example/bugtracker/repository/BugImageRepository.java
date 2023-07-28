package com.example.bugtracker.repository;

import com.example.bugtracker.model.BugImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BugImageRepository extends JpaRepository<BugImage, Integer> {

}
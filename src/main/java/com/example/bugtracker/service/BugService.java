package com.example.bugtracker.service;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.repository.BugRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BugService {
    private final BugRepository bugRepository;

    public BugService(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    public Bug createBug(Bug bug) {
        // Add any additional business logic or validation

        return bugRepository.save(bug);
    }

    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    public Bug getBugById(Integer bugId) {
        return bugRepository.findById(bugId)
                .orElseThrow(() -> new NoSuchElementException("Bug not found"));
    }

    // Add other service methods as needed
}

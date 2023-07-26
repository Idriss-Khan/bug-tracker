package com.example.bugtracker.service;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.BugRepository;
import com.example.bugtracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {
    @Autowired
    private final ProjectRepository projectRepository;
    @Autowired
    private final BugRepository bugRepository;

    public ProjectService(ProjectRepository projectRepository, BugRepository bugRepository) {
        this.projectRepository = projectRepository;
        this.bugRepository = bugRepository;
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new NoSuchElementException("Project not found"));
    }
    public void deleteProjectById(Integer projectId) {
        Project project = getProjectById(projectId);
        if (project != null) {
            // Delete all the associated bugs first
            for (Bug bug : project.getBugs()) {
                deleteBugById(bug.getId());
            }
            projectRepository.delete(project);
        } else {
            throw new NoSuchElementException("Project not found");
        }
    }

    private void deleteBugById(Integer bugId) {
        bugRepository.deleteById(bugId);
    }

    public void addAssociatedUser(Integer projectId, User user) {
        Project project = getProjectById(projectId);
        project.addAssociatedUser(user);
        projectRepository.save(project);
    }

    public void removeAssociatedUser(Integer projectId, User user) {
        Project project = getProjectById(projectId);
        project.removeAssociatedUser(user);
        projectRepository.save(project);
    }
}

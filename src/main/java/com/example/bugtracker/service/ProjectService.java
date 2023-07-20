package com.example.bugtracker.service;

import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {
    @Autowired
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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

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
import java.util.Objects;
import java.util.Set;

@Service
public class ProjectService {
    @Autowired
    private final ProjectRepository projectRepository;
    @Autowired
    private final BugRepository bugRepository;
    @Autowired
    private NotificationService notificationService;

    public ProjectService(ProjectRepository projectRepository, BugRepository bugRepository) {
        this.projectRepository = projectRepository;
        this.bugRepository = bugRepository;
    }

    public Project createProject(Project project) {
        Project savedProject = projectRepository.save(project);

        if (savedProject.getProjectManager() != null) {
            String notificationMessage = "You have been assigned as the project manager for project: " + savedProject.getName();
            notificationService.createNotification(savedProject.getProjectManager(), notificationMessage);
        }

        return savedProject;
    }

    public void updateProject(Project updatedProject) {
        Project existingProject = projectRepository.findById(updatedProject.getId()).orElse(null);

        if (existingProject != null) {
            User oldProjectManager = existingProject.getProjectManager();
            User newProjectManager = updatedProject.getProjectManager();

            boolean projectManagerChanged = !Objects.equals(oldProjectManager, newProjectManager);

            if (projectManagerChanged) {
                if (oldProjectManager != null && !Objects.equals(oldProjectManager.getId(), newProjectManager.getId())) {
                    String removedMessage = "You have been removed as the project manager from project: " + existingProject.getName();
                    notificationService.createNotification(oldProjectManager, removedMessage);
                }

                if (newProjectManager != null && !Objects.equals(newProjectManager.getId(), oldProjectManager.getId())) {
                    String assignedMessage = "You have been assigned as the project manager for project: " + existingProject.getName();
                    notificationService.createNotification(newProjectManager, assignedMessage);
                }
            }

            // Handle notifications for adding associated users
            for (User newUser : updatedProject.getAssociatedUsers()) {
                if (!existingProject.getAssociatedUsers().contains(newUser)) {
                    String addedMessage = "You have been assigned to work on project: " + existingProject.getName();
                    notificationService.createNotification(newUser, addedMessage);
                }
            }

            // Handle notifications for removing associated users
            for (User oldUser : existingProject.getAssociatedUsers()) {
                if (!updatedProject.getAssociatedUsers().contains(oldUser)) {
                    String removedMessage = "You have been removed as an associated user from project: " + existingProject.getName();
                    notificationService.createNotification(oldUser, removedMessage);
                }
            }

            existingProject.setName(updatedProject.getName());
            existingProject.setDescription(updatedProject.getDescription());
            existingProject.setStartDate(updatedProject.getStartDate());
            existingProject.setEndDate(updatedProject.getEndDate());
            existingProject.setStatus(updatedProject.getStatus());
            existingProject.setPriority(updatedProject.getPriority());
            existingProject.setProjectManager(updatedProject.getProjectManager());
            existingProject.setAssociatedUsers(updatedProject.getAssociatedUsers());

            projectRepository.save(existingProject);
        }
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

    public Set<Project> getProjectsForUser(User user) {
        // Retrieve projects where the user is the manager or an associated user
        return projectRepository.findByProjectManagerOrAssociatedUsers(user, user);
    }

}

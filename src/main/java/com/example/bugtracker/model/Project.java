package com.example.bugtracker.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The Entity class represents a Project.
 */
@Entity
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status;

    private String priority;

    @ManyToOne
    @JoinColumn(name = "project_manager_id")
    private User projectManager;

    @ManyToMany
    @JoinTable(
            name = "project_user",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> associatedUsers;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Bug> bugs;

    public void addAssociatedUser(User user) {
        if (associatedUsers == null) {
            associatedUsers = new ArrayList<>();
        }
        associatedUsers.add(user);
    }

    public void removeAssociatedUser(User user) {
        if (associatedUsers != null) {
            associatedUsers.remove(user);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public User getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(User projectManager) {
        this.projectManager = projectManager;
    }

    public List<User> getAssociatedUsers() {
        return associatedUsers;
    }

    public void setAssociatedUsers(List<User> associatedUsers) {
        this.associatedUsers = associatedUsers;
    }

    public List<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Bug> bugs) {
        this.bugs = bugs;
    }

}

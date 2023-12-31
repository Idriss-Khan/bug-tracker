package com.example.bugtracker.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The Entity class represents an issue with a project.
 */
@Entity
@Table(name = "Bug")
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String stepsToReproduce;

    private String status;

    private String priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate created;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate due;

    @ManyToOne
    @JoinColumn(name = "posted_by")
    private User postedBy;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assigned_user")
    private User assignedUser;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public String getFormattedCreated() {
        return created.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getFormattedDue() {
        return due.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(User assignedUser) {
        this.assignedUser = assignedUser;
    }

    @Transient
    private List<MultipartFile> bugImages;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.ALL)
    private List<BugImage> bugImageEntities;

    public Bug() {
        this.created = LocalDate.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    public void setStepsToReproduce(String stepsToReproduce) {
        this.stepsToReproduce = stepsToReproduce;
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

    public User getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(User postedBy) {
        this.postedBy = postedBy;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<MultipartFile> getBugImages() {
        return bugImages;
    }

    public void setBugImages(List<MultipartFile> bugImages) {
        this.bugImages = bugImages;
    }

    public void addBugImageEntity(BugImage bugImage) {
        if (this.bugImageEntities == null) {
            this.bugImageEntities = new ArrayList<>();
        }
        this.bugImageEntities.add(bugImage);
        bugImage.setBug(this);
    }

    public void setBugImageEntities(List<BugImage> bugImageEntities) {
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public List<BugImage> getBugImageEntities() {
        return bugImageEntities;
    }
}

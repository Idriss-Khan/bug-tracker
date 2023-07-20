package com.example.bugtracker.model;

import jakarta.persistence.*;

import java.util.List;

/**
 * The Entity class represents an issue with a project.
 */
@Entity
@Table(name = "Bug")
public class Bug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String stepsToReproduce;

    private String status;

    private String priority;

    private String postedBy;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "bug", cascade = CascadeType.ALL)
    private List<BugImage> bugImages;


}

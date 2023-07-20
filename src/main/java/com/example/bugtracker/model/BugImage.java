package com.example.bugtracker.model;

import jakarta.persistence.*;

@Entity
public class BugImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String imageName;

    @Lob
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "bug_id")
    private Bug bug;
}

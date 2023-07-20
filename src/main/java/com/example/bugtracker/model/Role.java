package com.example.bugtracker.model;

import jakarta.persistence.*;

/**
 * The Entity class represents a user role such as ADMIN and USER.
 * It is mapped to the "roles" table in the database.
 */
@Entity
@Table(name="roles")
public class Role {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public Role(){
    }

    public Role(String name){
        this.name = name;
    }

    public Role(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    // Getters and Setters

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
}
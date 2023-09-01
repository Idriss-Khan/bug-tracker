package com.example.bugtracker.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "read_status", nullable = false)
    private boolean readStatus;

    // Constructors, getters, setters

    public String getFormattedTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        long minutesAgo = ChronoUnit.MINUTES.between(timestamp, now);

        if (minutesAgo < 1) {
            // Less than 1 minute ago, show "Just now"
            return "Just now";
        } else if (minutesAgo < 60) {
            // Less than 60 minutes ago, show minutes
            return minutesAgo + " minute" + (minutesAgo > 1 ? "s" : "") + " ago";
        } else if (minutesAgo < 1440) {
            // Less than 24 hours ago, show hours
            long hoursAgo = ChronoUnit.HOURS.between(timestamp, now);
            return hoursAgo + " hour" + (hoursAgo > 1 ? "s" : "") + " ago";
        } else {
            // Older than 24 hours, show absolute time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a");
            return timestamp.format(formatter);
        }
    }

    public void markAsRead() {
        this.readStatus = true;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }
}
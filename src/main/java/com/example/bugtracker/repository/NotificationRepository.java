package com.example.bugtracker.repository;

import com.example.bugtracker.model.Notification;
import com.example.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserOrderByTimestampDesc(User user);
    int countByUserAndReadStatus(User user, boolean readStatus);
}

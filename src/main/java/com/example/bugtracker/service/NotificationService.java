package com.example.bugtracker.service;

import com.example.bugtracker.model.Notification;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifications(User user) {
        return notificationRepository.findByUserOrderByTimestampDesc(user);
    }

    public int countUnreadNotifications(User user) {
        return notificationRepository.countByUserAndReadStatus(user, false);
    }

    public void createNotification(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());
        notification.setReadStatus(false);

        notificationRepository.save(notification);
    }

    public void markNotificationAsRead(Integer notificationId) {
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);

        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setReadStatus(true);
            notificationRepository.save(notification);
        }
    }
}
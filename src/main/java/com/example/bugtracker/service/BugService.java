package com.example.bugtracker.service;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.BugImage;
import com.example.bugtracker.model.Project;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.BugImageRepository;
import com.example.bugtracker.repository.BugRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class BugService {
    private final BugRepository bugRepository;
    private BugImageRepository bugImageRepository;
    private UserService userService;
    @Autowired
    private NotificationService notificationService;

    public BugService(BugRepository bugRepository, BugImageRepository bugImageRepository, UserService userService) {
        this.bugRepository = bugRepository;
        this.bugImageRepository = bugImageRepository;
        this.userService = userService;
    }

    public Bug createBug(Bug bug, List<MultipartFile> bugImages) {
        List<BugImage> bugImageEntities = new ArrayList<>();

        for (MultipartFile image : bugImages) {
            if (!image.isEmpty()) {
                String imageName = saveImageLocally(image);
                BugImage bugImage = new BugImage();
                bugImage.setImageName(imageName);
                bugImage.setBug(bug);
                bugImageEntities.add(bugImage);
            }
        }

        bug.setBugImages(null);
        bug.setBugImageEntities(bugImageEntities);

        Bug savedBug = bugRepository.save(bug);
        bugImageRepository.saveAll(bugImageEntities);

        return savedBug;
    }

    public void updateBug(Bug existingBug, Bug updatedBug) {
        User oldAssignedUser = existingBug.getAssignedUser();
        User newAssignedUser = updatedBug.getAssignedUser();

        boolean assignedUserChanged = !Objects.equals(oldAssignedUser, newAssignedUser);

        if (assignedUserChanged) {
            if (oldAssignedUser != null && newAssignedUser != null && !Objects.equals(oldAssignedUser.getId(), newAssignedUser.getId())) {
                String removedMessage = "You have been removed from working on bug: " + existingBug.getTitle();
                notificationService.createNotification(oldAssignedUser, removedMessage);
            }

            if (newAssignedUser != null && (oldAssignedUser == null || !Objects.equals(newAssignedUser.getId(), oldAssignedUser.getId()))) {
                String assignedMessage = "You have been assigned to work on bug: " + existingBug.getTitle();
                notificationService.createNotification(newAssignedUser, assignedMessage);
            }
        }

        existingBug.setTitle(updatedBug.getTitle());
        existingBug.setDescription(updatedBug.getDescription());
        existingBug.setStepsToReproduce(updatedBug.getStepsToReproduce());
        existingBug.setDue(updatedBug.getDue());
        existingBug.setPriority(updatedBug.getPriority());
        existingBug.setStatus(updatedBug.getStatus());

        if (newAssignedUser != null) {
            User assignedUser = userService.get(newAssignedUser.getId());
            existingBug.setAssignedUser(assignedUser);
        } else {
            existingBug.setAssignedUser(null); // Clear the assigned user if newAssignedUser is null
        }

        bugRepository.save(existingBug);
    }


    private String saveImageLocally(MultipartFile image) {
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
        String uploadDir = "src/main/resources/static/images"; // upload directory

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save image file in the upload directory
            try (InputStream inputStream = image.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not store image file: " + fileName, e);
        }

        return "/images/" + fileName;
    }

    public List<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    public Bug getBugById(Integer bugId) {
        return bugRepository.findById(bugId)
                .orElseThrow(() -> new NoSuchElementException("Bug not found"));
    }

    public List<Bug> getBugsByUser(User user) {
        return bugRepository.findByPostedBy(user);
    }

    public int getTotalBugCount() {
        return bugRepository.countAllBugs();
    }

    public int getOpenBugCount() {
        return bugRepository.countByStatusNot("Closed");
    }

    public int getClosedBugCount() {
        return bugRepository.countByStatus("Closed");
    }

    public int getBugsDueToday() {
        LocalDate today = LocalDate.now();
        return bugRepository.countByDue(today);
    }

    public int getBugsDueIn7Days() {
        LocalDate in7Days = LocalDate.now().plusDays(7);
        return bugRepository.countByDueBetween(LocalDate.now(), in7Days);
    }


}

package com.example.bugtracker.service;

import com.example.bugtracker.model.Bug;
import com.example.bugtracker.model.BugImage;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BugService {
    private final BugRepository bugRepository;
    private BugImageRepository bugImageRepository;

    public BugService(BugRepository bugRepository, BugImageRepository bugImageRepository) {
        this.bugRepository = bugRepository;
        this.bugImageRepository = bugImageRepository;
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

}

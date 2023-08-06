package com.example.bugtracker.service;

import com.example.bugtracker.model.Role;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.RoleRepository;
import com.example.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;

    public void saveWithDefaultRole(User user) {
        // Encoded password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Save a new user as a USER role by default
        Role userRole = roleRepo.findByName("USER");
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepo.save(user);
    }

    public List<User> listAll() {
        return userRepo.findAll();
    }

    public User get(Integer id) {
        return userRepo.findById(id).get();
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    public void save(User user) {
        User existingUser = userRepo.findById(user.getId()).orElse(null);
        if (existingUser != null) {
            // Preserve existing password if not modified
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encodedPassword = encoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
            }
        }

        userRepo.save(user);
    }

    public void updateProfileImage(User user, MultipartFile profilePicFile) {
        User currentUser = userRepo.findByEmail(user.getEmail());
        // Update profile image
        if (profilePicFile != null && !profilePicFile.isEmpty()) {

            String fileName = StringUtils.cleanPath(profilePicFile.getOriginalFilename());
            String uploadDir = "src/main/resources/static/profileimg";
            Path uploadPath = Paths.get(uploadDir);

            try {
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = profilePicFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not store profile picture: " + fileName, e);
            }
            currentUser.setProfilePicture("/profileimg/" + fileName);
        }
        userRepo.save(currentUser);
    }

    public boolean emailExists(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));
        return userOptional.isPresent();
    }

    public List<User> getUsersByRole(String roleName) {
        Role role = roleRepo.findByName(roleName);
        return userRepo.findByRoles(role.getName());
    }
}




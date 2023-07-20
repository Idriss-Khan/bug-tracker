package com.example.bugtracker.service;

import com.example.bugtracker.model.Role;
import com.example.bugtracker.model.User;
import com.example.bugtracker.repository.RoleRepository;
import com.example.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    public void save(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);
    }

    public boolean emailExists(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepo.findByEmail(email));
        return userOptional.isPresent();
    }
}



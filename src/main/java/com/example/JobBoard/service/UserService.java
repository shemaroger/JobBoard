package com.example.JobBoard.service;

import com.example.JobBoard.model.Role;
import com.example.JobBoard.model.User;
import com.example.JobBoard.repository.RoleRepository;
import com.example.JobBoard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        return userRepository.save(user); // Assuming save will handle both update and create
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}

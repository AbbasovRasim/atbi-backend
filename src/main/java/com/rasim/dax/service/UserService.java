package com.rasim.dax.service;

import com.rasim.dax.entity.User;
import com.rasim.dax.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {

        this.userRepository = userRepository;
    }

    // CREATE USER
    public User createUser(User user) {

        return userRepository.save(user);
    }

    // GET ALL USERS
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    // GET USER BY ID
    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);
    }

    // GET USER BY USERNAME
    public Optional<User> getByUsername(
            String username
    ) {

        return userRepository.findByUsername(username);
    }
}
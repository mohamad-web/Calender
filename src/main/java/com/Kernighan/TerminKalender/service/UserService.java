package com.Kernighan.TerminKalender.service;

import com.Kernighan.TerminKalender.model.Role;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(String username, String email, String rawPassword) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User(username, email, encodedPassword, Role.USER, true);
        return userRepository.save(user);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User saveUser(User user) {

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User setUserAsAdmin(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setRole(Role.ADMIN);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
    }
}

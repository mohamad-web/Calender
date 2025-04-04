package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.model.Role;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Benutzername existiert bereits.");
            return "register";
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("error", "E-Mail-Adresse ist bereits registriert.");
            return "register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        userRepository.save(user);

        model.addAttribute("success", "Registrierung erfolgreich! Sie können sich jetzt anmelden.");
        return "register";
    }
}
package com.Kernighan.TerminKalender.config;

import com.Kernighan.TerminKalender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        // Prüfen, ob der Admin existiert
        if (userService.findByUsername("admin").isEmpty()) {
            userService.registerUser("admin", "admin@example.com", "admin123");
            System.out.println("Admin-Benutzer erstellt: Benutzername: admin, Passwort: admin123");
        }
    }
}
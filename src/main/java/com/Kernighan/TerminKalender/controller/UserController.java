package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.model.Termin;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.service.TerminService;
import com.Kernighan.TerminKalender.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TerminService terminService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/make-admin")
    public ResponseEntity<User> makeUserAdmin(@PathVariable Long id) {
        User updatedUser = userService.setUserAsAdmin(id);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkUserExists(@RequestParam String username) {
        boolean exists = userService.findByUsername(username).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/benutzer-dashboard")
    public String getBenutzerDashboard(Model model) {

        List<Termin> termine = terminService.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        List<Map<String, String>> formattedTermine = termine.stream()
                .map(t -> Map.of(
                        "title", t.getTitle(),
                        "startTime", t.getStartTime().format(formatter),
                        "endTime", t.getEndTime().format(formatter)
                ))
                .collect(Collectors.toList());

        model.addAttribute("termine", formattedTermine);
        return "benutzer-dashboard";
    }

    @GetMapping("/alle-benutzer")
    public String alleBenutzer(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin-alle-benutzer";
    }

    @PostMapping("/benutzer/loeschen/{id}")
    public String benutzerLoeschen(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/alle-benutzer";
    }
}
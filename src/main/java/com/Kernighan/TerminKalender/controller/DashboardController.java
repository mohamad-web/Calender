package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.model.Role;
import com.Kernighan.TerminKalender.model.Termin;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.service.TerminService;
import com.Kernighan.TerminKalender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final TerminService terminService;
    private final UserService userService;

    @Autowired
    public DashboardController(TerminService terminService, UserService userService) {
        this.terminService = terminService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails currentUser, Model model) {
        if (currentUser == null) {
            return "redirect:/login";
        }

        String username = currentUser.getUsername();
        User user = userService.findByUsername(username).orElseThrow();
        model.addAttribute("username", username);

        if (user.getRole() == Role.ADMIN) {
            List<Termin> termine = terminService.getAllTermineForUser(user.getId());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            List<Map<String, String>> formattedTermine = termine.stream()
                    .map(t -> Map.of(
                            "id", String.valueOf(t.getId()),
                            "title", t.getTitle(),
                            "startTime", t.getStartTime().format(formatter),
                            "endTime", t.getEndTime().format(formatter)
                    ))
                    .collect(Collectors.toList());

            model.addAttribute("termine", formattedTermine);
            model.addAttribute("users", userService.getAllUsers());
            return "admin_dashboard";
        } else {
            List<Termin> termine = terminService.getAllTermineForUser(user.getId());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            List<Map<String, String>> formattedTermine = termine.stream()
                    .map(t -> Map.of(
                            "id", String.valueOf(t.getId()),
                            "title", t.getTitle(),
                            "startTime", t.getStartTime().format(formatter),
                            "endTime", t.getEndTime().format(formatter)
                    ))
                    .collect(Collectors.toList());

            model.addAttribute("termine", formattedTermine);
            return "benutzer-dashboard";
        }
    }

    // TODO Listing the Accepted Invitations in Dashboard
}


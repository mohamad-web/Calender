package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.model.Termin;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.service.TerminService;
import com.Kernighan.TerminKalender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/termine")
public class TerminWebController {
    private final TerminService terminService;
    private final UserService userService;

    @Autowired
    public TerminWebController(TerminService terminService, UserService userService) {
        this.terminService = terminService;
        this.userService = userService;
    }

    @GetMapping("/neu")
    public String terminErstellenForm(Model model) {
        model.addAttribute("termin", new Termin());


        List<User> benutzerListe = userService.getAllUsers();
        model.addAttribute("benutzerListe", benutzerListe);

        return "termin-erstellen";
    }


/**
    @PostMapping("/neu")
    public ResponseEntity<String> erstelleTermin(@RequestBody Map<String, String> requestData, Principal principal) {
        String username = principal.getName();
        User organisator = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        Termin termin = new Termin();
        termin.setTitle(requestData.get("title"));
        termin.setDescription(requestData.get("description"));
        termin.setStartTime(LocalDateTime.parse(requestData.get("startTime")));
        termin.setEndTime(LocalDateTime.parse(requestData.get("endTime")));
        termin.setOrganizer(organisator);

        List<String> eingeladeneBenutzer = Arrays.asList(requestData.get("eingeladeneBenutzer").split(","));

        terminService.saveTerminWithInvitations(termin, new HashSet<>(eingeladeneBenutzer));

        return ResponseEntity.ok("Termin erfolgreich erstellt");
    }
**/
@PostMapping("/neu")
public ResponseEntity<?> erstelleTermin(@RequestBody Map<String, Object> payload, Principal principal) {
    String title = (String) payload.get("title");
    String description = (String) payload.get("description");
    String startTime = (String) payload.get("startTime");
    String endTime = (String) payload.get("endTime");


    List<String> eingeladeneBenutzer = (List<String>) payload.getOrDefault("eingeladeneBenutzer", new ArrayList<>());


    System.out.println("📨 Eingeladene Benutzer (Backend): " + eingeladeneBenutzer);

    if (title == null || startTime == null || endTime == null) {
        return ResponseEntity.badRequest().body("Fehlende Daten im Anfrage-Body!");
    }

    Termin termin = new Termin();
    termin.setTitle(title);
    termin.setDescription(description);
    termin.setStartTime(LocalDateTime.parse(startTime));
    termin.setEndTime(LocalDateTime.parse(endTime));

    User organisator = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

    termin.setOrganizer(organisator);

    terminService.saveTerminWithInvitations(termin, new HashSet<>(eingeladeneBenutzer));

    return ResponseEntity.ok("Termin wurde erfolgreich erstellt!");
}

    @GetMapping("/meine-termine")
    public String meineTermine(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));


        List<Termin> termine = terminService.getAllTermineForUser(user.getId());

        model.addAttribute("username", username);
        model.addAttribute("termine", termine);

        return "meine-termine";
    }

    @GetMapping("/bearbeiten/{id}")
    public String terminBearbeitenForm(@PathVariable Long id, Model model, Principal principal) {
        Optional<Termin> terminOpt = terminService.getTerminById(id);

        if (terminOpt.isEmpty()) {
            return "redirect:/dashboard";
        }

        Termin termin = terminOpt.get();


        if (!termin.getOrganizer().getUsername().equals(principal.getName())) {
            return "redirect:/dashboard";
        }

        model.addAttribute("termin", termin);
        return "termin-bearbeiten";
    }
}
package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.dto.EinladungDTO;
import com.Kernighan.TerminKalender.model.Termin;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.repository.TerminRepository;
import com.Kernighan.TerminKalender.service.EinladungService;
import com.Kernighan.TerminKalender.service.TerminService;
import com.Kernighan.TerminKalender.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/termine")
public class TerminRestController {
    private final TerminService terminService;
    private final UserService userService;
    private final EinladungService einladungService;
    private final TerminRepository terminRepository;

    @Autowired
    public TerminRestController(TerminService terminService, UserService userService, EinladungService einladungService, TerminRepository terminRepository) {
        this.terminService = terminService;
        this.userService = userService;
        this.einladungService = einladungService;
        this.terminRepository = terminRepository;
    }

    @GetMapping
    public List<Termin> getAllTermine() {
        return terminService.getAllTermine();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Termin> getTerminById(@PathVariable Long id) {
        Optional<Termin> termin = terminService.getTerminById(id);
        return termin.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Termin createTermin(@RequestBody Termin termin) {
        return terminService.saveTermin(termin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTermin(@PathVariable Long id) {
        terminService.deleteTermin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meine")
    public List<Termin> getMeineTermine(Principal principal) {
        if (principal == null) {
            throw new RuntimeException("Benutzer nicht eingeloggt");
        }

        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        return terminService.getAllTermineForUser(user.getId());
    }

    @GetMapping("/meine-termine")
    public String meineTermine(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));

        List<Termin> termine = terminService.getAllTermineByOrganizer(user.getId());

        model.addAttribute("username", username);
        model.addAttribute("termine", termine);

        return "meine-termine";
    }

    @GetMapping("/{terminId}/einladungen")
    public ResponseEntity<List<EinladungDTO>> getEinladungenForTermin(@PathVariable Long terminId) {
        Optional<Termin> terminOpt = terminService.getTerminById(terminId);
        if (terminOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EinladungDTO> einladungen = einladungService.getEinladungenFuerTermin(terminOpt.get())
                .stream()
                .map(e -> new EinladungDTO(
                        e.getEmpfaenger() != null ? e.getEmpfaenger().getUsername() : "Unbekannt",
                        e.getStatus()
                ))
                .toList();

        return ResponseEntity.ok(einladungen);
    }

    @DeleteMapping("/loeschen/{id}")
    public ResponseEntity<?> terminLoeschen(@PathVariable Long id, Principal principal) {
        Optional<Termin> terminOpt = terminRepository.findById(id);

        if (terminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Termin nicht gefunden.");
        }

        Termin termin = terminOpt.get();

        if (!termin.getOrganizer().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sie dürfen diesen Termin nicht löschen.");
        }

        terminRepository.delete(termin);
        return ResponseEntity.ok().body("Termin erfolgreich gelöscht.");
    }

    @PutMapping("/bearbeiten/{id}")
    public ResponseEntity<?> terminBearbeiten(@PathVariable Long id, @RequestBody Termin neuerTermin, Principal principal) {
        Optional<Termin> terminOpt = terminRepository.findById(id);

        if (terminOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Termin nicht gefunden.");
        }

        Termin termin = terminOpt.get();


        if (!termin.getOrganizer().getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sie dürfen diesen Termin nicht bearbeiten.");
        }


        termin.setTitle(neuerTermin.getTitle());
        termin.setDescription(neuerTermin.getDescription());
        termin.setStartTime(neuerTermin.getStartTime());
        termin.setEndTime(neuerTermin.getEndTime());

        terminRepository.save(termin);
        return ResponseEntity.ok("Termin erfolgreich aktualisiert.");
    }

}

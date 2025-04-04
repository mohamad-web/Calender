package com.Kernighan.TerminKalender.controller;

import com.Kernighan.TerminKalender.model.Einladung;
import com.Kernighan.TerminKalender.model.Termin;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.service.EinladungService;
import com.Kernighan.TerminKalender.service.TerminService;
import com.Kernighan.TerminKalender.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invitation")
@RequiredArgsConstructor
public class EinladungController {

    private final TerminService terminService;
    private final UserService userService;
    private final EinladungService einladungService;


    @PostMapping("/{terminId}/invite/{username}")
    public ResponseEntity<String> inviteUser(@PathVariable Long terminId, @PathVariable String username) {
        Optional<Termin> terminOpt = terminService.getTerminById(terminId);
        Optional<User> userOpt = userService.getUserByUsername(username);

        if (terminOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Termin oder Benutzer nicht gefunden.");
        }

        Termin termin = terminOpt.get();
        User user = userOpt.get();
        einladungService.sendeEinladung(termin, user);

        return ResponseEntity.ok("Benutzer erfolgreich eingeladen.");
    }


    @GetMapping("/{terminId}/status")
    public ResponseEntity<List<Einladung>> getInvitationStatus(@PathVariable Long terminId) {
        Optional<Termin> terminOpt = terminService.getTerminById(terminId);
        if (terminOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(einladungService.getEinladungenFuerTermin(terminOpt.get()));
    }


    @PostMapping("/{einladungId}/accept")
    public ResponseEntity<String> acceptInvitation(@PathVariable Long einladungId) {
        einladungService.akzeptiereEinladung(einladungId);
        return ResponseEntity.ok("Einladung akzeptiert.");
    }


    @PostMapping("/{einladungId}/decline")
    public ResponseEntity<String> declineInvitation(@PathVariable Long einladungId) {
        einladungService.ablehnenEinladung(einladungId);
        return ResponseEntity.ok("Einladung abgelehnt.");
    }


    @GetMapping("/meine-einladungen/{username}")
    public ResponseEntity<List<Einladung>> getMeineEinladungen(@PathVariable String username) {
        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        User user = userOpt.get();
        List<Einladung> einladungen = einladungService.getEinladungenFürBenutzer(user.getId());

        return ResponseEntity.ok(einladungen);
    }
}

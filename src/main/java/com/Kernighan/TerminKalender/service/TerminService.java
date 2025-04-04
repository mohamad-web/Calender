package com.Kernighan.TerminKalender.service;

import com.Kernighan.TerminKalender.model.Einladung;
import com.Kernighan.TerminKalender.model.Termin;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.repository.EinladungRepository;
import com.Kernighan.TerminKalender.repository.TerminRepository;
import com.Kernighan.TerminKalender.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TerminService {
    private final TerminRepository terminRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final EinladungService einladungService;
    private final EinladungRepository einladungRepository;

    @Autowired
    public TerminService(TerminRepository terminRepository, UserRepository userRepository, UserService userService, EinladungService einladungService, EinladungRepository einladungRepository) {
        this.terminRepository = terminRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.einladungService = einladungService;
        this.einladungRepository = einladungRepository;
    }

    public Optional<Termin> getTerminById(Long terminId) {
        return terminRepository.findById(terminId);
    }

    public List<Termin> getAllTermine() {
        return terminRepository.findAll();
    }


    public Termin saveTermin(Termin termin) {
        return terminRepository.save(termin);
    }

    public void deleteTermin(Long id) {
        terminRepository.deleteById(id);
    }


    public Termin saveTerminWithInvitations(Termin termin, Set<String> eingeladeneBenutzer) {
        System.out.println("📨 Termin wird gespeichert: " + termin.getTitle());
        System.out.println("👥 Eingeladene Benutzer: " + eingeladeneBenutzer);

        Termin gespeicherterTermin = terminRepository.save(termin);

        for (String username : eingeladeneBenutzer) {
            userService.findByUsername(username).ifPresent(user -> {
                System.out.println("➡ Einladung wird erstellt für: " + user.getUsername());

                Einladung einladung = new Einladung(gespeicherterTermin, user);
                einladungRepository.save(einladung);
            });
        }

        return gespeicherterTermin;
    }

    public List<Termin> getAllTermineForUser(Long id) {
        return terminRepository.findByOrganizerId(id);
    }

    public List<Termin> getAllTermineByOrganizer(Long organizerId) {
        return terminRepository.findByOrganizerId(organizerId);
    }

    public List<Termin> findAll() {
        return terminRepository.findAll();
    }

    public List<Termin> getTermineMitEinladungFuerBenutzer(User user) {
        return terminRepository.findAll().stream()
                .filter(t -> t.getInvitedUsers().contains(user))
                .collect(Collectors.toList());
    }

    public void sendeEinladung(Termin termin, User empfaenger) {
        Einladung einladung = new Einladung();
        einladung.setTermin(termin);
        einladung.setEmpfaenger(empfaenger);
        einladung.setStatus("PENDING");

        einladung.setTitle(termin.getTitle());
        einladung.setDescription(termin.getDescription());
        einladung.setStartTime(termin.getStartTime());
        einladung.setEndTime(termin.getEndTime());
        einladung.setSender(termin.getOrganizer().getUsername());

        einladungRepository.save(einladung);
    }

}

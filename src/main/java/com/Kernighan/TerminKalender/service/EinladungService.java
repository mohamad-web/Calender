package com.Kernighan.TerminKalender.service;

import com.Kernighan.TerminKalender.model.Einladung;
import com.Kernighan.TerminKalender.model.Termin;
import com.Kernighan.TerminKalender.model.User;
import com.Kernighan.TerminKalender.repository.EinladungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EinladungService {

    private final EinladungRepository einladungRepository;

    @Autowired
    public EinladungService(EinladungRepository einladungRepository) {
        this.einladungRepository = einladungRepository;
    }

    public void sendeEinladung(Termin termin, User empfaenger) {
        Einladung einladung = new Einladung(termin, empfaenger);
        einladungRepository.save(einladung);
    }

    public List<Einladung> getEinladungenFürBenutzer(Long userId) {
        return einladungRepository.findByEmpfaengerIdAndStatus(userId, "PENDING");
    }

    public void akzeptiereEinladung(Long einladungId) {
        Einladung einladung = einladungRepository.findById(einladungId).orElseThrow();
        einladung.setStatus("ACCEPTED");
        einladungRepository.save(einladung);
    }

    public void ablehnenEinladung(Long einladungId) {
        Einladung einladung = einladungRepository.findById(einladungId).orElseThrow();
        einladung.setStatus("DECLINED");
        einladungRepository.save(einladung);
    }



    public List<Einladung> getEinladungenFuerTermin(Termin termin) {
        return einladungRepository.findByTermin(termin);
    }


    public void saveEinladung(Einladung einladung) {
        einladungRepository.save(einladung);
    }

    public void aktualisiereStatus(Long einladungId, String username, boolean akzeptiert) {
        Einladung einladung = einladungRepository.findById(einladungId)
                .orElseThrow(() -> new RuntimeException("Einladung nicht gefunden."));

        if (!einladung.getEmpfaenger().getUsername().equals(username)) {
            throw new RuntimeException("Nicht berechtigt, diese Einladung zu bearbeiten.");
        }

        einladung.setAccepted(akzeptiert);
        einladungRepository.save(einladung);
    }



}

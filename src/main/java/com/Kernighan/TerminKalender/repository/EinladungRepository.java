package com.Kernighan.TerminKalender.repository;

import com.Kernighan.TerminKalender.model.Einladung;
import com.Kernighan.TerminKalender.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EinladungRepository extends JpaRepository<Einladung, Long> {
    List<Einladung> findByAcceptedFalse();
    List<Einladung> findByEmpfaengerIdAndStatus(Long empfaengerId, String status);

    List<Einladung> findByTermin(Termin termin);
}

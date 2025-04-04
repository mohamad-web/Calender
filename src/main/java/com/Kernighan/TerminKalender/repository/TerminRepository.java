
package com.Kernighan.TerminKalender.repository;

import com.Kernighan.TerminKalender.model.Termin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface TerminRepository extends JpaRepository<Termin, Long> {

    List<Termin> findByOrganizerId(Long organizerId);
}

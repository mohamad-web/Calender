package com.Kernighan.TerminKalender.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "einladungen")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Einladung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @ManyToOne
    @JoinColumn(name = "termin_id", nullable = false)
    private Termin termin;

    @ManyToOne
    @JoinColumn(name = "empfaenger_id", nullable = false)
    private User empfaenger;

    @Column(nullable = false)
    private String status = "PENDING";

    private String sender;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private String description;

    @Column(nullable = false)
    private boolean accepted = false;


    public Einladung() {}

    public Einladung(Termin termin, User empfaenger) {
        this.termin = termin;
        this.empfaenger = empfaenger;
        this.status = "PENDING";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Termin getTermin() {
        return termin;
    }

    public void setTermin(Termin termin) {
        this.termin = termin;
    }

    public User getEmpfaenger() {
        return empfaenger;
    }

    public void setEmpfaenger(User empfaenger) {
        this.empfaenger = empfaenger;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Einladung einladung = (Einladung) o;
        return Objects.equals(id, einladung.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

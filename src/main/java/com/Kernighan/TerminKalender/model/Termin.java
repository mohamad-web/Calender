package com.Kernighan.TerminKalender.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "termine")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Termin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToMany
    @JoinTable(
            name = "termin_invitation",
            joinColumns = @JoinColumn(name = "termin_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> invitedUsers;


    @ElementCollection
    private Set<String> invitationStatus;

    @OneToMany(mappedBy = "termin", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Einladung> einladungen = new HashSet<>();


    public Set<User> getInvitedUsers() {
        Set<User> invitedUsers = new HashSet<>();
        for (Einladung einladung : einladungen) {
            invitedUsers.add(einladung.getEmpfaenger());
        }
        return invitedUsers;
    }

    public void addEinladung(Einladung einladung) {
        einladungen.add(einladung);
        einladung.setTermin(this);
    }

    public void removeEinladung(Einladung einladung) {
        einladungen.remove(einladung);
        einladung.setTermin(null);
    }



    public Set<String> getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(Set<String> invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public void setInvitedUsers(Set<User> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }

    public User getBenutzer() {
        return organizer;
    }
}
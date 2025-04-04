package com.Kernighan.TerminKalender.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_logs")
public class SystemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp = LocalDateTime.now();
    private String adminUsername;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private String action;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(columnDefinition = "TEXT")
    private String details;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getAdminUsername() { return adminUsername; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}

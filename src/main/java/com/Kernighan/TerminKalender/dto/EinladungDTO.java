package com.Kernighan.TerminKalender.dto;

public class EinladungDTO {
    private String empfaengerName;
    private String status;

    public EinladungDTO(String empfaengerName, String status) {
        this.empfaengerName = empfaengerName;
        this.status = status;
    }

    public String getEmpfaengerName() {
        return empfaengerName;
    }

    public String getStatus() {
        return status;
    }
}

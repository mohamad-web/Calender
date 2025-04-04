package com.Kernighan.TerminKalender.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TerminDto {



    @NotBlank(message = "Titel darf nicht leer sein.")
    private String title;

    private String description;

    @NotNull(message = "Startzeit darf nicht leer sein.")
    @Future(message = "Startzeit muss in der Zukunft liegen.")
    private LocalDateTime startTime;

    @NotNull(message = "Endzeit darf nicht leer sein.")
    @Future(message = "Endzeit muss in der Zukunft liegen.")
    private LocalDateTime endTime;
}

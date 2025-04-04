package com.Kernighan.TerminKalender.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Benutzername darf nicht leer sein.")
    @Size(min = 3, max = 20, message = "Benutzername muss zwischen 3 und 20 Zeichen lang sein.")
    private String username;

    @NotBlank(message = "E-Mail darf nicht leer sein.")
    @Email(message = "Ungültige E-Mail-Adresse.")
    private String email;

    @NotBlank(message = "Passwort darf nicht leer sein.")
    @Size(min = 6, message = "Passwort muss mindestens 6 Zeichen lang sein.")
    private String password;
}
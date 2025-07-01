package it.filminpocket.FilmInPocket.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Il nome utente non può essere vuoto")
    private String username;
    @NotBlank(message = "La password non può essere vuota")
    private String password;
}

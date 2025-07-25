package it.filminpocket.FilmInPocket.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "Il nome utente non può essere vuoto")
    @Size(min = 3,max = 20, message = "Lo username deve essere tra i 3 e i 20 caratteri ")
    private String username;
    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Deve essere un'email valida")
    private String email;
    @NotBlank(message = "La password non può essere vuota")
    @Size(min = 3, message = "La password deve contenere almeno 3 caratteri")
    private String password;
}

/**
 * @NotBlank: Assicura che il campo non sia nullo e non sia una stringa vuota.
 * @Size: Definisce la lunghezza minima e massima.
 * @Email: Controlla che il formato della stringa sia quello di un'email valida.
 */

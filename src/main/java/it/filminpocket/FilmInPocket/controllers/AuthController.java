package it.filminpocket.FilmInPocket.controllers;
import it.filminpocket.FilmInPocket.dtos.LoginRequestDto;
import it.filminpocket.FilmInPocket.dtos.LoginResponseDto;
import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.dtos.UserRegistrationDto;
import it.filminpocket.FilmInPocket.servicies.AuthService;
import it.filminpocket.FilmInPocket.servicies.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /**
     * Endpoint per la registrazione di un nuovo utente.
     * Accetta un DTO con i dati di registrazione e lo valida.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@RequestBody @Validated UserRegistrationDto registrationDto) {
        return authService.registerUser(registrationDto);
    }

    /**
     * Endpoint per il login di un utente.
     * Accetta le credenziali e, se corrette, restituisce un token JWT.
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto loginUser(@RequestBody @Validated LoginRequestDto loginDto) {
        return authService.loginUser(loginDto);
    }
}

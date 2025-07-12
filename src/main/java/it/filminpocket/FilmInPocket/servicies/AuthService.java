package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.LoginRequestDto;
import it.filminpocket.FilmInPocket.dtos.LoginResponseDto;
import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.dtos.UserRegistrationDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.UnauthorizedException;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import it.filminpocket.FilmInPocket.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static it.filminpocket.FilmInPocket.servicies.UserCardAcquisitionService.RECHARGE_INTERVAL_HOURS;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTool jwtTool;

    public UserDto registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username già in uso.");
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        // Imposta ruolo di default se mancante
        if (newUser.getRole() == null) {
            newUser.setRole(it.filminpocket.FilmInPocket.enumerated.UserRole.ROLE_USER);
        }

        // Imposta i ticket iniziali
        newUser.setFilmTickets(2);
        newUser.setLastTicketRecharge(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);
        LocalDateTime nextRecharge = savedUser.getFilmTickets() < UserCardAcquisitionService.MAX_TICKETS
                ? savedUser.getLastTicketRecharge().plusHours(RECHARGE_INTERVAL_HOURS)
                : null;

        return new UserDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getFilmTickets(),
                nextRecharge
        );
    }

    public LoginResponseDto loginUser(LoginRequestDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTool.createToken(user);
            LocalDateTime nextRecharge = user.getFilmTickets() < UserCardAcquisitionService.MAX_TICKETS
                    ? user.getLastTicketRecharge().plusHours(RECHARGE_INTERVAL_HOURS)
                    : null;

            return new LoginResponseDto(
                    accessToken,
                    new UserDto(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getFilmTickets(),
                            nextRecharge
                    )
            );

        } catch (Exception e) {
            throw new UnauthorizedException("Credenziali non valide. Effettua di nuovo il login.");
        }
    }
}

package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.LoginRequestDto;
import it.filminpocket.FilmInPocket.dtos.LoginResponseDto;
import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.dtos.UserRegistrationDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.UnauthorizedException; // (Crea questa eccezione custom)
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import it.filminpocket.FilmInPocket.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
        if(userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username già in uso.");
        }

        User newUser = new User();
        newUser.setUsername(registrationDto.getUsername());
        newUser.setEmail(registrationDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        User savedUser = userRepository.save(newUser);
        UserDto userDto = new UserDto();
        userDto.setId(savedUser.getId());
        userDto.setUsername(savedUser.getUsername());
        userDto.setEmail(savedUser.getEmail());
        userDto.setFilmTickets(savedUser.getFilmTickets());
        userDto.setNextTicketRechargeTime(savedUser.getLastTicketRecharge().plusHours(RECHARGE_INTERVAL_HOURS));

        return userDto;
    }

    public LoginResponseDto loginUser(LoginRequestDto loginDto) {
        try {

            /**
             * Usa l'AuthenticationManager per validare le credenziali
              */
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            /**
             * Se l'autenticazione ha successo, estrai l'utente e crea il token
              */
            User user = (User) authentication.getPrincipal();
            String accessToken = jwtTool.createToken(user);

            /**
             * Crea il DTO per la risposta
             */
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setFilmTickets(user.getFilmTickets());

            return new LoginResponseDto(accessToken, userDto);

        } catch (Exception e) {

            /**
             * Lancia un'eccezione se le credenziali sono errate
             */
            throw new UnauthorizedException("Credenziali non valide. Effettua di nuovo il login.");
        }
    }
}

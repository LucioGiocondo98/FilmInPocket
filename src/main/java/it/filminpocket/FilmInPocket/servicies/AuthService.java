package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.LoginRequestDto;
import it.filminpocket.FilmInPocket.dtos.LoginResponseDto;
import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.dtos.UserRegistrationDto;
import it.filminpocket.FilmInPocket.entities.CustomUserDetails;
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
        // Assicura che venga settato un ruolo di default
        if (newUser.getRole() == null) {
            newUser.setRole(it.filminpocket.FilmInPocket.enumerated.UserRole.ROLE_USER);
        }

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
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userRepository.findById(userDetails.getId())
                    .orElseThrow(() -> new UnauthorizedException("Utente non trovato"));
            String accessToken = jwtTool.createToken(user);

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setFilmTickets(user.getFilmTickets());

            return new LoginResponseDto(accessToken, userDto);

        } catch (Exception e) {
            throw new UnauthorizedException("Credenziali non valide. Effettua di nuovo il login.");
        }
    }
}

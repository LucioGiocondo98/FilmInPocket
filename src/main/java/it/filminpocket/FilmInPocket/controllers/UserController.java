package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.servicies.UserCardAcquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserCardAcquisitionService userCardAcquisitionService;

    @GetMapping("/me/tickets")
    public UserDto getCurrentUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        userCardAcquisitionService.rechargeFilmTicketsForUser(user);

        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFilmTickets(),
                user.getLastTicketRecharge() != null
                        ? user.getLastTicketRecharge().plusHours(12)
                        : null
        );
    }
}
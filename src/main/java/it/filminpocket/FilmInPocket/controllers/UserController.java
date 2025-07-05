package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import it.filminpocket.FilmInPocket.servicies.UserCardAcquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("me/cards")
public class UserController {
    @Autowired
    private UserCardAcquisitionService userCardAcquisitionService;

    @Autowired
    private UserRepository userRepository;


    /**
     * Endpoint per acquisire un pacchetto di 5 carte casuali consumando un ticket.
     * L'utente deve avere almeno 1 filmTicket disponibile.
     */
    @PostMapping("/acquire-pack")
    public List<CardDto> acquireNewCardPack(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Utente non trovato."));

        return userCardAcquisitionService.acquireRandomCardPack(user.getId());
    }

}

package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.mappers.CardMapper;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import it.filminpocket.FilmInPocket.servicies.UserCardAcquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("me/cards")
public class UserController {
    @Autowired
    private UserCardAcquisitionService userCardAcquisitionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardMapper cardMapper;


    /**
     * Endpoint per acquisire un pacchetto di 5 carte casuali consumando un ticket.
     * L'utente deve avere almeno 1 filmTicket disponibile.
     */
    @PostMapping("/acquire-pack")
    public List<CardDto> acquireNewCardPack(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return userCardAcquisitionService.acquireRandomCardPack(user.getId());
    }

    @GetMapping("/collection/{cardId}")
    public CardDto getCardFromCollection(@PathVariable int cardId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Card card = userRepository.findCardInUserCollection(user.getId(), cardId)
                .orElseThrow(() -> new NotFoundException("La carta non è presente nella tua collezione."));

        return cardMapper.convertToDto(card);
    }

    public Map<String, Object> getUserTicketsInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        // Aggiorna i ticket al momento della richiesta
        userCardAcquisitionService.rechargeFilmTicketsForUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("filmTickets", user.getFilmTickets());

        if (user.getFilmTickets() < 2 && user.getLastTicketRecharge() != null) {
            LocalDateTime nextRecharge = user.getLastTicketRecharge().plusHours(12);
            response.put("nextRecharge", nextRecharge);
        } else {
            response.put("nextRecharge", null);
        }

        return response;
    }

}

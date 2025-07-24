package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.AdminUserViewDto;
import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.servicies.AdminViewerService;
import it.filminpocket.FilmInPocket.servicies.CardService;
import it.filminpocket.FilmInPocket.servicies.UserCardAcquisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private AdminViewerService adminViewerService;
    @Autowired
    private UserCardAcquisitionService userCardAcquisitionService;

    @Autowired
    private CardService cardService;

    @GetMapping("/users")
    public Page<AdminUserViewDto> getAllUsers(Pageable pageable) {
        return adminViewerService.getAllUsers(pageable);
    }

    @GetMapping("/users/{id}")
    public AdminUserViewDto getUserById(@PathVariable int id) {
        return adminViewerService.getUserById(id);
    }

    @GetMapping("/cards")
    public Page<CardDto> getAllCards(Pageable pageable) {
        return cardService.findAll(pageable);
    }

    @GetMapping("/filtered/cards")
    public Page<CardDto> getFilteredCardsForAdmin(
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) it.filminpocket.FilmInPocket.enumerated.Rarity rarity,
            @RequestParam(required = false) String cardType,
            Pageable pageable
    ) {
        return cardService.findAllWithFilters(
                Optional.ofNullable(nameContains),
                Optional.ofNullable(rarity),
                Optional.ofNullable(genre),
                Optional.ofNullable(year),
                Optional.ofNullable(cardType),
                pageable
        );
    }
    @PostMapping("/recharge-tickets")
    public void rechargeAllTickets() {
        try {
            userCardAcquisitionService.performScheduledTicketRechargeForAllUsers();
        } catch (Exception e) {
            e.printStackTrace(); // stampa nel log di Koyeb
            throw new RuntimeException("Errore durante la ricarica ticket", e);
        }
    }

}

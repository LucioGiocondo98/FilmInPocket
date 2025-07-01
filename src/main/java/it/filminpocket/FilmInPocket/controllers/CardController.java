package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.dtos.CreateCardDto;
import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import it.filminpocket.FilmInPocket.mappers.CardMapper; // <-- IMPORTANTE
import it.filminpocket.FilmInPocket.servicies.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private CardMapper cardMapper;

    @GetMapping("/collection")
    public ResponseEntity<List<CardDto>> getMyCardCollection(
            @RequestParam(required = false) Rarity rarity,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String directorName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String cardType,
            Authentication authentication
    ){
        User user= (User) authentication.getPrincipal();
        List<Card> filteredCards=cardService.findUserCardsByFilter(user.getId(),rarity,genre,directorName,year,cardType);
        List<CardDto> cardDto=filteredCards.stream().map(cardMapper::convertToDto).toList();
        return ResponseEntity.ok(cardDto);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // <-- Questo metodo è accessibile solo agli ADMIN
    public ResponseEntity<CardDto> createCard(@RequestBody @Validated CreateCardDto createCardDto) {
        CardDto newCard = cardService.createCard(createCardDto);
        return new ResponseEntity<>(newCard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // <-- Questo metodo è accessibile solo agli ADMIN
    public ResponseEntity<CardDto> updateCard(@PathVariable int id, @RequestBody @Validated CreateCardDto createCardDto) {
        CardDto updatedCard = cardService.updateCard(id, createCardDto);
        return ResponseEntity.ok(updatedCard);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // <-- Questo metodo è accessibile solo agli ADMIN
    public void deleteCard(@PathVariable int id) {
        cardService.deleteCard(id);
    }
}

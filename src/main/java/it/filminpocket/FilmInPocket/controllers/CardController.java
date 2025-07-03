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
    @ResponseStatus(HttpStatus.OK)
    public List<CardDto> getMyCardCollection(
            @RequestParam(required = false) Rarity rarity,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String directorName,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String cardType,
            Authentication authentication
    ){
        User user= (User) authentication.getPrincipal();
        List<Card> filteredCards=cardService.findUserCardsByFilter(user.getId(),rarity,genre,directorName,year,cardType);
        return filteredCards.stream().map(cardMapper::convertToDto).toList();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CardDto getCardById(@PathVariable int id){
        return cardService.findCardById(id);
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDto createCard(@RequestBody @Validated CreateCardDto createCardDto) {
        return cardService.createCard(createCardDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // <-- Questo metodo è accessibile solo agli ADMIN
    public CardDto updateCard(@PathVariable int id, @RequestBody @Validated CreateCardDto createCardDto) {
        return cardService.updateCard(id, createCardDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // <-- Questo metodo è accessibile solo agli ADMIN
    public void deleteCard(@PathVariable int id) {
        cardService.deleteCard(id);
    }
}

package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.CreateDeckDto;
import it.filminpocket.FilmInPocket.dtos.DeckDto;
import it.filminpocket.FilmInPocket.entities.Deck;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.mappers.DeckMapper;
import it.filminpocket.FilmInPocket.servicies.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/me/decks")
public class DeckController {

    @Autowired
    private DeckService deckService;

    @Autowired
    private DeckMapper deckMapper;

    /**
     * Endpoint per recuperare tutti i mazzi dell'utente autenticato.
     */
    @GetMapping
    public List<DeckDto> getUserDecks(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<Deck> decks = deckService.findDecksByUser(currentUser);
        return decks.stream()
                .map(deckMapper::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Endpoint per creare un nuovo mazzo.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeckDto createDeck(@RequestBody @Validated CreateDeckDto createDeckDto, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Deck createdDeck = deckService.createDeck(createDeckDto, currentUser);
        return deckMapper.convertToDto(createdDeck);
    }

    @PutMapping("/{deckId}")
    public DeckDto updateDeck(
            @PathVariable int deckId,
            @RequestBody @Validated CreateDeckDto updateDeckDto, // Riusiamo il DTO di creazione
            Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();
        Deck updatedDeck = deckService.updateDeck(deckId, updateDeckDto, currentUser);
        return deckMapper.convertToDto(updatedDeck);
    }

    @DeleteMapping("/{deckId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeck(@PathVariable int deckId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        deckService.deleteDeck(deckId, currentUser);
    }
}

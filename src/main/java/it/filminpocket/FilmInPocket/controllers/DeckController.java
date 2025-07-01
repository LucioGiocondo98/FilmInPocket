package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.CreateDeckDto;
import it.filminpocket.FilmInPocket.dtos.DeckDto;
import it.filminpocket.FilmInPocket.entities.Deck;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.mappers.DeckMapper;
import it.filminpocket.FilmInPocket.servicies.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/me/decks") // Rotta base per tutte le operazioni sui mazzi dell'utente
public class DeckController {

    @Autowired
    private DeckService deckService;

    @Autowired
    private DeckMapper deckMapper;

    /**
     * Endpoint per recuperare tutti i mazzi dell'utente autenticato.
     */
    @GetMapping
    public ResponseEntity<List<DeckDto>> getUserDecks(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<Deck> decks = deckService.findDecksByUser(currentUser);

        // Converte la lista di entità in una lista di DTO
        List<DeckDto> deckDtos = decks.stream()
                .map(deckMapper::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(deckDtos);
    }

    /**
     * Endpoint per creare un nuovo mazzo.
     */
    @PostMapping
    public ResponseEntity<DeckDto> createDeck(@RequestBody @Validated CreateDeckDto createDeckDto, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Deck createdDeck = deckService.createDeck(createDeckDto, currentUser);
        DeckDto deckDto = deckMapper.convertToDto(createdDeck);
        return new ResponseEntity<>(deckDto, HttpStatus.CREATED);
    }

    @PutMapping("/{deckId}")
    public ResponseEntity<DeckDto> updateDeck(
            @PathVariable int deckId,
            @RequestBody @Validated CreateDeckDto updateDeckDto, // Riusiamo il DTO di creazione
            Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();
        Deck updatedDeck = deckService.updateDeck(deckId, updateDeckDto, currentUser);
        return ResponseEntity.ok(deckMapper.convertToDto(updatedDeck));
    }

    @DeleteMapping("/{deckId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeck(@PathVariable int deckId, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        deckService.deleteDeck(deckId, currentUser);
    }
}

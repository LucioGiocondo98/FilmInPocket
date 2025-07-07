package it.filminpocket.FilmInPocket.controllers;

import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.dtos.CreateCardDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import it.filminpocket.FilmInPocket.mappers.CardMapper;
import it.filminpocket.FilmInPocket.servicies.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private CardMapper cardMapper;


    @GetMapping("/collection")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public Page<CardDto> getMyCardCollection(
            @RequestParam(required = false) Rarity rarity,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String nameContains,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String cardType,
            Authentication authentication,
            Pageable pageable
    ) {
        User user = (User) authentication.getPrincipal();
        return cardService.findUserCardsByFilter(user.getId(),
                Optional.ofNullable(nameContains),
                Optional.ofNullable(rarity),
                Optional.ofNullable(genre),
                Optional.ofNullable(year),
                Optional.ofNullable(cardType),
                pageable
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CardDto getCardById(@PathVariable int id) {
        return cardService.findCardById(id);
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public CardDto createCard(@RequestBody @Validated CreateCardDto createCardDto) throws IOException {
        return cardService.createCard(createCardDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CardDto updateCard(@PathVariable int id, @RequestBody @Validated CreateCardDto createCardDto) throws IOException {
        return cardService.updateCard(id, createCardDto);
    }

    @PutMapping("/{id}/image")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CardDto uploadCardImage(@PathVariable int id, @RequestParam("image") MultipartFile image) throws IOException {
        return cardService.updateCardImage(id, image);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteCard(@PathVariable int id) {
        cardService.deleteCard(id);
    }





}

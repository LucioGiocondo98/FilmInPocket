package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;

import java.util.List;

@Data
public class DeckDto {
    private int id;
    private String name;
    private List<CardDto> cards;
}

/**
 * Quando un utente richiede di vedere i suoi mazzi,
 * il nostro DeckService convertirà ogni entità Deck in un DeckDto, che a sua volta conterrà una lista di CardDto.
 * Questo assicura che non vengano mai esposti dati non necessari.
 */

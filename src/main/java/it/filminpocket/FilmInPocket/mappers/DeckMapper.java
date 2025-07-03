package it.filminpocket.FilmInPocket.mappers;

import it.filminpocket.FilmInPocket.dtos.DeckDto;
import it.filminpocket.FilmInPocket.entities.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
@Component
public class DeckMapper {
    @Autowired
    private CardMapper cardMapper; // Riusiamo il mapper delle carte

    public DeckDto convertToDto(Deck deck) {
        DeckDto dto = new DeckDto();
        dto.setId(deck.getId());
        dto.setName(deck.getName());
        if (deck.getCards() != null) {
            dto.setCards(deck.getCards().stream()
                    .map(cardMapper::convertToDto)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}

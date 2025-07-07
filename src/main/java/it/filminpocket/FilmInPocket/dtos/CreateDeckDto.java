package it.filminpocket.FilmInPocket.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateDeckDto {
    @NotBlank
    @Size(min = 3,max = 50)
    private String name;
    private List<Integer> cardIds;
}

/**
 * Logica di Utilizzo: Il nostro DeckService prenderà questo DTO,
 * controllerà che l'utente possieda effettivamente le carte con gli ID forniti, e poi creerà il nuovo mazzo.
 */

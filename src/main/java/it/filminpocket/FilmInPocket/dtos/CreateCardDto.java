package it.filminpocket.FilmInPocket.dtos;

import it.filminpocket.FilmInPocket.enumerated.Rarity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateCardDto {
    @NotBlank
    private String cardType;
    @NotBlank
    private String name;
    private String description;
    private String imageUrl;
    @NotNull
    private Rarity rarity;

    // Campi MovieCard
    private Integer releaseYear;
    private String directorName;
    private String genre;
    private Integer healthPoints;
    private Integer attackPoints;

    // Campi Director/Actor
    private String bornDate;
    private List<String> filmography;

    // Campi DirectorCard
    private Integer filmAttackBonus;
    private Integer filmHealthBonus;

    // Campi ActorCard
    private Integer opponentDebuffAttack;
    private Integer allyBuffHealth;
}


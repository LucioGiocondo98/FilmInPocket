package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovieCardDto extends CardDto{
    private  int releaseYear;
    private String directorName;
    private String genre;
    private int healthPoints;
    private int attackPoints;
}

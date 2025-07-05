package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rappresenta una carta di tipo Film. Eredita tutti i campi da Card.
 */
@Entity
@DiscriminatorValue("MOVIE")
@Getter
@Setter
@NoArgsConstructor
public class MovieCard extends Card{
    @Column(name = "release_year")
    private int releaseYear;
    @Column(name = "director_name")
    private String directorName;
    private String genre;


    /**
    * Statistiche per parte gameplay
    */
    @Column(name = "health_points")
    private int healthPoints;
    @Column(name = "attack_points")
    private int attackPoints;
}
/**
 * Questa è la carta base del nostro gioco.
 * Oltre ai dati ereditati, aggiunge le statistiche di gioco (healthPoints, attackPoints) e le informazioni specifiche del film come anno, regista e genere.
*/
package it.filminpocket.FilmInPocket.entities;

import it.filminpocket.FilmInPocket.enumerated.Rarity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Superclasse ASTRATTA per tutti i tipi di carte del gioco.
 * Utilizza una strategia di ereditarietà SINGLE_TABLE, quindi tutte le carte
 * (Movie, Director, Actor) verranno salvate in un'unica tabella 'card'.
 */
@Entity
// @Inheritance definisce come salvare la gerarchia di classi. SINGLE_TABLE è la più performante.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// @DiscriminatorColumn crea una colonna speciale per distinguere il tipo di carta (es. 'MOVIE').
@DiscriminatorColumn(name = "card_type")
@Data
public abstract class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rarity rarity;
    @ManyToMany(mappedBy = "collection")
    private Set<User> usersCollection= new HashSet<>();
}
/**
* A cosa serve?
* Card definisce tutte le caratteristiche che ogni singola carta del gioco deve avere, non importa se è un film, un regista o un attore.
 *Questo ci evita di ripetere gli stessi campi (id, name, description, etc.) in ogni classe di carta.
- * Ereditarietà SINGLE_TABLE: Abbiamo scelto questa strategia perché è molto efficiente.
 * Invece di avere una tabella movie_card, una director_card, etc., avremo un'unica, grande tabella card.
 * Dentro questa tabella, la colonna card_type (definita da @DiscriminatorColumn)
 * ci dirà se la riga specifica è un "MOVIE", un "DIRECTOR" o un "ACTOR".
 */
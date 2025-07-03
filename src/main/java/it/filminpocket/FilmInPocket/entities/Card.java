package it.filminpocket.FilmInPocket.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "usersCollection")
@EqualsAndHashCode(exclude = "usersCollection")
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
    @JsonBackReference
    private Set<User> usersCollection = new HashSet<>();

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
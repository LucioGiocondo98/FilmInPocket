package it.filminpocket.FilmInPocket.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "decks")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"user", "cards"})
@EqualsAndHashCode(exclude = {"user", "cards"})
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(name = "decks_cards",
            joinColumns = @JoinColumn(name = "deck_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    @JsonBackReference
    private List<Card> cards;


}
/**
 * Spiegazione: Deck è l'entità che fa da "ponte".
 * È collegata a un singolo User (il proprietario) e a una lista di Card (le carte che compongono il mazzo).
 * Le annotazioni @ManyToOne e @ManyToMany qui definiscono il cuore della struttura del tuo gioco.
 */
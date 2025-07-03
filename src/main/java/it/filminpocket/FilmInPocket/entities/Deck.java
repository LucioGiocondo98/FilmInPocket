package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "decks")
@NoArgsConstructor
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
    private List<Card> cards;

    // --- GETTERS E SETTERS MANUALI ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public List<Card> getCards() { return cards; }
    public void setCards(List<Card> cards) { this.cards = cards; }
}
/**
 * Spiegazione: Deck è l'entità che fa da "ponte".
 * È collegata a un singolo User (il proprietario) e a una lista di Card (le carte che compongono il mazzo).
 * Le annotazioni @ManyToOne e @ManyToMany qui definiscono il cuore della struttura del tuo gioco.
 */
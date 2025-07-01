package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
/**
 * Rappresenta un mazzo di carte creato da un utente.
 */
@Entity
@Table(name = "decks")
@Data
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;



    /**
     * Relazione Molti-a-Uno con User.
     * @JoinColumn crea la colonna 'user_id' in questa tabella 'decks',
     * che conterrà l'ID dell'utente proprietario.
     * 'nullable = false' assicura che un mazzo debba SEMPRE appartenere a un utente.
     */
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;


    /**
     * Relazione Molti-a-Molti con Card. Un mazzo contiene molte carte,
     * e la stessa carta può essere in molti mazzi diversi.
     */
    @ManyToMany
    @JoinTable(name = "decks_cards",joinColumns = @JoinColumn(name = "deck_id"),inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List<Card> cards;
}
/**
 * Spiegazione: Deck è l'entità che fa da "ponte".
 * È collegata a un singolo User (il proprietario) e a una lista di Card (le carte che compongono il mazzo).
 * Le annotazioni @ManyToOne e @ManyToMany qui definiscono il cuore della struttura del tuo gioco.
 */
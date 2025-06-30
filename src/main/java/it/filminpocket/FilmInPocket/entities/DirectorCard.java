package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
/**
 * Rappresenta una carta di tipo Regista. Funziona come una "Carta Terreno".
 */
@Entity
@DiscriminatorValue("DIRECTOR")
@Data
@EqualsAndHashCode(callSuper = true)
public class DirectorCard extends Card{
    @Column(name = "born_date")
    private LocalDate bornDate;
    // @ElementCollection è usata per salvare una lista di valori semplici (qui, String).
    // JPA creerà una tabella separata `card_filmography` per contenere questa lista.
    @ElementCollection
    @CollectionTable(name = "card_filmography",joinColumns = @JoinColumn(name ="card_id" ))
    @Column(name = "film_title")
    private List<String> filmography;

    //campi per gamplay
    @Column(name = "film_attack_bonus")
    private int filmAttackBonus;
    @Column(name = "film_health_bonus")
    private int filmHealthBonus;
}

/**
 * Spiegazione: Questa carta ha un ruolo strategico.
 * Non combatte direttamente, ma fornisce bonus passivi a tutte le carte film dello stesso regista che si trovano in gioco.
 * filmography è una List<String> gestita elegantemente da @ElementCollection.
 */

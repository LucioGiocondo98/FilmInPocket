package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
/**
 * Rappresenta una carta di tipo Regista. Funziona come una "Carta Terreno".
 */
@Entity
@DiscriminatorValue("DIRECTOR")
@Getter
@Setter
@NoArgsConstructor
public class DirectorCard extends PersonCard{

    @Column(name = "film_attack_bonus")
    private int filmAttackBonus;
    @Column(name = "film_health_bonus")
    private int filmHealthBonus;
}

/**
 * Spiegazione: Questa carta ha un ruolo strategico.
 * Non combatte direttamente, ma fornisce bonus passivi a tutte le carte film dello stesso regista che si trovano in gioco.
 */

package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
/**
 * Rappresenta una carta di tipo Attore.
 * Funziona come una "Carta Magia Equipaggiamento".
 */
@Entity
@DiscriminatorValue("ACTOR")
@Data
@EqualsAndHashCode(callSuper = true)
public class ActorCard extends PersonCard{

    @Column(name = "opponent_debuff_attack")
    private int opponentDebuffAttack;
    @Column(name = "ally_buff_health")
    private int allyBuffHealth;

}

/**
 * Spiegazione: Questa è una carta tattica "usa e getta".
 * Quando viene giocata, applica un bonus o un malus a una singola carta bersaglio, per poi essere scartata.
 */

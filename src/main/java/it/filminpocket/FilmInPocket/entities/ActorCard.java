package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Rappresenta una carta di tipo Attore.
 * Funziona come una "Carta Magia Equipaggiamento".
 */
@Entity
@DiscriminatorValue("ACTOR")
@Getter
@Setter
@NoArgsConstructor
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

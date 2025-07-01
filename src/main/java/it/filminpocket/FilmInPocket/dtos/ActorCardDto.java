package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActorCardDto extends CardDto{
    private String bornDate;
    private int opponentDebuffAttack;
    private int allyBuffHealth;
}

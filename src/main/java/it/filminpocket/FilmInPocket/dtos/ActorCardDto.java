package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActorCardDto extends CardDto{
    private String bornDate;
    private List<String> filmography;

    private int opponentDebuffAttack;
    private int allyBuffHealth;
}

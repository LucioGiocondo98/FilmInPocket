package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DirectorCardDto extends CardDto{
    private String bornDate;
    private int filmAttackBonus;
    private int filmHealthBonus;
}

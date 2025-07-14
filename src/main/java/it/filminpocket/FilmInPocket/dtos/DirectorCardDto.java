package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DirectorCardDto extends CardDto{
    private String bornDate;
    private List<String> filmography;
    private int filmAttackBonus;
    private int filmHealthBonus;
}

package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;

@Data
public class CardDto {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private String rarity;
    private String cardType;
}

package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class AdminUserViewDto {
private int id;
private String username;
private String email;
private int filmTickets;
private ZonedDateTime nextRechargeTime;
private List<CardDto> cardDtoList;
private List<DeckDto> deckDtoList;
}

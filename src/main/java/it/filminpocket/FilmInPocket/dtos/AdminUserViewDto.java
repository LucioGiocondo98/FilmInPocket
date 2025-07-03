package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminUserViewDto {
private int id;
private String username;
private String email;
private int filmTickets;
private LocalDateTime nextRechargeTime;
private List<CardDto> cardDtoList;
private List<DeckDto> deckDtoList;
}

package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private int id;
    private String username;
    private String email;
    private int filmTickets;
    private LocalDateTime nextTicketRechargeTime;
}

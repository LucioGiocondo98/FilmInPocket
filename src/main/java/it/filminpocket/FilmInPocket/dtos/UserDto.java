package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;

@Data
public class UserDto {
    private int id;
    private String username;
    private String email;
    private int filmTickets;
}

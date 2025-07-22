package it.filminpocket.FilmInPocket.dtos;

import it.filminpocket.FilmInPocket.enumerated.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String email;
    private int filmTickets;
    private LocalDateTime nextTicketRechargeTime;
    private UserRole role;
    private String imageUrl;
}

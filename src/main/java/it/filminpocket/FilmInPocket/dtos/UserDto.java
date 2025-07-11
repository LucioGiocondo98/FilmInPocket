package it.filminpocket.FilmInPocket.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDto {
    private int id;
    private String username;
    private String email;
    private int filmTickets;
    private LocalDateTime nextTicketRechargeTime;
}

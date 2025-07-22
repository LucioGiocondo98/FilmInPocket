package it.filminpocket.FilmInPocket.mappers;

import it.filminpocket.FilmInPocket.dtos.UserDto;
import it.filminpocket.FilmInPocket.entities.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public UserDto toDto(User user) {
        LocalDateTime nextRecharge = user.getLastTicketRecharge().plusHours(12);
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFilmTickets(),
                nextRecharge,
                user.getRole(),
                user.getImageUrl()
        );
    }
}

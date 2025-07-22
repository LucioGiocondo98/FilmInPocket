package it.filminpocket.FilmInPocket.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateUserDto {
    private String email;
    private String password;
}

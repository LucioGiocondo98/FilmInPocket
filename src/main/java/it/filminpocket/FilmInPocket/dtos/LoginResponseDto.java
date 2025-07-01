package it.filminpocket.FilmInPocket.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String accessToken;
    private UserDto user;
}

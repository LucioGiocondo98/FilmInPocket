package it.filminpocket.FilmInPocket.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private LocalDateTime timestamp;
}

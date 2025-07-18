package it.filminpocket.FilmInPocket.mappers;

import it.filminpocket.FilmInPocket.dtos.ActorCardDto;
import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.dtos.DirectorCardDto;
import it.filminpocket.FilmInPocket.dtos.MovieCardDto;
import it.filminpocket.FilmInPocket.entities.ActorCard;
import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.entities.DirectorCard;
import it.filminpocket.FilmInPocket.entities.MovieCard;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardMapper {
    public CardDto convertToDto(Card card) {
        if (card instanceof MovieCard movie) {
            MovieCardDto movieCardDto = new MovieCardDto();
            movieCardDto.setReleaseYear(movie.getReleaseYear());
            movieCardDto.setDirectorName(movie.getDirectorName());
            movieCardDto.setGenre(movie.getGenre());
//            movieCardDto.setAttackPoints(movie.getAttackPoints());
//            movieCardDto.setHealthPoints(movie.getHealthPoints());
            copyBaseCardProperties(card, movieCardDto);
            return movieCardDto;
        } else if (card instanceof DirectorCard director) {
            DirectorCardDto directorCardDto = new DirectorCardDto();
            directorCardDto.setBornDate(director.getBornDate().toString());
//            directorCardDto.setFilmHealthBonus(director.getFilmHealthBonus());
//            directorCardDto.setFilmAttackBonus(director.getFilmAttackBonus());
            directorCardDto.setFilmography(
                    director.getFilmography() != null ? director.getFilmography() : List.of()
            );
            copyBaseCardProperties(card, directorCardDto);
            return directorCardDto;
        } else if (card instanceof ActorCard actor) {
            ActorCardDto actorCardDto = new ActorCardDto();
            actorCardDto.setBornDate(actor.getBornDate().toString());
//            actorCardDto.setOpponentDebuffAttack(actor.getOpponentDebuffAttack());
//            actorCardDto.setAllyBuffHealth(actor.getAllyBuffHealth());
            actorCardDto.setFilmography(
                    actor.getFilmography() != null ? actor.getFilmography() : List.of()
            );
            copyBaseCardProperties(card, actorCardDto);
            return actorCardDto;
        }

        throw new IllegalArgumentException("Tipo di carta non supportata: " + card.getClass().getName());
    }

    private void copyBaseCardProperties(Card card, CardDto cardDto) {
        cardDto.setId(card.getId());
        cardDto.setName(card.getName());
        cardDto.setDescription(card.getDescription());
        cardDto.setImageUrl(card.getImageUrl());
        cardDto.setRarity(card.getRarity().name());
        cardDto.setCardType(card.getClass().getSimpleName().replace("Card", ""));
    }
}

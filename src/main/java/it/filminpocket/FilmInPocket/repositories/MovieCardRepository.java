package it.filminpocket.FilmInPocket.repositories;

import it.filminpocket.FilmInPocket.entities.MovieCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MovieCardRepository extends JpaRepository<MovieCard,Integer> {
   List<MovieCard> findByGenre(String genre);
    List<MovieCard> findByDirectorName(String directorName);
    List<MovieCard> findByReleaseYear(int year);
}

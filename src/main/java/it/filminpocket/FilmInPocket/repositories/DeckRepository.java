package it.filminpocket.FilmInPocket.repositories;

import it.filminpocket.FilmInPocket.entities.Deck;
import it.filminpocket.FilmInPocket.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends JpaRepository<Deck,Integer> {
    List<Deck> findByUser(User user);
}

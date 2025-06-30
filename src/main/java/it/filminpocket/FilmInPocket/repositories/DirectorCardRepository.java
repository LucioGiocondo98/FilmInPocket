package it.filminpocket.FilmInPocket.repositories;

import it.filminpocket.FilmInPocket.entities.DirectorCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectorCardRepository extends JpaRepository<DirectorCard,Integer> {
}

package it.filminpocket.FilmInPocket.repositories;

import it.filminpocket.FilmInPocket.entities.ActorCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorCardRepository extends JpaRepository<ActorCard,Integer> {
}

package it.filminpocket.FilmInPocket.repositories;

import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository è un'annotazione che marca questa interfaccia come un componente Spring
// e ne traduce le eccezioni del database in eccezioni Spring più gestibili.
@Repository
public interface CardRepository extends JpaRepository<Card,Integer>, JpaSpecificationExecutor<Card> {
    List<Card> findByRarity(Rarity rarity);
}

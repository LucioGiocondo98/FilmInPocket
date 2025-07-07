package it.filminpocket.FilmInPocket.repositories;

import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("SELECT c FROM User u JOIN u.collection c WHERE u.id = :userId AND c.id = :cardId")
    Optional<Card> findCardInUserCollection(@Param("userId") int userId, @Param("cardId") int cardId);

}

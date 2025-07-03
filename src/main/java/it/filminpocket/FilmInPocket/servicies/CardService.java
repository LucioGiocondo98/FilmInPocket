package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.dtos.CreateCardDto;
import it.filminpocket.FilmInPocket.entities.*;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.mappers.CardMapper;
import it.filminpocket.FilmInPocket.repositories.CardRepository;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
public class CardService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardMapper cardMapper;


    public CardDto findCardById(int id){
        Card card= cardRepository.findById(id).orElseThrow(()->new NotFoundException("Carta non trovata"));
        return cardMapper.convertToDto(card);
    }

    public List<Card> findUserCardsByFilter(
            int userId, Rarity rarity,String genre,String directorName,Integer year,String cardTypeName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con ID: " + userId));
        Set<Card> allUserCards = user.getCollection();

        Stream<Card> cardStream = allUserCards.stream();

        if (rarity != null) {
            cardStream = cardStream.filter(card -> card.getRarity() == rarity);
        }
        if (cardTypeName != null && !cardTypeName.isBlank()) {
            cardStream = cardStream.filter(card ->
                    card.getClass().getSimpleName().equalsIgnoreCase(cardTypeName.trim() + "Card")
            );
        }
        if (genre != null && !genre.isBlank()) {
            cardStream = cardStream.filter(card ->
                    card instanceof MovieCard && genre.equalsIgnoreCase(((MovieCard) card).getGenre())
            );
        }
        if (directorName != null && !directorName.isBlank()) {
            cardStream = cardStream.filter(card ->
                    card instanceof MovieCard && directorName.equalsIgnoreCase(((MovieCard) card).getDirectorName())
            );
        }
        if (year != null) {
            cardStream = cardStream.filter(card ->
                    card instanceof MovieCard && year.equals(((MovieCard) card).getReleaseYear())
            );
        }

        return cardStream.toList();
    }

    /**
     * Crea una nuova carta nel database.
     * La logica determina che tipo di carta creare basandosi sul campo 'cardType' del DTO.
     */
    public CardDto createCard(CreateCardDto dto) {
        Card card;
        switch (dto.getCardType().toUpperCase()) {
            case "MOVIE":
                MovieCard movie = new MovieCard();
                movie.setReleaseYear(dto.getReleaseYear());
                movie.setDirectorName(dto.getDirectorName());
                movie.setGenre(dto.getGenre());
                movie.setHealthPoints(dto.getHealthPoints());
                movie.setAttackPoints(dto.getAttackPoints());
                card = movie;
                break;
            case "DIRECTOR":
                DirectorCard director = new DirectorCard();
                director.setBornDate(LocalDate.parse(dto.getBornDate()));
                director.setFilmography(dto.getFilmography());
                director.setFilmAttackBonus(dto.getFilmAttackBonus());
                director.setFilmHealthBonus(dto.getFilmHealthBonus());
                card = director;
                break;
            case "ACTOR":
                ActorCard actor = new ActorCard();
                actor.setBornDate(LocalDate.parse(dto.getBornDate()));
                actor.setFilmography(dto.getFilmography());
                actor.setOpponentDebuffAttack(dto.getOpponentDebuffAttack());
                actor.setAllyBuffHealth(dto.getAllyBuffHealth());
                card = actor;
                break;
            default:
                throw new IllegalArgumentException("Tipo di carta non valido: " + dto.getCardType());
        }
        card.setName(dto.getName());
        card.setDescription(dto.getDescription());
        card.setImageUrl(dto.getImageUrl());
        card.setRarity(dto.getRarity());
        Card savedCard = cardRepository.save(card);
        return cardMapper.convertToDto(savedCard);
    }


    /**
     * Aggiorna una carta esistente.
     * La logica è simile alla creazione, ma parte da un'entità esistente.
     */
    public CardDto updateCard(int id, CreateCardDto dto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata con ID: " + id));
        card.setName(dto.getName());
        card.setDescription(dto.getDescription());
        card.setImageUrl(dto.getImageUrl());
        card.setRarity(dto.getRarity());

        if (card instanceof MovieCard movie && dto.getCardType().equalsIgnoreCase("MOVIE")) {
            movie.setReleaseYear(dto.getReleaseYear());
            movie.setDirectorName(dto.getDirectorName());
            movie.setGenre(dto.getGenre());
            movie.setHealthPoints(dto.getHealthPoints());
            movie.setAttackPoints(dto.getAttackPoints());
        } else if (card instanceof DirectorCard director && dto.getCardType().equalsIgnoreCase("DIRECTOR")) {
            director.setBornDate(LocalDate.parse(dto.getBornDate()));
            director.setFilmography(dto.getFilmography());
            director.setFilmAttackBonus(dto.getFilmAttackBonus());
            director.setFilmHealthBonus(dto.getFilmHealthBonus());
        } else if (card instanceof ActorCard actor && dto.getCardType().equalsIgnoreCase("ACTOR")) {
            actor.setBornDate(LocalDate.parse(dto.getBornDate()));
            actor.setFilmography(dto.getFilmography());
            actor.setOpponentDebuffAttack(dto.getOpponentDebuffAttack());
            actor.setAllyBuffHealth(dto.getAllyBuffHealth());
        }

        Card updatedCard = cardRepository.save(card);
        return cardMapper.convertToDto(updatedCard);
    }

    /**
     * Elimina una carta dal database.
     */
    public void deleteCard(int id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata con ID: " + id));
        cardRepository.delete(card);
    }
}

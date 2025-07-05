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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardMapper cardMapper;

    /**
     * Metodo con paginazione e filtri dinamici.
     */
    public Page<CardDto> findUserCardsByFilter(int userId, Rarity rarity, String genre, String directorName, Integer year, String cardType, Pageable pageable) {
        // Specifica di base: le carte devono appartenere all'utente.
        Specification<Card> spec = (root, query, cb) -> root.join("usersCollection").get("id").in(userId);

        if (rarity != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("rarity"), rarity));
        }
        if (cardType != null && !cardType.isBlank()) {
            spec = spec.and((root, query, cb) -> {
                try {
                    Class<?> type = Class.forName("it.filminpocket.FilmInPocket.entities." + cardType.trim() + "Card");
                    return cb.equal(root.type(), type);
                } catch (ClassNotFoundException e) {
                    return cb.disjunction();
                }
            });
        }
        if (genre != null && !genre.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.treat(root, MovieCard.class).get("genre"), genre));
        }
        if (directorName != null && !directorName.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.treat(root, MovieCard.class).get("directorName"), directorName));
        }
        if (year != null) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.treat(root, MovieCard.class).get("releaseYear"), year));
        }

        Page<Card> cardsPage = cardRepository.findAll(spec, pageable);
        return cardsPage.map(cardMapper::convertToDto);
    }

    public CardDto findCardById(int id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata con ID: " + id));
        return cardMapper.convertToDto(card);
    }
    public Page<CardDto> getAllCards(Pageable pageable) {
        Page<Card> cardsPage = cardRepository.findAll(pageable);
        return cardsPage.map(cardMapper::convertToDto);
    }

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

    public void deleteCard(int id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata con ID: " + id));
        cardRepository.delete(card);
    }


    public Page<CardDto> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable).map(cardMapper::convertToDto);
    }
}


package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.dtos.CreateCardDto;
import it.filminpocket.FilmInPocket.entities.*;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import it.filminpocket.FilmInPocket.exceptions.BadRequestException;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.mappers.CardMapper;
import it.filminpocket.FilmInPocket.repositories.CardRepository;
import it.filminpocket.FilmInPocket.repositories.DeckRepository;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private ImageUploadService imageUploadService;
    @Autowired
    private DeckRepository deckRepository;

    /**
     * Metodo con paginazione e filtri dinamici.
     */
    public Page<CardDto> findUserCardsByFilter(
            int userId,
            Optional<String> nameContains,
            Optional<Rarity> rarity,
            Optional<String> genre,
            Optional<Integer> year,
            Optional<String> cardType,
            Pageable pageable
    ) {
        // Specifica di base: le carte devono appartenere all'utente.
        Specification<Card> spec = (root, query, cb) -> root.join("usersCollection").get("id").in(userId);

        if (rarity.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("rarity"), rarity.get()));
        }

        if (genre.isPresent() && !genre.get().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.treat(root, MovieCard.class).get("genre"), genre.get()));
        }

        if (year.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.treat(root, MovieCard.class).get("releaseYear"), year.get()));
        }

        if (nameContains.isPresent() && !nameContains.get().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + nameContains.get().toLowerCase() + "%"));
        }
        if (cardType.isPresent() && !cardType.get().isBlank()) {
            String type = cardType.get().toUpperCase();
            spec = spec.and((root, query, cb) -> {
                switch (type) {
                    case "MOVIE":
                        return cb.equal(root.type(), MovieCard.class);
                    case "ACTOR":
                        return cb.equal(root.type(), ActorCard.class);
                    case "DIRECTOR":
                        return cb.equal(root.type(), DirectorCard.class);
                    default:
                        return cb.conjunction(); // se tipo non valido, non applica nulla
                }
            });
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


    public CardDto createCard(CreateCardDto dto) throws IOException {
        if (cardRepository.existsByName(dto.getName())) {
            throw new BadRequestException("Esiste già una card con il nome: " + dto.getName());
        }
        Card card;
        switch (dto.getCardType().toUpperCase()) {
            case "MOVIE":
                MovieCard movie = new MovieCard();
                movie.setReleaseYear(dto.getReleaseYear());
                movie.setDirectorName(dto.getDirectorName());
                movie.setGenre(dto.getGenre());
              //  movie.setHealthPoints(dto.getHealthPoints() != null ? dto.getHealthPoints() : 0);
                // movie.setAttackPoints(dto.getAttackPoints() != null ? dto.getAttackPoints() : 0);
                card = movie;
                break;
            case "DIRECTOR":
                DirectorCard director = new DirectorCard();
                director.setBornDate(LocalDate.parse(dto.getBornDate()));
                director.setFilmography(dto.getFilmography());
//                director.setFilmAttackBonus(dto.getFilmAttackBonus()!= null ? dto.getFilmAttackBonus():0);
//                director.setFilmHealthBonus(dto.getFilmHealthBonus()!= null ? dto.getFilmHealthBonus():0);
                card = director;
                break;
            case "ACTOR":
                ActorCard actor = new ActorCard();
                actor.setBornDate(LocalDate.parse(dto.getBornDate()));
                actor.setFilmography(dto.getFilmography());
//                actor.setOpponentDebuffAttack(dto.getOpponentDebuffAttack()!= null ? dto.getOpponentDebuffAttack():0);
//                actor.setAllyBuffHealth(dto.getAllyBuffHealth()!= null ? dto.getAllyBuffHealth():0);
                card = actor;
                break;
            default:
                throw new IllegalArgumentException("Tipo di carta non valido: " + dto.getCardType());
        }
        card.setName(dto.getName());
        card.setDescription(dto.getDescription());
        card.setRarity(dto.getRarity());
        card.setImageUrl(null);
        Card savedCard = cardRepository.save(card);
        return cardMapper.convertToDto(savedCard);
    }


    public CardDto updateCard(int id, CreateCardDto dto) throws IOException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata con ID: " + id));
        card.setName(dto.getName());
        card.setDescription(dto.getDescription());
        card.setRarity(dto.getRarity());
        if (dto.getImageUrl() != null && !dto.getImageUrl().isBlank()) {
            card.setImageUrl(dto.getImageUrl());
        }
        if (card instanceof MovieCard movie && dto.getCardType().equalsIgnoreCase("MOVIE")) {
            movie.setReleaseYear(dto.getReleaseYear());
            movie.setDirectorName(dto.getDirectorName());
            movie.setGenre(dto.getGenre());
//            movie.setHealthPoints(dto.getHealthPoints());
//            movie.setAttackPoints(dto.getAttackPoints());
        } else if (card instanceof DirectorCard director && dto.getCardType().equalsIgnoreCase("DIRECTOR")) {
            director.setBornDate(LocalDate.parse(dto.getBornDate()));
            director.setFilmography(dto.getFilmography());
//            director.setFilmAttackBonus(dto.getFilmAttackBonus());
//            director.setFilmHealthBonus(dto.getFilmHealthBonus());
        } else if (card instanceof ActorCard actor && dto.getCardType().equalsIgnoreCase("ACTOR")) {
            actor.setBornDate(LocalDate.parse(dto.getBornDate()));
            actor.setFilmography(dto.getFilmography());
//            actor.setOpponentDebuffAttack(dto.getOpponentDebuffAttack());
//            actor.setAllyBuffHealth(dto.getAllyBuffHealth());
        }

        Card updatedCard = cardRepository.save(card);
        return cardMapper.convertToDto(updatedCard);
    }


    @Transactional
    public void deleteCard(int id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata con ID: " + id));

        // 1. Rimuovi la card da tutti gli utenti che la possiedono
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getCollection().removeIf(c -> Objects.equals(c.getId(), id))) {
                userRepository.save(user);
            }
        }

        // 2. Rimuovi la card da tutti i mazzi che la includono
        List<Deck> decks = deckRepository.findAll();
        for (Deck deck : decks) {
            if (deck.getCards().removeIf(c -> Objects.equals(c.getId(), id))) {
                deckRepository.save(deck);
            }
        }

        // 3. Elimina definitivamente la card
        cardRepository.delete(card);
    }


    public Page<CardDto> findAll(Pageable pageable) {
        return cardRepository.findAll(pageable).map(cardMapper::convertToDto);
    }

    public CardDto updateCardImage(int id, MultipartFile image) throws IOException {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Carta non trovata con ID: " + id));

        if (image != null && !image.isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(image);
            card.setImageUrl(imageUrl);
            cardRepository.save(card);
        } else {
            throw new BadRequestException("File immagine mancante o vuoto.");
        }

        return cardMapper.convertToDto(card);
    }


    public Page<CardDto> findAllWithFilters(
            Optional<String> nameContains,
            Optional<Rarity> rarity,
            Optional<String> genre,
            Optional<Integer> year,
            Optional<String> cardType,
            Pageable pageable
    ) {
        Specification<Card> spec = Specification.where(null);

        if (rarity.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("rarity"), rarity.get()));
        }

        if (genre.isPresent() && !genre.get().isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.treat(root, MovieCard.class).get("genre"), genre.get()));
        }

        if (year.isPresent()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.treat(root, MovieCard.class).get("releaseYear"), year.get()));
        }

        if (nameContains.isPresent() && !nameContains.get().isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("name")), "%" + nameContains.get().toLowerCase() + "%"));
        }
        if (cardType.isPresent() && !cardType.get().isBlank()) {
            String type = cardType.get().toUpperCase();
            spec = spec.and((root, query, cb) -> {
                switch (type) {
                    case "MOVIE":
                        return cb.equal(root.type(), MovieCard.class);
                    case "ACTOR":
                        return cb.equal(root.type(), ActorCard.class);
                    case "DIRECTOR":
                        return cb.equal(root.type(), DirectorCard.class);
                    default:
                        return cb.conjunction();
                }
            });
        }
        return cardRepository.findAll(spec, pageable).map(cardMapper::convertToDto);
    }
}


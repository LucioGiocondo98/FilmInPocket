package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.CreateDeckDto;
import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.entities.Deck;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.exceptions.UnauthorizedException;
import it.filminpocket.FilmInPocket.repositories.CardRepository;
import it.filminpocket.FilmInPocket.repositories.DeckRepository;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Deck> findDecksByUser(User user){
        return deckRepository.findByUser(user);
    }

    /**
    *Creo mazzo
     */
    public Deck createDeck(CreateDeckDto createDeckDto, User user) {
        Deck newDeck = new Deck();
        System.out.println("Nome del mazzo: " + createDeckDto.getName());
        System.out.println("Card IDs ricevuti: " + createDeckDto.getCardIds());
        newDeck.setName(createDeckDto.getName());
        newDeck.setUser(user);
        updateDeckCards(newDeck, createDeckDto.getCardIds(), user);
        return deckRepository.save(newDeck);
    }

    /**
     * Aggiorna un mazzo esistente dell'utente.
     */
    public Deck updateDeck(int deckId, CreateDeckDto updateDeckDto, User user) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new NotFoundException("Mazzo non trovato con ID: " + deckId));

        if (deck.getUser().getId() != user.getId()) {
            throw new UnauthorizedException("Non hai il permesso di modificare questo mazzo.");
        }

        if (updateDeckDto.getName() != null && !updateDeckDto.getName().isBlank()) {
            deck.setName(updateDeckDto.getName());
        }

        if (updateDeckDto.getCardIds() != null) {
            updateDeckCards(deck, updateDeckDto.getCardIds(), user);
        }

        return deckRepository.save(deck);
    }

    /**
     * Verifica che tutte le carte appartengano all’utente e aggiorna le carte del mazzo.
     */
    private void updateDeckCards(Deck deck, List<Integer> cardIds, User user) {
        if (cardIds == null || cardIds.isEmpty()) {
            deck.setCards(List.of());
            return;
        }

        List<Card> requestedCards = cardRepository.findAllById(cardIds);

        if (requestedCards.size() != cardIds.size()) {
            throw new NotFoundException("Alcune carte richieste non sono state trovate.");
        }


        User userWithCollection = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));
        userWithCollection.getCollection().size();

        List<Integer> userCollectionIds = userWithCollection.getCollection().stream()
                .map(Card::getId)
                .toList();

        for (Card card : requestedCards) {
            if (!userCollectionIds.contains(card.getId())) {
                throw new NotFoundException("L'utente non possiede la carta con ID: " + card.getId());
            }
        }

        deck.setCards(requestedCards);
    }

    /**
     * Elimina un mazzo se appartiene all’utente.
     */
    public void deleteDeck(int deckId, User user) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new NotFoundException("Mazzo non trovato con ID: " + deckId));

        if (deck.getUser().getId() != user.getId()) {
            throw new UnauthorizedException("Non hai il permesso di eliminare questo mazzo.");
        }

        deckRepository.delete(deck);
    }
}

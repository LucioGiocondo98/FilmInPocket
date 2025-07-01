package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.CreateDeckDto;
import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.entities.Deck;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.repositories.CardRepository;
import it.filminpocket.FilmInPocket.repositories.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeckService {
    @Autowired
    private DeckRepository deckRepository;
    @Autowired
    private CardRepository cardRepository;

    public List<Deck> findDecksByUser(User user){
        return deckRepository.findByUser(user);
    }

    /**
    *Creo mazzo
     */
    public Deck createDeck(CreateDeckDto createDeckDto,User user){
        Deck newDeck= new Deck();
        newDeck.setName(createDeckDto.getName());
        newDeck.setUser(user);
        if (createDeckDto.getCardIds()!=null && createDeckDto.getCardIds().isEmpty()){
            List<Card> requestedCards= cardRepository.findAllById(createDeckDto.getCardIds());
            List<Integer> userCollectionIds= user.getCollection().stream().map(Card::getId).collect(Collectors.toList());
            for (Card card:requestedCards){
                if (!userCollectionIds.contains(card.getId())){
                    throw new NotFoundException("L'utente non possiede la carta con ID: "+ card.getId());
                }
            }
            newDeck.setCards(requestedCards);
        }
        return deckRepository.save(newDeck);
    }


    /**
     * Modifica un mazzo esistente.
     */
    public Deck updateDeck(int deckId, CreateDeckDto updateDeckDto, User user) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Mazzo non trovato con ID: " + deckId));
        if (deck.getUser().getId() != user.getId()) {
            throw new SecurityException("Non hai il permesso di modificare questo mazzo.");
        }

        deck.setName(updateDeckDto.getName());
        return deckRepository.save(deck);
    }


    /**
     * Elimina un mazzo.
     */
    public void deleteDeck(int deckId, User user) {
        Deck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new RuntimeException("Mazzo non trovato con ID: " + deckId));
        if (deck.getUser().getId() != user.getId()) {
            throw new SecurityException("Non hai il permesso di eliminare questo mazzo.");
        }
        deckRepository.delete(deck);
    }
}

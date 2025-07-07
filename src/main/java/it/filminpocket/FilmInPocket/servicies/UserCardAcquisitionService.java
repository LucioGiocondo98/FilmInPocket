package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.CardDto;
import it.filminpocket.FilmInPocket.entities.Card;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import it.filminpocket.FilmInPocket.exceptions.BadRequestException;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.mappers.CardMapper;
import it.filminpocket.FilmInPocket.repositories.CardRepository;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class UserCardAcquisitionService {
    private static final int TICKETS_PER_PACK = 1;
    private static final int CARDS_PER_PACK = 5;
    private static final int MAX_TICKETS = 2;
    public static final int RECHARGE_INTERVAL_HOURS = 12;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    private final Random random = new Random();

    /**
     * Permette a un utente di acquisire un pacchetto di 5 carte casuali consumando un filmTicket.
     * La selezione delle carte è basata sulla rarità.
     *
     * @param userId L'ID dell'utente che sta acquisendo le carte.
     * @return Una lista di DTO delle carte acquisite (5 carte).
     * // @throws NotFoundException se l'utente non è trovato.
     * // @throws BadRequestException se l'utente non ha abbastanza filmTickets o non ci sono carte disponibili.
     */

    @Transactional
    public List<CardDto> acquireRandomCardPack(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato con ID: " + userId));

        if (user.getFilmTickets() < TICKETS_PER_PACK) {
            throw new BadRequestException("Non hai abbastanza filmTickets per acquisire un nuovo pack.");
        }
        user.setFilmTickets(user.getFilmTickets() - TICKETS_PER_PACK);
        List<Card> allAvailableCards = cardRepository.findAll();
        if (allAvailableCards.isEmpty()) {
            throw new BadRequestException("Nessuna carta disponibile per l'acquisto");
        }
        List<CardDto> acquiredCards = new ArrayList<>();
        for (int i = 0; i < CARDS_PER_PACK; i++) {
            Card selectedCard = selectRandomCardByRarity(allAvailableCards);
           user.getCollection().add(selectedCard);
            acquiredCards.add(cardMapper.convertToDto(selectedCard));
        }
        userRepository.save(user);
        return acquiredCards;
    }

    /**
     * Seleziona una carta casuale da una lista, con una probabilità basata sulla rarità.
     * // @param allCards Tutte le carte disponibili.
     * // @return Una carta selezionata.
     */

    private Card selectRandomCardByRarity(List<Card> allCards) {
        int randomNumber = random.nextInt(100);
        Rarity cardRarity;

        if (randomNumber < 70) {
            cardRarity = Rarity.COMMON;
        } else if (randomNumber < 95) {
            cardRarity = Rarity.RARE;
        } else {
            cardRarity = Rarity.EPIC;
        }
        List<Card> cardsOfCardRarity = allCards.stream().filter(card -> card.getRarity() == cardRarity).collect(Collectors.toList());
        if (!cardsOfCardRarity.isEmpty()) {
            return cardsOfCardRarity.get(random.nextInt(cardsOfCardRarity.size()));
        } else {
            return allCards.get(random.nextInt(allCards.size()));
        }
    }

    /**
     * Logica per ricaricare i filmTickets dell'utente ogni 12 ore fino a un massimo di 2.
     * Questo metodo è progettato per essere chiamato da un task schedulato per tutti gli utenti.
     * //@param user L'utente per cui ricaricare i ticket.
     * Utilizziamo i long per evitare overflow laddove i periodi di utilizzo siano mesi,anni ecc...
     */

    @Transactional
    public void rechargeFilmTicketsForUser(User user) {
        if (user.getLastTicketRecharge() == null) {
            user.setLastTicketRecharge(LocalDateTime.now());
            userRepository.save(user);
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastRechargeTicket = user.getLastTicketRecharge();
        long hoursPassed = Duration.between(lastRechargeTicket, now).toHours();
        long periodPassed = hoursPassed / RECHARGE_INTERVAL_HOURS;
        if (periodPassed > 0) {
            int ticketsAdded = 0;
            for (int i = 0; i < periodPassed; i++) {
                if (user.getFilmTickets() < MAX_TICKETS) {
                    user.setFilmTickets(user.getFilmTickets() + 1);
                    ticketsAdded++;
                } else {
                    break;
                }
            }
            user.setLastTicketRecharge(lastRechargeTicket.plusHours(periodPassed * RECHARGE_INTERVAL_HOURS));
            if (ticketsAdded > 0 || !lastRechargeTicket.equals(user.getLastTicketRecharge())) {
                userRepository.save(user);
            }
        }
    }
    /**
     * Metodo pubblico per essere chiamato da un task schedulato.
     * Itera su tutti gli utenti in database e tenta di ricaricare i loro ticket.
     */
    @Transactional
    public void performScheduledTicketRechargeForAllUsers() {
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            rechargeFilmTicketsForUser(user);
        }
    }
}

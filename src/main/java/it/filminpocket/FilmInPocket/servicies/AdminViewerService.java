package it.filminpocket.FilmInPocket.servicies;

import it.filminpocket.FilmInPocket.dtos.AdminUserViewDto;
import it.filminpocket.FilmInPocket.dtos.DeckDto;
import it.filminpocket.FilmInPocket.entities.User;
import it.filminpocket.FilmInPocket.exceptions.NotFoundException;
import it.filminpocket.FilmInPocket.mappers.CardMapper;
import it.filminpocket.FilmInPocket.mappers.DeckMapper;
import it.filminpocket.FilmInPocket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminViewerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private DeckMapper deckMapper;

    public Page<AdminUserViewDto> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(this::mapUserToAdminViewDto);
    }

    public AdminUserViewDto getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utente con id=" + id + " non trovato."));
        return mapUserToAdminViewDto(user);
    }

    private AdminUserViewDto mapUserToAdminViewDto(User user) {
        AdminUserViewDto dto = new AdminUserViewDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFilmTickets(user.getFilmTickets());
        if (user.getLastTicketRecharge() != null) {
            dto.setNextRechargeTime(user.getLastTicketRecharge().plusHours(UserCardAcquisitionService.RECHARGE_INTERVAL_HOURS));
        }

//        List<CardDto> collectionDto = user.getCollection().stream()
//                .map(cardMapper::convertToDto)
//                .collect(Collectors.toList());
//        dto.setCardDtoList(collectionDto);

        List<DeckDto> decksDto = user.getDecks().stream()
                .map(deckMapper::convertToDto)
                .collect(Collectors.toList());
        dto.setDeckDtoList(decksDto);

        return dto;
    }
}
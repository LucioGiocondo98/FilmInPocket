package it.filminpocket.FilmInPocket.entities;

import it.filminpocket.FilmInPocket.enumerated.UserRole;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
/**
 * Rappresenta un utente registrato al gioco.
 */
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,unique = true)
    private String username;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    private int filmTickets;
    private LocalDateTime lastTicketRecharge;
    private LocalDateTime createdAt;

    // --- RELAZIONI ---

    // Relazione Molti-a-Molti con Card per la collezione.
    // @JoinTable crea la tabella intermedia 'user_collection'
    // che collega l'ID dell'utente all'ID della carta.
    @ManyToMany
    @JoinTable(name = "user_collection",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "card_id"))
    private List <Card> collection;

    // Relazione Uno-a-Molti con Deck.
    // 'mappedBy = "user"' dice a JPA che questa relazione è già "mappata" (gestita)
    // dal campo 'user' nella classe Deck. È fondamentale per evitare tabelle extra.
    // 'cascade = CascadeType.ALL' significa: se elimino un utente, elimina anche tutti i suoi mazzi.
    // 'orphanRemoval = true' significa: se rimuovo un mazzo dalla lista di un utente, eliminalo dal DB
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Deck> decks;

    // Questo metodo viene eseguito AUTOMATICAMENTE prima di salvare un nuovo utente per la prima volta.
    // È perfetto per impostare valori di default.
    @PrePersist
    protected void onCreate(){
        createdAt=LocalDateTime.now();
        filmTickets=2;
        role=UserRole.ROLE_USER;
    }
}

/**
 * Spiegazione: User contiene non solo i dati di login, ma anche le due relazioni più importanti: collection (una lista di Card che l'utente possiede) e decks (una lista dei Deck che ha creato).
 * L'uso di cascade e @PrePersist mostra una gestione avanzata e pulita del ciclo di vita dei dati.
 */

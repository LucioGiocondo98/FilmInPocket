package it.filminpocket.FilmInPocket.entities;

import it.filminpocket.FilmInPocket.enumerated.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Rappresenta un utente registrato al gioco.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {
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

    /** Relazione Molti-a-Molti con Card per la collezione.
    // @JoinTable crea la tabella intermedia 'user_collection'
    // che collega l'ID dell'utente all'ID della carta.
    */
   // @ManyToMany
    //@JoinTable(name = "user_collection",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "card_id"))
    //private Set<Card> collection=new HashSet<>();

    /**Relazione Uno-a-Molti con Deck.
    // 'mappedBy = "user"' dice a JPA che questa relazione è già "mappata" (gestita)
    // dal campo 'user' nella classe Deck. È fondamentale per evitare tabelle extra.
    // 'cascade = CascadeType.ALL' significa: se elimino un utente, elimina anche tutti i suoi mazzi.
    // 'orphanRemoval = true' significa: se rimuovo un mazzo dalla lista di un utente, eliminalo dal DB
     */
    //@OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    //private List<Deck> decks= new ArrayList<>();


    /**
     *  Questo metodo viene eseguito AUTOMATICAMENTE prima di salvare un nuovo utente per la prima volta.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.filmTickets == 0) {
            this.filmTickets = 2;
        }
        if (this.lastTicketRecharge == null) {
            this.lastTicketRecharge = LocalDateTime.now();
        }
        if (this.role == null) {
            this.role = UserRole.ROLE_USER;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

/**
 * Spiegazione: User contiene non solo i dati di login, ma anche le due relazioni più importanti: collection (una lista di Card che l'utente possiede) e decks (una lista dei Deck che ha creato).
 * L'uso di cascade e @PrePersist mostra una gestione avanzata e pulita del ciclo di vita dei dati.
 */

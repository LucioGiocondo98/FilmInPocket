package it.filminpocket.FilmInPocket.entities;

import it.filminpocket.FilmInPocket.enumerated.UserRole;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private int filmTickets;
    private LocalDateTime lastTicketRecharge;
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_collection",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    private Set<Card> collection = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deck> decks = new ArrayList<>();

    // --- GETTERS E SETTERS MANUALI ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
    public int getFilmTickets() { return filmTickets; }
    public void setFilmTickets(int filmTickets) { this.filmTickets = filmTickets; }
    public LocalDateTime getLastTicketRecharge() { return lastTicketRecharge; }
    public void setLastTicketRecharge(LocalDateTime lastTicketRecharge) { this.lastTicketRecharge = lastTicketRecharge; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Set<Card> getCollection() { return collection; }
    public void setCollection(Set<Card> collection) { this.collection = collection; }
    public List<Deck> getDecks() { return decks; }
    public void setDecks(List<Deck> decks) { this.decks = decks; }

    // --- METODI UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    @PrePersist
    protected void onCreate() {
        if(this.createdAt == null) this.createdAt = LocalDateTime.now();
        if(this.filmTickets == 0) this.filmTickets = 2;
        if(this.lastTicketRecharge == null) this.lastTicketRecharge = LocalDateTime.now();
        if(this.role == null) this.role = UserRole.ROLE_USER;
    }
}
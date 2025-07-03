package it.filminpocket.FilmInPocket.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.filminpocket.FilmInPocket.enumerated.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"collection", "decks"})
@EqualsAndHashCode(exclude = {"collection", "decks"})
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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_collection",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id"))
    @JsonManagedReference
    private Set<Card> collection = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Deck> decks = new ArrayList<>();

    // Metodi UserDetails
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
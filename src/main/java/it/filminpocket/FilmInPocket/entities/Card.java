package it.filminpocket.FilmInPocket.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.filminpocket.FilmInPocket.enumerated.Rarity;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "card_type")
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "usersCollection")
@EqualsAndHashCode(exclude = "usersCollection")
public abstract class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rarity rarity;

    @ManyToMany(mappedBy = "collection", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<User> usersCollection = new HashSet<>();
}
package it.filminpocket.FilmInPocket.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PersonCard extends Card {

    @Column(name = "born_date")
    private LocalDate bornDate;


    /**
     // @ElementCollection è usata per salvare una lista di valori semplici (qui, String).
     // JPA creerà una tabella separata `card_filmography` per contenere questa lista.
     */
    @ElementCollection
    @CollectionTable(name = "card_filmography", joinColumns = @JoinColumn(name = "card_id"))
    @Column(name = "film_title")
    private List<String> filmography;
}

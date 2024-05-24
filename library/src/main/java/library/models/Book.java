package library.models;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.*;


@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {
    @javax.persistence.Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title", nullable = false)
    @Pattern(regexp = "[А-ЯЁа-яё\\s\\-\\.]+", message = "Некорректный ввод")
    private String title;

    @Column(name = "author", nullable = false)
    @Pattern(regexp = "[А-ЯЁа-яё\\s\\-\\.]+")
    private String author;

    @Column(name = "year", nullable = false)
    @Min(value = 1)
    private int publicationYear;

    @Column(name = "publishing_house", nullable = false)
    @Pattern(regexp = "[А-ЯЁа-яё\\s\\-\\.]+")
    private String publishingHouse;

    @Column(name = "number_of_copies", nullable = false)
    @Min(value = 1)
    private int numberOfCopies;
    @Transient
    private int availableCopies;

    @ManyToMany
    @JoinTable(
            name = "book_person",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> owners = new ArrayList<>();

    @Transient
    private boolean isExpired;

    public Book() {
    }

    public Book(String title, String author, int publicationYear, String publishingHouse, int numberOfCopies) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.publishingHouse = publishingHouse;
        this.numberOfCopies = numberOfCopies;
        this.availableCopies = numberOfCopies;
    }

    public void addOwner(Person person) {
        owners.add(person);
        availableCopies--;
    }

    public void removeOwner(Person person) {
        owners.remove(person);
        availableCopies++;
    }

    public int getAvailableCopies() {
        return numberOfCopies - owners.size();
    }
}

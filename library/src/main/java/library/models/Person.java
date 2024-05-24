package library.models;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@Setter
@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false)
    @Size(min = 2, max = 60)
    @Pattern(regexp = "^[А-ЯЁ][а-яё]*([- ][А-ЯЁ][а-яё]*){0,2}( [А-ЯЁ][а-яё]*)?$")
    private String fullName;

    @Column(name = "year_of_birth", nullable = false)
    @Min(value = 1900)
    private int birthYear;

    @ManyToMany(mappedBy = "owners")
    private List<Book> books = new ArrayList<>();

    public Person() {
    }

    public boolean hasBook(Book book) {
        return books.contains(book);
    }

    public Person(String fullName, int birthYear) {
        this.fullName = fullName;
        this.birthYear = birthYear;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }
}

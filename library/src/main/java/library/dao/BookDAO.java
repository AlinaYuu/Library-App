package library.dao;

import library.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@Component
public class BookDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void assign(int bookId, int personId) {
        String request = "INSERT INTO book_person (book_id, person_id, borrow_date) VALUES (?, ?, ?)";
        jdbcTemplate.update(request, bookId, personId, LocalDate.now());
    }

    @Transactional
    public LocalDate getBorrowDateForBook(int bookId) {
        String request = "SELECT borrow_date FROM book_person WHERE book_id = ?";
        return jdbcTemplate.queryForObject(request, LocalDate.class, bookId);
    }

    public Optional<Person> findOwnersOfTheBook(int bookId) {
        String request = "SELECT * FROM person WHERE id IN (SELECT person_id FROM book_person WHERE book_id = ?)";
        return jdbcTemplate.query(request, new Object[]{bookId}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }
}

package library.repositories;

import library.models.Book;
import library.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findAllByFullName(String fullName);
    @Query("SELECT p.books FROM Person p WHERE p.id = :personId")
    List<Book> findBooksByPersonId(@Param("personId") int personId);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO book_person (book_id, person_id, borrow_date) VALUES (?1, ?2, CURRENT_DATE)", nativeQuery = true)
    void addBookToPerson(int bookId, int personId);
}

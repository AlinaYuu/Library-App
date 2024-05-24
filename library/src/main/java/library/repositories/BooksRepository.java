package library.repositories;

import library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleStartingWith(String title);

    Optional<Book> getBookByTitleAndAuthorAndPublicationYearAndPublishingHouse
            (String title, String author, int publicationYear, String publishingHouse);
}

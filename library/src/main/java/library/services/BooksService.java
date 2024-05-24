package library.services;

import library.dao.BookDAO;
import library.models.Book;
import library.models.Person;
import library.repositories.BooksRepository;
import library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;
    private final BookDAO bookDAO;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository, BookDAO bookDAO) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
        this.bookDAO = bookDAO;
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear) {
            return booksRepository.findAll(Sort.by("publicationYear"));
        } else {
            return booksRepository.findAll();
        }
    }

    public Book findById(int id) {
        Optional<Book> bookOptional = booksRepository.findById(id);
        return bookOptional.orElse(null);
    }

    public List<Book> findByTitleStartingWith(String title) {
        return booksRepository.findByTitleStartingWith(title);
    }

    public List<Book> findWithPagination(Integer page, int booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("publicationYear"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Optional<Person> findPeopleByBookOwners(int bookId) {
        return bookDAO.findOwnersOfTheBook(bookId);
    }

    @Transactional
    public void release(int bookId, int personId) {
        Optional<Book> bookOptional = booksRepository.findById(bookId);
        Optional<Person> personOptional = peopleRepository.findById(personId);

        if (bookOptional.isPresent() && personOptional.isPresent()) {
            Book book = bookOptional.get();
            Person person = personOptional.get();

            book.removeOwner(person);
            person.removeBook(book);

            booksRepository.save(book);
            peopleRepository.save(person);
        }
    }

    @Transactional
    public void assign(int bookId, int personId) {
        bookDAO.assign(bookId, personId);
    }

    public void isExpired(int bookId) {
        LocalDate borrowDate = bookDAO.getBorrowDateForBook(bookId);
        if (borrowDate != null) {
            LocalDate now = LocalDate.now();
            // вычисляем количество дней между датой взятия и текущей датой
            long duration = borrowDate.until(now).getDays();
            findById(bookId).setExpired(duration > 10);
        }
    }

    public List<Book> findBooksByPersonId(int personId) {
        List<Book> booksList = peopleRepository.findBooksByPersonId(personId);
        for (Book book : booksList) {
            isExpired(book.getId());
        }
        return booksList;
    }

    public Optional<Book> getBookByTitleAndAuthorAndPublicationYearAndPublishingHouse(
            String title, String author, int publicationYear, String publishingHouse) {
        return booksRepository.getBookByTitleAndAuthorAndPublicationYearAndPublishingHouse(
                title, author, publicationYear, publishingHouse);
    }

}




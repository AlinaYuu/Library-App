package library.util;

import library.models.Book;
import library.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class BookValidator implements Validator {
    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Book book = (Book) o;

        Optional<Book> repeatBook = booksService.getBookByTitleAndAuthorAndPublicationYearAndPublishingHouse(
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getPublishingHouse());

        if (repeatBook.isPresent()) {
            errors.rejectValue("title", "", "Такая книга уже добавлена");
        }
    }

}

package library.controllers;

import library.models.Book;
import library.models.Person;
import library.services.BooksService;
import library.services.PeopleService;
import library.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BookController {
    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BooksService bookService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = bookService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }

    @GetMapping
    public String list(Model model,
                       @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                       @RequestParam(value = "books_per_page", required = false, defaultValue = "10") Integer booksPerPage,
                       @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") Boolean sortByYear) {
        List<Book> books;
        if (page != null && booksPerPage != null) {
            books = booksService.findWithPagination(page, booksPerPage, sortByYear);
        } else {
            books = booksService.findAll(sortByYear);
        }
        model.addAttribute("books", books);
        return "books/list";
    }


    @GetMapping("/{id}")
    public String show(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findById(id));
        model.addAttribute("people", peopleService.findAll());
        Optional<Person> owners = booksService.findPeopleByBookOwners(id);
        if (owners != null)
            model.addAttribute("owners", owners);
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book Book) {
        return "books/new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findById(id));
        return "books/edit";
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "books/edit";

        booksService.update(id, book);
        return "redirect:/books";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchPage() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model,
                             @RequestParam("query") String query) {
        model.addAttribute("books", booksService.findByTitleStartingWith(query));
        return "books/search";
    }

    @PostMapping("/assign/{id}")
    public String assign(@PathVariable("id") int bookId,
                         @RequestParam("personId") int personId) {
        booksService.assign(bookId, personId);
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/release/{id}")
    public String release(@PathVariable("id") int bookId, @RequestParam("personId") int personId) {
        booksService.release(bookId, personId);
        return "redirect:/books/" + bookId;
    }
}

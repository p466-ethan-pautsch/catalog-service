package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> get(@RequestParam(required = false) String author) {
        if (author != null) {
            return bookService.viewBooksByAuthor(author);
        }

        return bookService.viewBookList();
    }

    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable String isbn) {
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book post(@Valid @RequestBody Book book) {
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("{isbn}")
    public ResponseEntity<Book> put(@PathVariable String isbn, @Valid @RequestBody Book book) {
        boolean exists = false;

        try {
            bookService.viewBookDetails(isbn);
            exists = true;
        } catch (BookNotFoundException ex) {
            // A missing book will be created.
        }

        Book savedBook = bookService.editBookDetails(isbn, book);

        if (exists) {
            return ResponseEntity.ok(savedBook);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }
}
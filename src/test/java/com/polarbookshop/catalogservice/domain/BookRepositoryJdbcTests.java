package com.polarbookshop.catalogservice.domain;

import java.util.Optional;

import com.polarbookshop.catalogservice.config.DataConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
class BookRepositoryJdbcTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Test
    void findBookByIsbnWhenExisting() {
        var isbn = "1234561237";
        var book = Book.of(isbn, "Title", "Author", 12.90);

        jdbcAggregateTemplate.insert(book);

        Optional<Book> actual = bookRepository.findByIsbn(isbn);

        assertThat(actual).isPresent();
        assertThat(actual.get().isbn()).isEqualTo(isbn);
    }

    @Test
    void findBookByIsbnWhenNotExisting() {
        assertThat(bookRepository.findByIsbn("1234561238")).isEmpty();
    }

    @Test
    void findAllBooks() {
        var book1 = Book.of("1234567891", "Book One", "Author One", 10.00);
        var book2 = Book.of("1234567892", "Book Two", "Author Two", 15.00);

        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        var books = bookRepository.findAll();

        assertThat(books)
                .extracting(Book::isbn)
                .contains("1234567891", "1234567892");
    }

    @Test
    void existsByIsbnWhenExisting() {
        var book = Book.of("1234567893", "Title", "Author", 12.90);
        jdbcAggregateTemplate.insert(book);

        assertThat(bookRepository.existsByIsbn("1234567893")).isTrue();
    }

    @Test
    void existsByIsbnWhenNotExisting() {
        assertThat(bookRepository.existsByIsbn("1234567894")).isFalse();
    }

    @Test
    void deleteByIsbnWhenExisting() {
        var book = Book.of("1234567895", "Title", "Author", 12.90);
        jdbcAggregateTemplate.insert(book);

        bookRepository.deleteByIsbn("1234567895");

        assertThat(bookRepository.existsByIsbn("1234567895")).isFalse();
    }

    @Test
    void createdDateIsSetWhenSaving() {
        var book = Book.of("1234567896", "Title", "Author", 12.90);

        var savedBook = bookRepository.save(book);

        assertThat(savedBook.createdDate()).isNotNull();
    }

    @Test
    void findBooksByAuthor() {
        var book1 = Book.of("1111111111", "Book One", "Lewis", 12.90);
        var book2 = Book.of("2222222222", "Book Two", "Lewis", 14.90);
        var book3 = Book.of("3333333333", "Book Three", "Tolkien", 19.90);

        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);
        jdbcAggregateTemplate.insert(book3);

        var books = bookRepository.findAllByAuthor("Lewis");

        assertThat(books)
                .extracting(Book::isbn)
                .contains("1111111111", "2222222222")
                .doesNotContain("3333333333");
    }
}
package com.polarbookshop.catalogservice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookJsonTests {

    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws IOException {
        var now = Instant.parse("2026-10-01T10:15:30Z");
        var book = new Book(
                394L,
                "1234567890",
                "Title",
                "Author",
                "Penguin Books",
                9.90,
                now,
                now,
                21
        );

        var content = json.write(book);

        assertThat(content)
                .extractingJsonPathNumberValue("@.id")
                .isEqualTo(394);

        assertThat(content)
                .extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.isbn());

        assertThat(content)
                .extractingJsonPathStringValue("@.title")
                .isEqualTo(book.title());

        assertThat(content)
                .extractingJsonPathStringValue("@.author")
                .isEqualTo(book.author());

        assertThat(content)
                .extractingJsonPathStringValue("@.publisher")
                .isEqualTo(book.publisher());

        assertThat(content)
                .extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.price());

        assertThat(content)
                .extractingJsonPathStringValue("@.createdDate")
                .isEqualTo(now.toString());

        assertThat(content)
                .extractingJsonPathStringValue("@.lastModifiedDate")
                .isEqualTo(now.toString());

        assertThat(content)
                .extractingJsonPathNumberValue("@.version")
                .isEqualTo(21);
    }

    @Test
    void testDeserialize() throws IOException {
        var content = """
                {
                    "id": 394,
                    "isbn": "1234567890",
                    "title": "Title",
                    "author": "Author",
                    "publisher": "Penguin Books",
                    "price": 9.90,
                    "createdDate": "2026-10-01T10:15:30Z",
                    "lastModifiedDate": "2026-10-01T10:15:30Z",
                    "version": 21
                }
                """;

        var instant = Instant.parse("2026-10-01T10:15:30Z");

        assertThat(json.parse(content))
                .isEqualTo(new Book(
                        394L,
                        "1234567890",
                        "Title",
                        "Author",
                        "Penguin Books",
                        9.90,
                        instant,
                        instant,
                        21
                ));
    }
}
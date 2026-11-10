package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration")
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expected = Book.of("1231231231", "Title", "Author", 9.90);

		webTestClient
				.post().uri("/books")
				.bodyValue(expected)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actual -> {
					assertThat(actual).isNotNull();
					assertThat(actual.isbn()).isEqualTo(expected.isbn());
				});
	}

	@Test
	void whenBookCreatedUpdatedAndDeletedThenBookNoLongerExists() {
		String isbn = "9876543210";

		var originalBook = Book.of(isbn, "Original Title", "Test Author", 9.90);
		var updatedBook = Book.of(isbn, "Updated Title", "Test Author", 14.99);

		// POST - Create the book (201)
		webTestClient
				.post().uri("/books")
				.bodyValue(originalBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class)
				.value(actual -> {
					assertThat(actual.isbn()).isEqualTo(originalBook.isbn());
					assertThat(actual.title()).isEqualTo(originalBook.title());
					assertThat(actual.id()).isNotNull();
				});

		// GET - Verify the book exists (200)
		webTestClient
				.get().uri("/books/{isbn}", isbn)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.value(actual -> {
					assertThat(actual.isbn()).isEqualTo(originalBook.isbn());
					assertThat(actual.title()).isEqualTo(originalBook.title());
					assertThat(actual.id()).isNotNull();
				});

		// PUT - Update the book (200)
		webTestClient
				.put().uri("/books/{isbn}", isbn)
				.bodyValue(updatedBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.value(actual -> {
					assertThat(actual.isbn()).isEqualTo(updatedBook.isbn());
					assertThat(actual.title()).isEqualTo(updatedBook.title());
					assertThat(actual.price()).isEqualTo(updatedBook.price());
				});

		// DELETE - Remove the book (204)
		webTestClient
				.delete().uri("/books/{isbn}", isbn)
				.exchange()
				.expectStatus().isNoContent();

		// GET - Verify the book no longer exists (404)
		webTestClient
				.get().uri("/books/{isbn}", isbn)
				.exchange()
				.expectStatus().isNotFound();
	}
}
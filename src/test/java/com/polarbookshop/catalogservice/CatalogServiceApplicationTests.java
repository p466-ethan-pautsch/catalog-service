package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {
		var expected = new Book("1231231231", "Title", "Author", 9.90);

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

		var originalBook = new Book(isbn, "Original Title", "Test Author", 9.90);
		var updatedBook = new Book(isbn, "Updated Title", "Test Author", 14.99);

		// POST - Create the book (201)
		webTestClient
				.post().uri("/books")
				.bodyValue(originalBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class)
				.isEqualTo(originalBook);

		// GET - Verify the book exists (200)
		webTestClient
				.get().uri("/books/{isbn}", isbn)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.isEqualTo(originalBook);

		// PUT - Update the book (200)
		webTestClient
				.put().uri("/books/{isbn}", isbn)
				.bodyValue(updatedBook)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class)
				.isEqualTo(updatedBook);

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
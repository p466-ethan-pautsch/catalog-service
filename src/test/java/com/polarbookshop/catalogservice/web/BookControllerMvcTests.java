package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.domain.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.BookService;
import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception {
        String isbn = "7373731394";
        given(bookService.viewBookDetails(isbn))
                .willThrow(BookNotFoundException.class);

        mockMvc.perform(get("/books/" + isbn))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenPostBookAlreadyExistsThenShouldReturn422() throws Exception {
        given(bookService.addBookToCatalog(any()))
                .willThrow(new BookAlreadyExistsException("1234567890"));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "1234567890",
                                  "title": "Title",
                                  "author": "Author",
                                  "price": 9.90
                                }
                                """))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void whenPostInvalidBookThenShouldReturn400() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "isbn": "invalid",
                                  "title": "",
                                  "author": "Author",
                                  "price": 9.90
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenPutExistingBookThenShouldReturn200() throws Exception {
        String isbn = "1234567890";
        var existingBook = new Book(isbn, "Original", "Author", 9.90);
        var updatedBook = new Book(isbn, "Updated", "Author", 12.50);

        given(bookService.viewBookDetails(isbn)).willReturn(existingBook);
        given(bookService.editBookDetails(any(), any())).willReturn(updatedBook);

        mockMvc.perform(put("/books/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "isbn": "1234567890",
                              "title": "Updated",
                              "author": "Author",
                              "price": 12.50
                            }
                            """))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutNewBookThenShouldReturn201() throws Exception {
        String isbn = "1234567899";
        var newBook = new Book(isbn, "New Book", "Author", 9.90);

        given(bookService.viewBookDetails(isbn))
                .willThrow(new BookNotFoundException(isbn));
        given(bookService.editBookDetails(any(), any())).willReturn(newBook);

        mockMvc.perform(put("/books/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "isbn": "1234567899",
                              "title": "New Book",
                              "author": "Author",
                              "price": 9.90
                            }
                            """))
                .andExpect(status().isCreated());
    }
}
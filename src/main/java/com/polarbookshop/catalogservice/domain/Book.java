package com.polarbookshop.catalogservice.domain;

public record Book(
        String isbn,        // unique identifier
        String title,
        String author,
        Double price
) {}
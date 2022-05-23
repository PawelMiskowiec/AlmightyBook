package com.example.almightybook.catalog.application.port;

import com.example.almightybook.catalog.domain.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.*;

public interface CatalogUseCase {
    List<Book> findByTitle(String title);

    Optional<Book> findById(Long id);

    public Optional<Book> findOneByTitle(String title);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    List<Book> findByTitleAndAuthor(String title, String author);

    Book addBook(CreateBookCommand command);

    void removeById(Long id);

    UpdateBookResponse updateBook(UpdateBookCommand command);

    void updateBookCover(UpdateBookCoverCommand command);

    void removeBookCover(Long id);

    @Value
    class UpdateBookCoverCommand{
        Long id;
        byte[] file;
        String contentType;
        String filename;
    }

    @Value
    class CreateBookCommand{
        String title;
        Set<Long> authors;
        Integer year;
        BigDecimal price;
        Long available;
    }

    @Value
    @Builder
    @AllArgsConstructor
    class UpdateBookCommand{
        Long id;
        String title;
        Set<Long>  authors;
        Integer year;
        BigDecimal price;
    }

    @Value
    class UpdateBookResponse{
        public static UpdateBookResponse SUCCESS = new UpdateBookResponse(true, emptyList());
        boolean success;
        List<String> errors;
    }

}

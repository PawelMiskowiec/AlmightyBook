package com.example.almightybook.catalog.web;

import com.example.almightybook.catalog.application.port.CatalogUseCase;
import com.example.almightybook.catalog.application.port.CatalogUseCase.CreateBookCommand;
import com.example.almightybook.catalog.db.AuthorJpaRepository;
import com.example.almightybook.catalog.domain.Author;
import com.example.almightybook.catalog.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CatalogControllerIT {

    @Autowired
    AuthorJpaRepository authorJpaRepository;

    @Autowired
    CatalogUseCase catalogUseCase;

    @Autowired
    CatalogController controller;

    @Test
    public void getAllBooks(){
        //given
        Author goetz = authorJpaRepository.save(new Author("Brain Goetz"));
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2005,
                new BigDecimal("99.90"),
                50L
        ));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java Concurrency in Practice",
                Set.of(goetz.getId()),
                2006,
                new BigDecimal("129.90"),
                50L
        ));

        //when
        List<Book> all = controller.getAll(Optional.empty(), Optional.empty());

        //then
        assertEquals(2, all.size());
    }

    @Test
    public void getBooksByAuthor(){
        //given
        givenEffectiveJava();
        givenJavaConcurrencyInPractice();
        //when
        List<Book> all = controller.getAll(Optional.empty(), Optional.of("Bloch"));

        //then
        assertEquals(1, all.size());
        assertEquals("Effective Java", all.get(0).getTitle());
    }

    @Test
    public void getBooksByTitle(){
        //given
        givenEffectiveJava();
        givenJavaConcurrencyInPractice();
        //when
        List<Book> all = controller.getAll(Optional.of("Effective"), Optional.empty());

        //then
        assertEquals(1, all.size());
        assertEquals("Effective Java", all.get(0).getTitle());
    }

    private void givenJavaConcurrencyInPractice() {
        Author goetz = authorJpaRepository.save(new Author("Brain Goetz"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Java Concurrency in Practice",
                Set.of(goetz.getId()),
                2006,
                new BigDecimal("129.90"),
                50L
        ));
    }

    private void givenEffectiveJava() {
        Author bloch = authorJpaRepository.save(new Author("Joshua Bloch"));
        catalogUseCase.addBook(new CreateBookCommand(
                "Effective Java",
                Set.of(bloch.getId()),
                2005,
                new BigDecimal("99.90"),
                50L
        ));
    }
}
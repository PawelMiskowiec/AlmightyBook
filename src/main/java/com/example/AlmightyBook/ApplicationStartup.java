package com.example.AlmightyBook;

import com.example.AlmightyBook.application.CatalogController;
import com.example.AlmightyBook.domain.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
class ApplicationStartup implements CommandLineRunner {

    private final CatalogController catalogController;

    @Override
    public void run(String... args) throws Exception{
        List<Book> books = catalogController.findByTitle("Harry");
        books.forEach(System.out::println);
    }
}

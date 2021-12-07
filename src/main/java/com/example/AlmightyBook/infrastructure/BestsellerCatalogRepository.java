package com.example.AlmightyBook.infrastructure;

import com.example.AlmightyBook.domain.Book;
import com.example.AlmightyBook.domain.CatalogRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
class BestsellerCatalogRepository implements CatalogRepository {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();

    public BestsellerCatalogRepository() {
        storage.put(1L, new Book(1L, "Harry Potter i Komnata Tajemnic", "JK Rowling", 1998));
        storage.put(2L, new Book(2L, "Władca Pierścieni: Dwie Wieże", "JRR Tolkien", 1954));
        storage.put(2L, new Book(3L, "Mężczyźni, którzy nienawidzą kobiet", "Sieg Larsson", 2005));
        storage.put(2L, new Book(3L, "Sezon Burz", "Andrzej Sapkowski", 2013));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }
}

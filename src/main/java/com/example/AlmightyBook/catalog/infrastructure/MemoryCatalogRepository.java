package com.example.AlmightyBook.catalog.infrastructure;

import com.example.AlmightyBook.catalog.domain.CatalogRepository;
import com.example.AlmightyBook.catalog.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryCatalogRepository implements CatalogRepository {
    private final Map<Long, Book> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(0L);

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(Book book) {
        if(book.getId() != null){
            storage.put(book.getId(), book);
        } else{
            long nextId = nextID();
            book.setId(nextId);
            storage.put(nextId, book);
        }

    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void removeById(long id) {
        storage.remove(id);
    }

    private long nextID() {
        return ID_NEXT_VALUE.getAndIncrement();
    }
}

package com.example.AlmightyBook.catalog.db;

import com.example.AlmightyBook.catalog.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookJpaRepository extends JpaRepository<Book, Long> {

    @Query("SELECT DISTINCT b FROM Book b JOIN FETCH b.authors")
    List<Book> findAllEager();

    List<Book> findByTitleStartsWithIgnoreCase(String title);

    Optional<Book> findDistinctFirstByTitleStartsWithIgnoreCase(String title);

    @Query( " SELECT b FROM Book b JOIN b.authors a " +
            " WHERE " +
            " lower(b.title) LIKE lower(concat('%', :title, '%')) " +
            " AND lower(a.firstName) LIKE lower(concat('%', :author, '%') ) " +
            " OR lower(a.lastName) LIKE lower(concat('%', :author, '%'))"
    )
    List<Book> findByTitleAndAuthor(@Param("title") String title, @Param("author") String author);

    @Query(" SELECT b FROM Book b JOIN b.authors a " +
            " WHERE " +
            " lower(a.firstName) LIKE lower(concat('%', :name, '%')) " +
            " OR lower(a.lastName) LIKE lower(concat('%', :name, '%')) "
    )
    List<Book> findByAuthor(@Param("name") String name);
}
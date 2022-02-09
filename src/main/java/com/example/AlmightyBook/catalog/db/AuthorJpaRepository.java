package com.example.AlmightyBook.catalog.db;

import com.example.AlmightyBook.catalog.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorJpaRepository extends JpaRepository<Author, Long> {
}

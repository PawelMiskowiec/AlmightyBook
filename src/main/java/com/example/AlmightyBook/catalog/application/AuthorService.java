package com.example.almightybook.catalog.application;

import com.example.almightybook.catalog.application.port.AuthorUseCase;
import com.example.almightybook.catalog.db.AuthorJpaRepository;
import com.example.almightybook.catalog.domain.Author;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@AllArgsConstructor
@Service
public class AuthorService implements AuthorUseCase {
    private final AuthorJpaRepository authorRepository;

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    @Transactional
    public Author addAuthor(CreateAuthorCommand command) {
        Author author = toAuthor(command);
        authorRepository.save(author);
        return author;
    }

    private Author toAuthor(CreateAuthorCommand command) {
        return new Author(command.getName());
    }
}

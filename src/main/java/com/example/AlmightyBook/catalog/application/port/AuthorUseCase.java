package com.example.almightybook.catalog.application.port;

import com.example.almightybook.catalog.domain.Author;
import lombok.Value;

import java.util.List;

public interface AuthorUseCase {
    List<Author> findAll();

    Author addAuthor(CreateAuthorCommand command);

    @Value
    class CreateAuthorCommand{
        private String name;
    }
}

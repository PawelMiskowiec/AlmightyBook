package com.example.AlmightyBook.catalog.application.port;

import com.example.AlmightyBook.catalog.domain.Author;
import lombok.Value;

import java.util.List;

public interface AuthorUseCase {
    List<Author> findAll();

    Author addAuthor(CreateAuthorCommand command);

    @Value
    class CreateAuthorCommand{
        private String firstName;
        private String lastName;
    }
}

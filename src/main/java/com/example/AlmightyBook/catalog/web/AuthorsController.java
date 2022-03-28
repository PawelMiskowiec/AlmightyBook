package com.example.AlmightyBook.catalog.web;

import com.example.AlmightyBook.catalog.application.port.AuthorUseCase;
import com.example.AlmightyBook.catalog.application.port.AuthorUseCase.CreateAuthorCommand;
import com.example.AlmightyBook.catalog.domain.Author;
import com.example.AlmightyBook.catalog.domain.Book;
import com.example.AlmightyBook.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/authors")
public class AuthorsController {
    private final AuthorUseCase authors;

    @GetMapping
    public List<Author> findAll(){
        return authors.findAll();
    }

    @PostMapping
    public ResponseEntity<Void> addAuthor(@RequestBody @Validated RestAuthorCommand command){
        Author author = authors.addAuthor(command.toCreateCommand());
        URI uri = createdAuthorUri(author);
        return ResponseEntity.created(uri).build();
    }

    private URI createdAuthorUri(Author author) {
        return new CreatedURI("/" + author.getId().toString()).uri();
    }

    @Data
    private static class RestAuthorCommand{
        @NotBlank
        private String name;
        private Set<Book> books = new HashSet<>();
        CreateAuthorCommand toCreateCommand(){ return new CreateAuthorCommand(name); }
    }
}

package com.example.almightybook.catalog.domain;

import com.example.almightybook.jpa.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@ToString
public class Author extends BaseEntity {
    private String name;

    @ManyToMany(mappedBy = "authors", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("authors")
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();

    @CreatedDate
    private LocalDateTime createdAt;

    public Author(String name) {
        this.name = name;
    }

    public void addBook(Book book){
        books.add(book);
        book.getAuthors().add(this);
    }

    public void removeBooks(Book book){
        books.remove(book);
        book.getAuthors().remove(this);
    }
}

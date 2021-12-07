package com.example.AlmightyBook.domain;

import java.util.List;


public interface CatalogRepository {
    List<Book> findAll();
}

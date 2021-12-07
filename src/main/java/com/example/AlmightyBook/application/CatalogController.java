package com.example.AlmightyBook.application;

import com.example.AlmightyBook.domain.Book;
import com.example.AlmightyBook.domain.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.xml.catalog.Catalog;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService service;

    public List<Book> findByTitle(String title){
        return service.findByTitle(title);
    }
}

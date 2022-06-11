package com.example.almightybook.catalog.web;

import com.example.almightybook.catalog.application.port.CatalogUseCase;
import com.example.almightybook.catalog.domain.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CatalogController.class})
class CatalogControllerTest {

    @MockBean
    CatalogUseCase catalog;

    @Autowired
    CatalogController controller;

    @Test
    public void shouldGetAllBooks(){
        //given
        Book effective = new Book("Effective Java", 2005, new BigDecimal("99.90"), 50L);
        Book concurrency = new Book("Java Concurrency", 2006, new BigDecimal("99.90"), 50L);
        when(catalog.findAll()).thenReturn(List.of(effective, concurrency));

        //when
        List<RestBook> all = controller.getAll(mockRequest(), Optional.empty(), Optional.empty());

        //then
        assertEquals(2, all.size());
    }

    private MockHttpServletRequest mockRequest() {
        return new MockHttpServletRequest();
    }

}
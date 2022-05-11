package com.example.AlmightyBook.catalog.web;

import com.example.AlmightyBook.catalog.application.port.CatalogUseCase;
import com.example.AlmightyBook.catalog.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({CatalogController.class})
@ActiveProfiles("test")
@WithMockUser
class CatalogControllerTestWebTest {

    @MockBean
    CatalogUseCase catalogUseCase;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldGetAllBooks() throws Exception {
        //given
        Book effective = new Book("Effective Java", 2005, new BigDecimal("99.90"), 50L);
        Book concurrency = new Book("Java Concurrency", 2006, new BigDecimal("99.90"), 50L);
        when(catalogUseCase.findAll()).thenReturn(List.of(effective, concurrency));

        //expect
        mockMvc.perform(get("/catalog"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
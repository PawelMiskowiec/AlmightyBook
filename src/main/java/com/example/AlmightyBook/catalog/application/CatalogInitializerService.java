package com.example.almightybook.catalog.application;


import com.example.almightybook.catalog.application.port.CatalogInitializerUseCase;
import com.example.almightybook.catalog.application.port.CatalogUseCase;
import com.example.almightybook.catalog.application.port.CatalogUseCase.CreateBookCommand;
import com.example.almightybook.catalog.application.port.CatalogUseCase.UpdateBookCoverCommand;
import com.example.almightybook.catalog.db.AuthorJpaRepository;
import com.example.almightybook.catalog.domain.Author;
import com.example.almightybook.catalog.domain.Book;
import com.example.almightybook.jpa.BaseEntity;
import com.example.almightybook.orders.application.port.ManageOrderUseCase;
import com.example.almightybook.orders.application.port.QueryOrderUseCase;
import com.example.almightybook.orders.domain.Recipient;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CatalogInitializerService implements CatalogInitializerUseCase {
    private final CatalogUseCase catalog;
    private final ManageOrderUseCase processOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorJpaRepository;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void initialize() {
        initData();
        placeOrder();
    }

    private void initData() {
        ClassPathResource resource = new ClassPathResource("books.csv");
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader((resource.getInputStream())))) {

            CsvToBean<CsvBook> build = new CsvToBeanBuilder<CsvBook>(reader)
                    .withType(CsvBook.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            build.stream().forEach(this::initBook);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse CSV file", e);
        }
    }

    private void initBook(CsvBook csvBook) {
        // parse authors
        Set<Long> authors = Arrays.stream(csvBook.authors.split(","))
                .filter(StringUtils::isNotBlank)
                .map(String::trim)
                .map(this::getOrCreateAuthor)
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        CreateBookCommand command = new CreateBookCommand(
                csvBook.title,
                authors,
                csvBook.year,
                csvBook.amount,
                50L
        );
        Book book = catalog.addBook(command);
        catalog.updateBookCover(updateBookCoverCommand(book.getId(), csvBook.thumbnail));
    }

    private UpdateBookCoverCommand updateBookCoverCommand(Long bookId, String thumbnailUrl) {
        ResponseEntity<byte[]> response = restTemplate.exchange(thumbnailUrl, HttpMethod.GET, null, byte[].class);
        String contentType = response.getHeaders().getContentType().toString();
        return new UpdateBookCoverCommand(bookId, response.getBody(), contentType, "cover");
    }

    private Author getOrCreateAuthor(String name) {
        return authorJpaRepository
                .findByNameIgnoreCase(name)
                .orElseGet(() -> authorJpaRepository.save(new Author(name)));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CsvBook{
        @CsvBindByName
        private String title;
        @CsvBindByName
        private String authors;
        @CsvBindByName
        private Integer year;
        @CsvBindByName
        private BigDecimal amount;
        @CsvBindByName
        private String thumbnail;
    }

    private void placeOrder() {
        Book effectiveJava = catalog.findOneByTitle("clean")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book"));
        Book cleanCode = catalog.findOneByTitle("effective")
                .orElseThrow(() -> new IllegalStateException("Cannot find a book"));;

        System.out.println(effectiveJava);
        System.out.println(cleanCode);

        Recipient recipient = Recipient.builder()
                .name("Dominik Politolog")
                .phone("343-412-332")
                .city("Zbąszynek")
                .street("Ul. Fizycznych 18")
                .zipCode("69-666")
                .email("domino666@gmail.com")
                .build();

        ManageOrderUseCase.PlaceOrderCommand command = ManageOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new ManageOrderUseCase.OrderItemCommand(effectiveJava.getId(), 10))
                .item(new ManageOrderUseCase.OrderItemCommand(cleanCode.getId(), 20))
                .build();

        processOrder.placeOrder(command);
        queryOrder.findAll()
                .forEach(order -> log.info("Got order with total price: " + order.getFinalPrice() + " details: " + order));
    }

}

package com.example.AlmightyBook.catalog.web;

import com.example.AlmightyBook.catalog.application.port.CatalogUseCase;
import com.example.AlmightyBook.catalog.db.AuthorJpaRepository;
import com.example.AlmightyBook.catalog.domain.Author;
import com.example.AlmightyBook.order.application.port.ManageOrderUseCase;
import com.example.AlmightyBook.order.application.port.QueryOrderUseCase;
import com.example.AlmightyBook.order.domain.Order;
import com.example.AlmightyBook.order.domain.OrderStatus;
import com.example.AlmightyBook.order.domain.Recipient;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.EmptyStackException;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final CatalogUseCase catalog;
    private final ManageOrderUseCase processOrder;
    private final QueryOrderUseCase queryOrder;
    private final AuthorJpaRepository authorJpaRepository;

    @PostMapping("/data")
    @Transactional
    public void initialize(){
        initData();
        placeOrder();
    }

    private void placeOrder() {
        Recipient recipient = Recipient.builder()
                .name("Dominik Politolog")
                .phone("343-412-332")
                .city("Zbąszynek")
                .street("Ul. Fizycznych 18")
                .zipCode("69-666")
                .email("domino666@gmail.com")
                .build();

        queryOrder.findAll()
                .forEach(order -> System.out.println("Got order with total price: " + order.totalPrice() + " details: " + order));
    }

    private void initData() {
        Author joshua = new Author("Joshua", "Bloch");
        Author neal = new Author("Neal", "Gafter");
        authorJpaRepository.save(joshua);
        authorJpaRepository.save(neal);

        CatalogUseCase.CreateBookCommand effectiveJava = new CatalogUseCase.CreateBookCommand(
                "Effective Java",
                Set.of(joshua.getId()),
                2005,
                new BigDecimal("79.00")
        );
        CatalogUseCase.CreateBookCommand javaPuzzlers =  new CatalogUseCase.CreateBookCommand(
                "Java Puzzlers",
                Set.of(joshua.getId(), neal.getId()),
                2018,
                new BigDecimal("99.00")
        );
        catalog.addBook(effectiveJava);
        catalog.addBook(javaPuzzlers);

        System.out.println(catalog.findOneByTitle("Java Puz"));
    }

}

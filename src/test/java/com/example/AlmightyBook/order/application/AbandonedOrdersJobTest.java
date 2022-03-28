package com.example.AlmightyBook.order.application;

import com.example.AlmightyBook.catalog.application.port.CatalogUseCase;
import com.example.AlmightyBook.catalog.db.BookJpaRepository;
import com.example.AlmightyBook.catalog.domain.Book;
import com.example.AlmightyBook.clock.Clock;
import com.example.AlmightyBook.order.application.port.ManageOrderUseCase;
import com.example.AlmightyBook.order.application.port.QueryOrderUseCase;
import com.example.AlmightyBook.order.domain.OrderStatus;
import com.example.AlmightyBook.order.domain.Recipient;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        properties = "app.orders.payment-period=1H"
)
@AutoConfigureTestDatabase
class AbandonedOrdersJobTest {

    @TestConfiguration
    static class TestConfig{
        @Bean
        public Clock.Fake clock(){
            return new Clock.Fake();
        }
    }

    @Autowired
    AbandonedOrdersJob ordersJob;

    @Autowired
    ManageOrderService service;

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    QueryOrderUseCase queryOrderSerivce;

    @Autowired
    Clock.Fake clock;

    @Autowired
    CatalogUseCase catalog;

    @Test
    public void shouldMarkOrderAsAbandoned() {
        //given
        Book book = givenEffectiveJava(50L);
        Long orderId = placedOrder(book.getId(), 15);

        //
        clock.tick(Duration.ofHours(2));
        ordersJob.run();

        //then
        assertEquals(OrderStatus.ABANDONED, queryOrderSerivce.findById(orderId).get().getStatus());
        assertEquals(50L, catalog.findById(book.getId()).get().getAvailable());
    }

    private Long placedOrder(Long bookId, int copies){
        ManageOrderUseCase.PlaceOrderCommand command = ManageOrderUseCase.PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new ManageOrderUseCase.OrderItemCommand(bookId, copies))
                .build();
        return  service.placeOrder(command).getRight();

    }

    private Recipient recipient() {
        return Recipient.builder().email("marek@example.eu").build();
    }

    private Book givenEffectiveJava(long available) {
        return bookJpaRepository.save(new Book("Effective Java", 2005, new BigDecimal("199.90"), available));
    }

    private Long getAvailableCopiesOf(Book book) {
        return catalog.findById(book.getId()).get().getAvailable();
    }


}
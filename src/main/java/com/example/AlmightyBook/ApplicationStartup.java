package com.example.AlmightyBook;

import com.example.AlmightyBook.catalog.application.port.CatalogUseCase;
import com.example.AlmightyBook.catalog.application.port.CatalogUseCase.CreateBookCommand;
import com.example.AlmightyBook.catalog.application.port.CatalogUseCase.UpdateBookCommand;
import com.example.AlmightyBook.catalog.domain.Book;
import com.example.AlmightyBook.order.application.port.PlaceOrderUseCase;
import com.example.AlmightyBook.order.application.port.PlaceOrderUseCase.PlaceOrderCommand;
import com.example.AlmightyBook.order.application.port.QueryOrderUseCase;
import com.example.AlmightyBook.order.domain.OrderItem;
import com.example.AlmightyBook.order.domain.Recipient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

import static com.example.AlmightyBook.order.application.port.PlaceOrderUseCase.*;

@Component
class ApplicationStartup implements CommandLineRunner {

    private final CatalogUseCase catalog;
    private final PlaceOrderUseCase placeOrder;
    private final QueryOrderUseCase queryOrder;

    public ApplicationStartup(CatalogUseCase catalog,
                              PlaceOrderUseCase placeOrder,
                              QueryOrderUseCase queryOrder) {
        this.catalog = catalog;
        this.placeOrder = placeOrder;
        this.queryOrder = queryOrder;
    }

    @Override
    public void run(String... args){
        initData();
        searchCatalog();
        placeOrder();

    }

    private void placeOrder() {
        Book harry = catalog.findOneByTitle("Harry").orElseThrow(() -> new IllegalStateException("Cannot find a book with such title"));
        Book sezonBurz = catalog.findOneByTitle("Sezon").orElseThrow(() -> new IllegalStateException("Cannot find a book with such title"));

        Recipient recipient = Recipient.builder()
                .name("Dominik Politolog")
                .phone("343-412-332")
                .city("Zbąszynek")
                .street("Ul. Fizycznych 18")
                .zipCode("69-666")
                .email("domino666@gmail.com")
                .build();

        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient)
                .item(new OrderItem(harry, 16))
                .item(new OrderItem(sezonBurz, 7))
                .build();

        PlaceOrderResponse response = placeOrder.placeOrder(command);
        System.out.println("Created order with ID: " + response.getOrderId() );

        queryOrder.findAll()
                .forEach(order -> System.out.println("Got order with total price: " + order.totalPrice() + " details: " + order));
    }

    private void searchCatalog() {
        findByAuthor();
        findAndUpdate();
        findByAuthor();
    }

    private void findAndUpdate() {
        catalog.findOneByTitleAndAuthor("Harry", "JK")
                .ifPresent(book -> {
                    UpdateBookCommand command = UpdateBookCommand.builder()
                            .id(book.getId())
                            .title("Harry Potter i Druga Komnata Tajemnic ")
                            .build();
                    CatalogUseCase.UpdateBookResponse response = catalog.updateBook(command);
                    System.out.println("Updating book result: " + response.isSuccess());
                });

    }

    private void initData() {
        catalog.addBook(new CreateBookCommand("Harry Potter i Komnata Tajemnic", "JK Rowling", 1998, new BigDecimal(100)));
        catalog.addBook(new CreateBookCommand("Władca Pierścieni: Dwie Wieże", "JRR Tolkien", 1954, new BigDecimal(130)));
        catalog.addBook(new CreateBookCommand("Mężczyźni, którzy nienawidzą kobiet", "Sieg Larsson", 2005, new BigDecimal(60)));
        catalog.addBook(new CreateBookCommand("Sezon Burz", "Andrzej Sapkowski", 2013, new BigDecimal(40)));
    }

    private void findByTitle() {
        List<Book> books = catalog.findByTitle("Harry");
        books.forEach(System.out::println);
    }

    private void findByAuthor() {
        List<Book> books = catalog.findByAuthor("JK");
        books.forEach(System.out::println);
    }
}

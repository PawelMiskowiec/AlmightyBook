package com.example.AlmightyBook.order.application;

import com.example.AlmightyBook.catalog.application.port.CatalogUseCase;
import com.example.AlmightyBook.catalog.db.BookJpaRepository;
import com.example.AlmightyBook.catalog.domain.Book;
import com.example.AlmightyBook.order.application.port.ManageOrderUseCase;
import com.example.AlmightyBook.order.application.port.QueryOrderUseCase;
import com.example.AlmightyBook.order.db.OrderJpaRepository;
import com.example.AlmightyBook.order.domain.Delivery;
import com.example.AlmightyBook.order.domain.OrderStatus;
import com.example.AlmightyBook.order.domain.Recipient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Objects;

import static com.example.AlmightyBook.order.application.port.ManageOrderUseCase.*;
import static com.example.AlmightyBook.order.application.port.ManageOrderUseCase.PlaceOrderCommand;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class OrderServicesTest {

    @Autowired
    BookJpaRepository bookJpaRepository;

    @Autowired
    OrderJpaRepository repository;

    @Autowired
    ManageOrderService service;

    @Autowired
    QueryOrderUseCase queryService;

    @Autowired
    CatalogUseCase catalog;

    @Test
    public void userCanPlaceOrder() {
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Book jcip = givenJavaConcurrency(50L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 15))
                .item(new OrderItemCommand(jcip.getId(), 10))
                .build();
        //when
        PlaceOrderResponse response = service.placeOrder(command);

        //then
        assertTrue(response.isSuccess());
        assertEquals(35L, getAvailableCopiesOf(effectiveJava));
        assertEquals(40L, getAvailableCopiesOf(jcip));

    }

    @Test
    public void userCanRevokeOrder() {
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "marek@example.org";
        Long orderID = placedOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35l, getAvailableCopiesOf(effectiveJava));

        // place order
        // books number decreased

        // when
        UpdateStatusCommand command = new UpdateStatusCommand(orderID, OrderStatus.CANCELED, recipient);
        service.updateOrderStatus(command);

        // then
        // books number changed back
        // order status is cancelled
        assertEquals(50L, getAvailableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.CANCELED, queryService.findById(orderID).get().getStatus());

    }

    @Test
    public void userCannotRevokePaidOrder(){
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placedOrder(effectiveJava.getId(), 10);

        //when
        UpdateStatusCommand command = new UpdateStatusCommand(orderId, OrderStatus.PAID, "admin@example.org");
        service.updateOrderStatus(command);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UpdateStatusCommand canceledStatus = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "admin@example.org");
            service.updateOrderStatus(canceledStatus);
        });

        //then

        assertTrue(exception.getMessage().contains("Unable to mark PAID order as CANCELED"));

    }

    @Test
    public void userCannotRevokeShippedOrder(){
        //given
        Book effectiveJava = givenEffectiveJava(50L);
        Long orderId = placedOrder(effectiveJava.getId(), 10);

        //when
        UpdateStatusCommand paidStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.PAID, "admin@example.org");
        UpdateStatusCommand shippedStatusCommand = new UpdateStatusCommand(orderId, OrderStatus.SHIPPED, "admin@example.org");
        service.updateOrderStatus(paidStatusCommand);
        service.updateOrderStatus(shippedStatusCommand);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            UpdateStatusCommand canceledStatus = new UpdateStatusCommand(orderId, OrderStatus.CANCELED, "admin@example.org");
            service.updateOrderStatus(canceledStatus);
        });

        //then

        assertTrue(exception.getMessage().contains("Unable to mark SHIPPED order as CANCELED"));

    }

    @Test
    public void userCannotOrderNonExistingBooks() {
        //given
        Book effectiveJava = new Book("Effective Java", 2005, new BigDecimal("199.90"), 30L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .recipient(recipient())
                .build();

        //when
        InvalidDataAccessApiUsageException exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            service.placeOrder(command);
        });

        //then
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("The given id must not be null!"));
    }

    @Test
    public void userCannotOrderNegativeNumberOfBooks() {
        //given
        Book effectiveJava = givenEffectiveJava(30L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .item(new OrderItemCommand(effectiveJava.getId(), -10))
                .recipient(recipient())
                .build();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.placeOrder(command);
        });
        System.out.println(exception.getMessage());
        //then
        assertTrue(Objects.requireNonNull(exception.getMessage()).contains("Quantity of ordered books should be greater than 0"));

    }


    @Test
    public void userCannotRevokeOtherUsersOrder(){
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "adam@example.org";
        Long orderID = placedOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35l, getAvailableCopiesOf(effectiveJava));

        // when
        UpdateStatusCommand command= new UpdateStatusCommand(orderID, OrderStatus.CANCELED, "marek@example.com");
        service.updateOrderStatus(command);

        // then
        assertEquals(35L, getAvailableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.NEW, queryService.findById(orderID).get().getStatus());

    }

    @Test
    public void userCantOrderMoreBooksThanAvailable() {
        //given
        Book effectiveJava = givenEffectiveJava(5L);
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(effectiveJava.getId(), 10))
                .build();
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.placeOrder(command);
        });

        //then
        assertTrue(exception.getMessage().contains("Too many copies of book " + effectiveJava.getId() + " requested"));
    }

    @Test
    public void adminCanMarkOrderAsPaid() {
        // given
        Book effectiveJava = givenEffectiveJava(50L);
        String recipient = "marek@example.org";
        Long orderID = placedOrder(effectiveJava.getId(), 15, recipient);
        assertEquals(35l, getAvailableCopiesOf(effectiveJava));

        // place order
        // books number decreased

        // when
        String admin = "admin@example.org";
        UpdateStatusCommand command = new UpdateStatusCommand(orderID, OrderStatus.PAID, admin);
        service.updateOrderStatus(command);

        // then
        // books number changed back
        // order status is cancelled
        assertEquals(35L, getAvailableCopiesOf(effectiveJava));
        assertEquals(OrderStatus.PAID, queryService.findById(orderID).get().getStatus());

    }

    @Test
    public void shippingCostsAreAddedToTotalOrderPrice(){
        //given
        Book book = givenBook(50L, "49.90");

        //when
        Long orderId = placedOrder(book.getId(), 1);

        //then
        assertEquals("59.80", orderOf(orderId).getFinalPrice().toPlainString());
    }


    @Test
    public void shippingCostsAreDiscountedOver100zlotys(){
        //given
        Book book = givenBook(50L, "49.90");

        //when
        Long orderId = placedOrder(book.getId(), 3);

        //then
        RichOrder order = orderOf(orderId);
        assertEquals("149.70", order.getFinalPrice().toPlainString());
        assertEquals("149.70", order.getOrderPrice().getItemsPrice().toPlainString());
    }

    @Test
    public void cheapestBookIsHalfPricedWhenTotalOver200zlotys(){
        //given
        Book book = givenBook(50L, "49.90");

        //when
        Long orderId = placedOrder(book.getId(), 5);

        //then
        RichOrder order = orderOf(orderId);
        assertEquals("224.55", order.getFinalPrice().toPlainString());
        assertEquals("249.50", order.getOrderPrice().getItemsPrice().toPlainString());
    }

    @Test
    public void cheapestBookIsFreeWhenTotalOver400zlotys(){
        //given
        Book book = givenBook(50L, "49.90");

        //when
        Long orderId = placedOrder(book.getId(), 10);

        //then
        RichOrder order = orderOf(orderId);
        assertEquals("449.10", order.getFinalPrice().toPlainString());
        assertEquals("499.00", order.getOrderPrice().getItemsPrice().toPlainString());
    }


    private Long getAvailableCopiesOf(Book book) {
        return catalog.findById(book.getId()).get().getAvailable();
    }

    private Long placedOrder(Long bookId, int copies){
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient())
                .item(new OrderItemCommand(bookId, copies))
                .delivery(Delivery.COURIER)
                .build();
        return  service.placeOrder(command).getRight();

    }

    private Long placedOrder(Long bookId, int copies, String recipient){
        PlaceOrderCommand command = PlaceOrderCommand
                .builder()
                .recipient(recipient(recipient))
                .item(new OrderItemCommand(bookId, copies))
                .delivery(Delivery.COURIER)
                .build();
        return  service.placeOrder(command).getRight();

    }

    private Recipient recipient() {
        return Recipient.builder().email("paul@example.eu").build();
    }

    private Recipient recipient(String email) {
        return Recipient.builder().email(email).build();
    }

    private RichOrder orderOf(Long orderId){
        return queryService.findById(orderId).get();
    }

    private Book givenBook(long available, String price) {
        return bookJpaRepository.save(new Book("Java Concurrency in Practice", 2006, new BigDecimal(price), available));
    }

    private Book givenJavaConcurrency(long available) {
        return bookJpaRepository.save(new Book("Java Concurrency in Practice", 2006, new BigDecimal("99.90"), available));
    }

    private Book givenEffectiveJava(long available) {
        return bookJpaRepository.save(new Book("Effective Java", 2005, new BigDecimal("199.90"), available));
    }
}
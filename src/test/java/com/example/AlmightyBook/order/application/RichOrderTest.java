package com.example.AlmightyBook.order.application;

import com.example.AlmightyBook.catalog.domain.Book;
import com.example.AlmightyBook.order.domain.OrderItem;
import com.example.AlmightyBook.order.domain.OrderStatus;
import com.example.AlmightyBook.order.domain.Recipient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RichOrderTest {

//    @Test
//    public void calculatesTotalPriceOfEmptyOrder(){
//        // given
//        RichOrder order = new RichOrder(
//                1L,
//                OrderStatus.NEW,
//                Collections.emptySet(),
//                Recipient.builder().build(),
//                LocalDateTime.now()
//        );
//
//        //when
//        BigDecimal price = order.totalPrice();
//
//        //then
//        Assertions.assertEquals(BigDecimal.ZERO, price);
//    }
//
//    @Test
//    public void calculatesTotalPrice(){
//        // given
//        Book book1 = new Book();
//        book1.setPrice(new BigDecimal("12.5"));
//        Book book2 = new Book();
//        book2.setPrice(new BigDecimal("33.99"));
//        Set<OrderItem> items = new HashSet<>(
//                Arrays.asList(
//                        new OrderItem(book1, 2),
//                        new OrderItem(book2, 5)
//                )
//        );
//        RichOrder order = new RichOrder(
//                1L,
//                OrderStatus.NEW,
//                items,
//                Recipient.builder().build(),
//                LocalDateTime.now()
//        );
//
//        //when
//        BigDecimal price = order.totalPrice();
//
//        //then
//        Assertions.assertEquals(new BigDecimal("194.95"), price);
//    }

}
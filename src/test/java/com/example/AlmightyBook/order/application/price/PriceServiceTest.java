package com.example.AlmightyBook.order.application.price;

import com.example.AlmightyBook.catalog.domain.Book;
import com.example.AlmightyBook.order.domain.Order;
import com.example.AlmightyBook.order.domain.OrderItem;
import com.example.AlmightyBook.order.domain.OrderStatus;
import com.example.AlmightyBook.order.domain.Recipient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class PriceServiceTest {

    PriceService priceService = new PriceService();

    @Test
    public void calculatesTotalPriceOfEmptyOrder(){
        // given
        Order order = Order.builder()
                .items(Collections.emptySet())
                .recipient(Recipient.builder().build())
                .build();
        //when
        OrderPrice orderPrice = priceService.calculatePrice(order);

        //then
        Assertions.assertEquals(BigDecimal.ZERO, orderPrice.finalPrice());
    }

    @Test
    public void calculatesTotalPrice() {
        // given
        Book book1 = new Book();
        book1.setPrice(new BigDecimal("12.5"));
        Book book2 = new Book();
        book2.setPrice(new BigDecimal("33.99"));


        Order order = Order.builder()
                .item(new OrderItem(book1, 2))
                .item(new OrderItem(book2, 5))
                .recipient(Recipient.builder().build())
                .build();

        //when
        OrderPrice price = priceService.calculatePrice(order);

        //then
        Assertions.assertEquals(new BigDecimal("194.95"), price.finalPrice());
        Assertions.assertEquals(new BigDecimal("194.95"), price.getItemsPrice());
    }
}
package com.example.AlmightyBook.orders.application.price;

import com.example.AlmightyBook.orders.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}

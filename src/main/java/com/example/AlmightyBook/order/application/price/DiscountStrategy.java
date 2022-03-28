package com.example.AlmightyBook.order.application.price;

import com.example.AlmightyBook.order.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}

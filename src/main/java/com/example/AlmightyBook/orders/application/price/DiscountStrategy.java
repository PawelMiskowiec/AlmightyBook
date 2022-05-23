package com.example.almightybook.orders.application.price;

import com.example.almightybook.orders.domain.Order;

import java.math.BigDecimal;

public interface DiscountStrategy {
    BigDecimal calculate(Order order);
}

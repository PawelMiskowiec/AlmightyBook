package com.example.almightybook.orders.application;

import com.example.almightybook.orders.application.price.OrderPrice;
import com.example.almightybook.orders.domain.OrderItem;
import com.example.almightybook.orders.domain.OrderStatus;
import com.example.almightybook.orders.domain.Recipient;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Value
public class RichOrder {
    Long id;
    OrderStatus status;
    Set<OrderItem> items;
    Recipient recipient;
    LocalDateTime createdAt;
    OrderPrice orderPrice;
    BigDecimal finalPrice;
}

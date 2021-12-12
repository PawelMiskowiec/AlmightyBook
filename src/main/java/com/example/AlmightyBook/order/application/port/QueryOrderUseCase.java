package com.example.AlmightyBook.order.application.port;

import com.example.AlmightyBook.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {
    List<Order> findAll();
}

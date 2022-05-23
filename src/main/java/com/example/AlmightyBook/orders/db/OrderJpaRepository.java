package com.example.almightybook.orders.db;

import com.example.almightybook.orders.domain.Order;
import com.example.almightybook.orders.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusAndCreatedAtLessThanEqual(OrderStatus status, LocalDateTime dateTime);
}

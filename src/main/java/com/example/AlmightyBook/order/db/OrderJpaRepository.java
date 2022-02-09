package com.example.AlmightyBook.order.db;

import com.example.AlmightyBook.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

}

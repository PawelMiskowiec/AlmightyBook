package com.example.AlmightyBook.orders.application.port;

import com.example.AlmightyBook.orders.application.RichOrder;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

}

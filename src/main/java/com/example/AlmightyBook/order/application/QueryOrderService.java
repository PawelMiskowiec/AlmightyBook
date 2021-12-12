package com.example.AlmightyBook.order.application;

import com.example.AlmightyBook.order.application.port.QueryOrderUseCase;
import com.example.AlmightyBook.order.domain.Order;
import com.example.AlmightyBook.order.domain.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class QueryOrderService implements QueryOrderUseCase {
    private final OrderRepository repository;

    @Override
    public List<Order> findAll() {
        return repository.findAll();
    }
}

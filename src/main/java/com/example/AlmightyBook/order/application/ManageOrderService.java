package com.example.AlmightyBook.order.application;

import com.example.AlmightyBook.catalog.application.port.CatalogUseCase;
import com.example.AlmightyBook.order.application.port.ManageOrderUseCase;
import com.example.AlmightyBook.order.db.OrderJpaRepository;
import com.example.AlmightyBook.order.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManageOrderService implements ManageOrderUseCase {
    private final OrderJpaRepository repository;
    private final CatalogUseCase catalog;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order
                .builder()
                .recipient(command.getRecipient())
                .items(command.getItems())
                .build();
        Order save = repository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

    @Override
    public void deleteOrderById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateOrderStatus(Long id, OrderStatus status) {
        repository.findById(id)
                .ifPresent(order -> {
                    order.setStatus(status);
                    repository.save(order);
                });
    }
}


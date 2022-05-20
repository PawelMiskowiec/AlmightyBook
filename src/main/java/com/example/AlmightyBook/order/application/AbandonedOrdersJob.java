package com.example.AlmightyBook.order.application;

import com.example.AlmightyBook.clock.Clock;
import com.example.AlmightyBook.order.application.port.ManageOrderUseCase;
import com.example.AlmightyBook.order.application.port.ManageOrderUseCase.UpdateStatusCommand;
import com.example.AlmightyBook.order.db.OrderJpaRepository;
import com.example.AlmightyBook.order.domain.Order;
import com.example.AlmightyBook.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class AbandonedOrdersJob {
    private final OrderJpaRepository repository;
    private final ManageOrderUseCase orderUseCase;
    private final OrdersProperties properties;
    private final User systemUser;
    private final Clock clock;

    @Transactional
    @Scheduled(cron = "${app.orders.abandon-cron}")
    public void run() {
        Duration paymentPeriod = properties.getPaymentPeriod();
        LocalDateTime timeStamp = clock.now().minus(paymentPeriod);
        List<Order> orders = repository.findByStatusAndCreatedAtLessThanEqual(OrderStatus.NEW, timeStamp);
        log.info("Found orders to be abandoned: " + orders.size());
        orders.forEach(order -> {
            UpdateStatusCommand command = new UpdateStatusCommand(order.getId(), OrderStatus.ABANDONED, systemUser);
            orderUseCase.updateOrderStatus(command);
        });
    }
}

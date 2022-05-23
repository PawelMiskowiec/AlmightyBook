package com.example.AlmightyBook.orders.application.price;

import com.example.AlmightyBook.orders.domain.Order;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PriceService {
    private final List<DiscountStrategy> strategies = List.of(
            new DeliveryDiscountStrategyImpl(),
            new TotalPriceDiscountStrategyImpl()
    );

    @Transactional
    public OrderPrice calculatePrice(Order order){
        return new OrderPrice(
                order.getItemsPrice(),
                order.getDeliveryPrice(),
                discounts(order)
        );
    }

    private BigDecimal discounts(Order order) {
        return strategies
                .stream()
                .map(strategy -> strategy.calculate(order))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

package com.example.AlmightyBook.order.application.price;

import com.example.AlmightyBook.order.domain.Order;

import java.math.BigDecimal;

public class DeliveryDiscountStrategyImpl implements DiscountStrategy {
    public static final BigDecimal threshold = BigDecimal.valueOf(100L);

    @Override
    public BigDecimal calculate(Order order) {
        if(order.getItemsPrice().compareTo(threshold)>=0){
            return order.getDeliveryPrice();
        }
        return BigDecimal.ZERO;
    }
}

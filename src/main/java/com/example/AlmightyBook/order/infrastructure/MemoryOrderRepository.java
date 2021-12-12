package com.example.AlmightyBook.order.infrastructure;


import com.example.AlmightyBook.order.domain.Order;
import com.example.AlmightyBook.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryOrderRepository implements OrderRepository {
    private final Map<Long, Order> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_VALUE = new AtomicLong(1L);

    @Override
    public Order save(Order order) {
        if(order.getId() != null){
            storage.put(order.getId(), order);
        } else{
            long nextId = nextID();
            order.setId(nextId);
            order.setCreatedAt(LocalDateTime.now());
            storage.put(nextId, order);
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(storage.values());
    }

    private long nextID(){
        return ID_NEXT_VALUE.getAndIncrement();
    }
}

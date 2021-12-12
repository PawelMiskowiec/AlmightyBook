package com.example.AlmightyBook.order.domain;

import com.example.AlmightyBook.catalog.domain.Book;
import lombok.Value;

@Value
public class OrderItem {
    Book book;
    int quantity;
}

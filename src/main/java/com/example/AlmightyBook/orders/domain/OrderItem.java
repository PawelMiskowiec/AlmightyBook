package com.example.almightybook.orders.domain;

import com.example.almightybook.catalog.domain.Book;
import com.example.almightybook.jpa.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "book_d")
    private Book book;
    private int quantity;
}

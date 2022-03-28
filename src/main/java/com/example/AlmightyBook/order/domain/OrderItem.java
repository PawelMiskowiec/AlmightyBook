package com.example.AlmightyBook.order.domain;

import com.example.AlmightyBook.catalog.domain.Book;
import com.example.AlmightyBook.jpa.BaseEntity;
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

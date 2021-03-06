package com.example.almightybook.orders.domain;

import com.example.almightybook.jpa.BaseEntity;
import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Recipient extends BaseEntity {
    private String name;
    private String phone;
    private String street;
    private String city;
    private String zipCode;
    private String email;
}

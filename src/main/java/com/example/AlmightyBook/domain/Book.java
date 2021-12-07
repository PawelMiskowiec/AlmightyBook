package com.example.AlmightyBook.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class Book {
    Long id;
    String title;
    String author;
    Integer year;


}

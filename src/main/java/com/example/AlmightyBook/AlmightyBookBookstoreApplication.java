package com.example.AlmightyBook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AlmightyBookBookstoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlmightyBookBookstoreApplication.class, args);
	}
}

package com.example.almightybook;

import com.example.almightybook.orders.application.OrdersProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@EnableConfigurationProperties(OrdersProperties.class)
@SpringBootApplication
public class AlmightyBookBookstoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlmightyBookBookstoreApplication.class, args);
	}

	@Bean
	RestTemplate restTemplate(){
		return new RestTemplateBuilder().build();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedMethods("*")
						.allowedOrigins("*");			}
		};
	}

}

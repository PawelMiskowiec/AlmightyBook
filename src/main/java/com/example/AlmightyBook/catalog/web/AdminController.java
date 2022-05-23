package com.example.almightybook.catalog.web;

import com.example.almightybook.catalog.application.port.CatalogInitializerUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@Slf4j
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {
    private final CatalogInitializerUseCase catalogInitializer;

    @Secured("ROLE_ADMIN")
    @PostMapping("/initialization")
    @Transactional
    public void initialize(){
        catalogInitializer.initialize();
    }

}

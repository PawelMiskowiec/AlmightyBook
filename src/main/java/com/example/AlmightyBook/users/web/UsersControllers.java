package com.example.almightybook.users.web;

import com.example.almightybook.users.application.port.UserRegistrationUseCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UsersControllers {

    private final UserRegistrationUseCase register;

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody RegisterCommand command){
        return register
                .register(command.username, command.password)
                .handle(
                        userEntity -> ResponseEntity.accepted().build(),
                        error -> ResponseEntity.badRequest().body(error)
                );

    }

    @Data
    static class RegisterCommand{
        @Email
        String username;
        @Size(min =3, max = 100)
        String password;
    }
}

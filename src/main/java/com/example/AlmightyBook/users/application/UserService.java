package com.example.AlmightyBook.users.application;

import com.example.AlmightyBook.user.db.UserEntityRepository;
import com.example.AlmightyBook.user.domain.UserEntity;
import com.example.AlmightyBook.users.application.port.UserRegistrationUseCase;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class UserService implements UserRegistrationUseCase {
    private final UserEntityRepository repository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public RegisterResponse register(String username, String password) {
        if(repository.findByUsernameIgnoreCase(username).isPresent()){
            return RegisterResponse.failure("Account already exists");
        }
        UserEntity entity = new UserEntity(username, encoder.encode(password));
        return RegisterResponse.success(repository.save(entity));
    }
}

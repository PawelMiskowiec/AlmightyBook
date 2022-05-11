package com.example.AlmightyBook.users.application.port;

import com.example.AlmightyBook.commons.Either;
import com.example.AlmightyBook.user.domain.UserEntity;
import org.springframework.security.core.userdetails.User;

public interface UserRegistrationUseCase {
    RegisterResponse register(String username, String password);

    class RegisterResponse extends Either<String, UserEntity>{

        public RegisterResponse(boolean success, String left, UserEntity right) {
            super(success, left, right);
        }

        public static RegisterResponse success(UserEntity right){
            return new RegisterResponse(true, null, right);
        }

        public static RegisterResponse failure(String left){
            return new RegisterResponse(false, left, null);
        }
    }
}

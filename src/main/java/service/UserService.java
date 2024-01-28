package service;

import dto.UserLoginDto;
import dto.UserSignUpDto;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void signUp(UserSignUpDto userSignUpDto);

    Optional<UUID> login(UserLoginDto userLoginDto);
}

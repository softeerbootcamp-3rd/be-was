package service;

import dto.UserLoginDto;
import dto.UserSignUpDto;

import java.util.UUID;

public interface UserService {
    void signUp(UserSignUpDto userSignUpDto);

    UUID login(UserLoginDto userLoginDto);
}

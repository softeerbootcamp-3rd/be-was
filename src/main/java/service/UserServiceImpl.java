package service;

import db.Database;
import dto.UserLoginDto;
import dto.UserSignUpDto;
import exception.BadRequestException;
import exception.InvalidLogin;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import session.Session;

import javax.xml.crypto.Data;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String ID_EMPTY_MESSAGE = "ID cannot be empty";
    private static final String PASSWORD_EMPTY_MESSAGE = "Password cannot be empty";
    private static final String NAME_EMPTY_MESSAGE = "Name cannot be empty";
    private static final String INVALID_EMAIL_FORMAT_MESSAGE = "Invalid email format";
    private static final String ID_EXISTS_MESSAGE = "ID already exists";

    public static class UserServiceHolder {
        private static final UserService INSTANCE = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return UserServiceHolder.INSTANCE;
    }

    @Override
    public void signUp(UserSignUpDto userSignUpDto) {
        validateUserSignUpDto(userSignUpDto);

        String userId = userSignUpDto.getId();
        Optional<User> existingUser = Database.findUserById(userId);

        existingUser.ifPresent(user -> {
            throw new BadRequestException(ID_EXISTS_MESSAGE);
        });

        User newUser = new User(userId, userSignUpDto.getPassword(), userSignUpDto.getName(), userSignUpDto.getEmail());
        Database.addUser(newUser);

        logger.info("User registered: {}", newUser);
    }

    @Override
    public UUID login(UserLoginDto userLoginDto) {
        validateNotBlank(userLoginDto.getId(), ID_EMPTY_MESSAGE);
        validateNotBlank(userLoginDto.getPassword(), PASSWORD_EMPTY_MESSAGE);
        Optional<User> userOptional = Database.findUserById(userLoginDto.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return Session.login(user);
        } else {
            throw new InvalidLogin("Invalid ID and password. Please double check if it's correct.");
        }
    }

    private void validateUserSignUpDto(UserSignUpDto userSignUpDto) {
        validateNotBlank(userSignUpDto.getId(), ID_EMPTY_MESSAGE);
        validateNotBlank(userSignUpDto.getPassword(), PASSWORD_EMPTY_MESSAGE);
        validateNotBlank(userSignUpDto.getName(), NAME_EMPTY_MESSAGE);

        String email = userSignUpDto.getEmail();
        if (email == null || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new BadRequestException(INVALID_EMAIL_FORMAT_MESSAGE);
        }
    }
    private void validateNotBlank(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(errorMessage);
        }
    }
}

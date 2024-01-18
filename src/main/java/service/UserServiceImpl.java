package service;

import db.Database;
import dto.UserSignUpDto;
import exception.BadRequestException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public void signUp(UserSignUpDto userSignUpDto) {
        validation(userSignUpDto);
        Optional<User> userById = Database.findUserById(userSignUpDto.getId());
        if (userById.isPresent()) {
            throw new BadRequestException("ID already exists");
        } else {
            User user = new User(userSignUpDto.getId(), userSignUpDto.getPassword(), userSignUpDto.getName(), userSignUpDto.getEmail());
            Database.addUser(user);
            logger.debug("유저가 등록되었습니다. user : {}", user.toString());
        }
    }

    private void validation(UserSignUpDto userSignUpDto) {
        String id = userSignUpDto.getId();
        String password = userSignUpDto.getPassword();
        String name = userSignUpDto.getName();
        String email = userSignUpDto.getEmail();
        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestException("ID cannot be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password cannot be empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Name cannot be empty");
        }
        if (email == null || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            throw new BadRequestException("Invalid email format");
        }
    }

    public static class UserServiceHolder {
        private static final UserService INSTANCE = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return UserServiceHolder.INSTANCE;
    }
}
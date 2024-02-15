package service;

import exception.AlreadyExistUserException;
import exception.LoginFailedException;
import exception.UserNotFoundException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.UserRepository;

import java.util.Collection;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String userId, String password, String name, String email) {
        User userById = userRepository.findUserById(userId);
        if (userById == null) {
            User user = new User(userId, password, name, email);
            logger.debug("user info = {}", user);
            userRepository.addUser(user);
            return;
        }

        throw new AlreadyExistUserException("이미 존재하는 userId입니다.");
    }

    public User findUserById(String userId) {
        User findUser = userRepository.findUserById(userId);
        if (findUser == null) {
            throw new UserNotFoundException("일치하는 user가 존재하지 않습니다");
        }
        return findUser;
    }

    public User login(String userId, String password) {
        User findUser = userRepository.findUserById(userId);

        if (findUser == null || isWrongPw(password, findUser.getPassword())) {
            throw new LoginFailedException("userId 혹은 password가 일치하지 않습니다.");
        }

        return findUser;
    }

    private static boolean isWrongPw(String password, String userPassword) {
        return !password.equals(userPassword);
    }

    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    public void clear() {
        userRepository.clear();
    }
}

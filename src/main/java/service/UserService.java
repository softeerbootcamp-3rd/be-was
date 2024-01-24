package service;

import db.Database;
import dto.UserRequest;
import webserver.exception.GeneralException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.status.ErrorCode;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void createUser(String userId, String password, String name, String email){
        if(existsUserId(userId)){
            logger.error("userId가 이미 존재하는 예외 발생");
            throw new GeneralException(ErrorCode.USER_ID_ALREADY_EXISTS_ERROR);
        }

        User user = User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();

        Database.addUser(user);
        logger.debug("User 생성 완료");
        logger.debug(user.toString());
    }

    public void createUser(UserRequest.Register register){
        if(existsUserId(register.getUserId())){
            logger.error("userId가 이미 존재하는 예외 발생");
            throw new GeneralException(ErrorCode.USER_ID_ALREADY_EXISTS_ERROR);
        }

        User user = User.builder()
                .userId(register.getUserId())
                .password(register.getPassword())
                .name(register.getName())
                .email(register.getEmail())
                .build();

        Database.addUser(user);
        logger.debug("User 생성 완료");
        logger.debug(user.toString());
    }

    public User findUser(String userId){
        return Database.findUserById(userId);
    }

    public boolean existsUserId(String userId){
        return Database.existsUserByUserId(userId);
    }
}

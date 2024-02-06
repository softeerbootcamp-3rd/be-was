package service;

import db.Database;
import dto.UserRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.exception.GeneralException;
import webserver.exception.LoginFailedException;
import webserver.exception.UserIdAlreadyExistsException;
import webserver.session.Session;
import webserver.session.SessionManager;
import webserver.status.ErrorCode;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void createUser(String userId, String password, String name, String email){
        if(existsUserId(userId)){
            logger.error("userId가 이미 존재하는 예외 발생");
            throw new UserIdAlreadyExistsException(ErrorCode.USER_ID_ALREADY_EXISTS_ERROR);
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
            throw new UserIdAlreadyExistsException(ErrorCode.USER_ID_ALREADY_EXISTS_ERROR);
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

    public Session login(UserRequest.Login loginInfo){
        if(!existsUserId(loginInfo.getUserId())){
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED_ERROR);
        }

        User user = Database.findUserById(loginInfo.getUserId());

        if(!user.getPassword().equals(loginInfo.getPassword())){
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED_ERROR);
        }

        Session session = SessionManager.createSession(loginInfo.getUserId());
        SessionManager.save(session);

        return session;
    }

    public User findUser(String userId){
        return Database.findUserById(userId);
    }

    public boolean existsUserId(String userId){
        return Database.existsUserByUserId(userId);
    }
}

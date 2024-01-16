package service;

import dto.UserRequestDto;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void create(UserRequestDto userRequestDto){
        User user = new User(userRequestDto.getUserId(), userRequestDto.getPassword(),
                userRequestDto.getName(), userRequestDto.getEmail());

        logger.debug(user.toString());
    }
}

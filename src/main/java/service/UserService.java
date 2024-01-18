package service;

import db.Database;
import http.response.HttpResponse;
import dto.UserDto;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.ContentType;
import utils.FileReader;
import http.HttpStatus;
import webserver.RequestHandler;

/**
 * 서비스 로직 구현
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    // 회원가입
    public HttpResponse createUser(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getPassword(),
                    userDto.getName(), userDto.getEmail());
        // 유저 아이디 중복 검사
        if (validateDuplicated(user)) {
            Database.addUser(user);
            logger.info(Database.findAll().toString());

            byte[] body = Database.findUserById(user.getUserId()).toString().getBytes();
            return HttpResponse.of(HttpStatus.REDIRECT, ContentType.HTML, "/index.html", body);
        }
        byte[] body = FileReader.readFile("/user/form.html");
        return HttpResponse.of(HttpStatus.BAD_REQUEST, ContentType.HTML, body);
    }

    private boolean validateDuplicated(User user) {
        return Database.findUserById(user.getUserId()) == null;
    }


}

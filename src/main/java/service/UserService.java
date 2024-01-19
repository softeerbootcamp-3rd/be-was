package service;

import db.Database;
import exception.WebServerException;
import model.User;

import java.util.Map;

import static constant.ErrorCode.*;


public class UserService {

    // 회원가입 - 회원 id, 이메일 중복 확인
    public static void signUp(Map<String, String> params) {
        User newUser = new User(params);

        if (Database.findUserById(newUser.getUserId()) != null) {
            throw new WebServerException(USER_ID_DUPLICATED);
        } else if (Database.findUserByEmail(newUser.getEmail()) != null) {
            throw new WebServerException(USER_EMAIL_DUPLICATED);
        }

        Database.addUser(newUser);
    }
}

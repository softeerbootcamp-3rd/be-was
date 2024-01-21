package service;

import data.RequestData;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParserUtil;
import webserver.RequestHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static void registerUser(RequestData requestData) {
        Map<String, String> userProps = RequestParserUtil.parseUserRegisterQuery(requestData.getRequestContent());

        String userId = userProps.get("userId");
        String password = userProps.get("password");
        String name = userProps.get("name");
        String email = userProps.get("email");

        if (userProps.size() != 4) {
            logger.error("회원가입에 필요한 파라미터의 수가 부족합니다.");
            throw new IllegalArgumentException("입력 파라미터의 개수가 부적절합니다.");
        }

        // 추출한 데이터로 사용자 생성
        User newUser = new User(userId, password, name, email);

        // DB에 저장
        Database.addUser(newUser);

        logger.debug(newUser.toString());
    }
}

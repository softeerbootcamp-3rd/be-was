package service;

import data.RequestData;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static void registerUser(RequestData requestData) {
        String url = requestData.getRequestContent();

        String userQuery = url.split("\\?")[1];

        // HTTP 요청으로부터 사용자 데이터 추출
        String[] pairs = userQuery.split("&");

        Map<String, String> userProps = new HashMap<>();
        for (String pair : pairs) {
            String[] splitPair = pair.split("=");
            if (splitPair.length == 2) {
                String key = splitPair[0];
                String val;
                val = URLDecoder.decode(splitPair[1]);
                userProps.put(key, val);
            }
        }

        String userId = userProps.get("userId");
        String password = userProps.get("password");
        String name = userProps.get("name");
        String email = userProps.get("email");

        // 추출한 데이터로 사용자 생성
        User newUser = new User(userId, password, name, email);

        // DB에 저장
        Database.addUser(newUser);

        logger.debug(newUser.toString());
    }
}

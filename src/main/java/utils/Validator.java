package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);
    public static Boolean validateSignUpInfo(Map<String, String> body) {
        // userId, password, name, email 값이 잘 들어왔는지 확인
        if (!body.containsKey("userId") || !body.containsKey("password")
                || !body.containsKey("name") || !body.containsKey("email")) {
            logger.error("파라미터 정보 불충분");
            return false;
        }

        // userId, password, name, email null 값 검사
        if (body.get("userId") == null || body.get("password") == null ||
                body.get("name") == null || body.get("email") == null) {
            logger.info("null 값이 들어옴");
            return false;
        }

        // userId, password, name, email 빈 값 검사
        if (body.get("userId").isEmpty() || body.get("password").isEmpty() ||
                body.get("name").isEmpty() || body.get("email").isEmpty()) {
            logger.error("빈 값이 들어옴");
            return false;
        }

        return true;
    }

    public static Boolean validateLoginInfo(Map<String, String> body) {
        // userId, password 값이 잘 들어왔는지 확인
        if (!body.containsKey("userId") || !body.containsKey("password")) {
            return false;
        }

        if (body.get("userId").isEmpty() || body.get("password").isEmpty()) {
            return false;
        }

        return true;
    }
}

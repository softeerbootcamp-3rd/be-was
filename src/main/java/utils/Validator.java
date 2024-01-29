package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);
    public static Boolean validateSignUpInfo(String body) {
        Map<String, String> bodyMap = Parser.extractParams(body);
        // userId, password, name, email 값이 잘 들어왔는지 확인
        if (!bodyMap.containsKey("userId") || !bodyMap.containsKey("password")
                || !bodyMap.containsKey("name") || !bodyMap.containsKey("email")) {
            logger.error("파라미터 정보 불충분");
            return false;
        }

        // userId, password, name, email null 값 검사
        if (bodyMap.get("userId") == null || bodyMap.get("password") == null ||
                bodyMap.get("name") == null || bodyMap.get("email") == null) {
            logger.info("null 값이 들어옴");
            return false;
        }

        // userId, password, name, email 빈 값 검사
        if (bodyMap.get("userId").isEmpty() || bodyMap.get("password").isEmpty() ||
                bodyMap.get("name").isEmpty() || bodyMap.get("email").isEmpty()) {
            logger.error("빈 값이 들어옴");
            return false;
        }

        return true;
    }

    public static Boolean validateLoginInfo(String body) {
        Map<String, String> bodyMap = Parser.extractParams(body);
        // userId, password 값이 잘 들어왔는지 확인
        if (!bodyMap.containsKey("userId") || !bodyMap.containsKey("password")) {
            return false;
        }

        if (bodyMap.get("userId").isEmpty() || bodyMap.get("password").isEmpty()) {
            return false;
        }

        return true;
    }

    public static Boolean validatePostInfo(String body) {
        Map<String, String> bodyMap = Parser.extractParams(body);
        if (!bodyMap.containsKey("writer") || !bodyMap.containsKey("title") || !bodyMap.containsKey("contents")) {
            return false;
        }

        if (bodyMap.get("writer").isEmpty() || bodyMap.get("title").isEmpty() || bodyMap.get("contents").isEmpty()) {
            return false;
        }

        return true;
    }
}

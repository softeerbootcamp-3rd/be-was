package webserver;

import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {

//    @Test
//    public void parsingTest() {
//        String query = "userId=abc123&password=9999&name=kim&email=temp@naver.com";
//        Map<String, String> result = RequestHandler.parse(query);
//        Assertions.assertEquals(result.size(), 4);
//    }
//
//    @Test
//    public void createUserTest() {
//        Map<String, String> params = new HashMap<>();
//        params.put("userId", "abc123");
//        params.put("password", "9999");
//        params.put("name", "kim");
//        params.put("email", "temp@naver.com");
//
//        User user = RequestHandler.createUser(params);
//
//        Assertions.assertEquals(user.getUserId(), "abc123");
//        Assertions.assertEquals(user.getPassword(), "9999");
//        Assertions.assertEquals(user.getName(), "kim");
//        Assertions.assertEquals(user.getEmail(), "temp@naver.com");
//    }
}
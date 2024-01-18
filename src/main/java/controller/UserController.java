package controller;

import dto.HttpResponseDto;
import dto.UserDto;
import utils.HttpRequest;
import service.UserService;
import utils.Parser;

/**
 * 스프링의 controller 역할
 * controller - service 연결
 */
public class UserController {
    private static final UserService userService = new UserService();

    public static HttpResponseDto signup(HttpRequest request) {
        // query 추출
        String query = Parser.extractQuery(request.getUrl());
        String[] params = Parser.parsing(query, "&", -1);
        String userId = "", password = "", name = "", email = "";

        for (String param : params) {
            String[] p = Parser.parsing(param, "=", 2);
            String key = p[0];
            String value = p[1];
            switch (key) {
                case "userId":
                    userId = value;
                    break;
                case "password":
                    password = value;
                    break;
                case "name":
                    name = value;
                    break;
                case "email":
                    email = value;
                    break;
            }
        }

        UserDto userDto = new UserDto(userId, password, name, email);
        return userService.createUser(userDto);
    }
}

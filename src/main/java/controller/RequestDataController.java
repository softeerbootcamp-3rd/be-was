package controller;

import data.RequestData;
import model.User;
import service.UserService;

public class RequestDataController {
    private static UserService userService;

    static {
        userService = new UserService();
    }

    public static void routeRequest(String url, RequestData requestData) {
        if (url.startsWith("/user")) {
            String remainUrl = url.substring("/user".length());

            if (remainUrl.startsWith("/create")) {
                remainUrl = remainUrl.substring("/create".length()+1);
                userService.registerUser(requestData, remainUrl);
            } else {

            }
        } else {

        }
    }
}

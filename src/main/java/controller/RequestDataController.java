package controller;

import data.RequestData;
import model.User;
import service.UserService;

public class RequestDataController {
    private static UserService userService;

    static {
        userService = new UserService();
    }

    public static String routeRequest(String url, RequestData requestData) {

        if (url.equals("/")) {
            return "/index.html";
        } else if (url.startsWith("/user")) {
            String remainUrl = url.substring("/user".length());

            if (remainUrl.startsWith("/create")) { // 리다이렉트 경로를 파일로 응답할 뿐이지, 브라우저는 여전히 /user를 현재 경로로 갖는다.
                remainUrl = remainUrl.substring("/create".length()+1);
                userService.registerUser(requestData, remainUrl);
                return "/index.html";
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}

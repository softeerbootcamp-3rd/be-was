package controller;

import data.RequestData;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.RequestHandler;

public class RequestDataController {

    private static final Logger logger = LoggerFactory.getLogger(RequestDataController.class);

    private static UserService userService;

    static {
        userService = new UserService();
    }

    public static String routeRequest(RequestData requestData) {
        String url = requestData.getRequestContent();

        if (url.equals("/")) {
            return redirectHome();
        } else if (url.equals("/index.html")) {
            return getFilePath(url);
        } else if (url.equals("/user/form.html")) {
            return getFilePath(url);
        } else if (url.equals("/user/list.html")) {
            return getFilePath(url);
        } else if (url.equals("/user/login.html")) {
            return getFilePath(url);
        } else if (url.equals("/user/profile.html")) {
            return getFilePath(url);
        } else if (url.startsWith("/user/create?")) {
            userService.registerUser(requestData);

            return redirectHome();
        } else {
            logger.debug("파일을 찾을 수 없거나 유효하지 않은 URL입니다.");

            return null;
        }
    }

    private static String redirectHome() {
        return "302 /index.html";
    }

    private static String getFilePath(String filePath) {
        return "200 " + filePath;
    }
}

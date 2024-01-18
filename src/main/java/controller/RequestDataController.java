package controller;

import data.RequestData;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.ResourceLoader;
import webserver.RequestHandler;

import java.io.File;

import static util.RequestParserUtil.getFileExtension;
import static util.ResourceLoader.getResourceType;

public class RequestDataController {

    private static final Logger logger = LoggerFactory.getLogger(RequestDataController.class);

    private static UserService userService;

    static {
        userService = new UserService();
    }

    public static String routeRequest(RequestData requestData) {
        String url = requestData.getRequestContent();

        File file = new File(ResourceLoader.url + "/templates" + url);

        String fileOrApi = getResourceType(url); // URL이 FILE을 나타내는지 API를 나타내는지 문자열로 반환

        if (fileOrApi.equals("FILE")) {
            if (file.exists() && !file.isDirectory()) {
                return getFilePath(url);
            } else {
                logger.debug("유효하지 않은 파일 경로입니다.");
                return null;
            }
        } else if (fileOrApi.equals("API")) {
            if (url.equals("/")) {
                return redirectHome();
            } else if (url.startsWith("/user/create?")) {
                userService.registerUser(requestData);
                return redirectHome();
            } else {
                logger.debug("유효하지 않은 API입니다.");
                return null;
            }
        } else {
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

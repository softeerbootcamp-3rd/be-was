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

        String extension = getFileExtension(url);

        try {
            File file;

            String directory = ResourcePathMapping.getDirectory(extension);

            file = new File(ResourceLoader.url + directory + url);

            String fileOrApi = getResourceType(url); // URL이 FILE을 나타내는지 API를 나타내는지 문자열로 반환

            if (fileOrApi.equals("FILE")) {
                if (file.exists() && !file.isDirectory()) {
                    return getFilePath(url);
                } else {
                    logger.debug("유효하지 않은 파일 경로입니다.");
                    return notFound();
                }
            } else if (fileOrApi.equals("API")) {
                if (url.equals("/")) {
                    return redirectHome();
                } else if (url.startsWith("/user/create")) {
                    userService.registerUser(requestData);
                    return redirectHome();
                } else if (url.equals("/user/login")) {
                    logger.debug("[API] /user/login");
                    UserService.login(requestData);
                    return redirectHome();
                } else {
                    logger.debug("유효하지 않은 API입니다.");
                    return notFound();
                }
            } else {
                return notFound();
            }
        } catch (IllegalArgumentException e) {
            logger.debug("IllegalArgumentException caught: {}", e.getMessage());
            return badRequest();
        }
    }

    private static String redirectHome() {
        return "302 /index.html";
    }

    private static String getFilePath(String filePath) {
        return "200 " + filePath;
    }

    private static String notFound() {
        return "404 /error/notfound.html";
    }

    private static String badRequest() {
        return "400 /error/badrequest.html";
    }
}

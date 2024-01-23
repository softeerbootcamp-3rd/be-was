package controller;

import data.RequestData;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.ResourceLoader;
import webserver.RequestHandler;
import data.Response;

import java.io.File;
import java.util.Locale;

import static util.RequestParserUtil.getFileExtension;
import static util.ResourceLoader.getResourceType;

public class RequestDataController {

    private static final Logger logger = LoggerFactory.getLogger(RequestDataController.class);

    private static UserService userService;

    static {
        userService = new UserService();
    }

    public static Response routeRequest(RequestData requestData) {
        String url = requestData.getRequestContent();

        String extension = getFileExtension(url);

        try {
            String fileOrApi = getResourceType(url); // URL이 FILE을 나타내는지 API를 나타내는지 문자열로 반환

            if (fileOrApi.equals("FILE")) {
                String directory = ResourceMapping.valueOf(extension.toUpperCase()).getDirectory();
                File file = new File(ResourceLoader.url + directory + url);
                if (file.exists() && !file.isDirectory()) {
                    if (url.equals("/user/login.html")) {
                        if (requestData.getHeaderValue("Cookie") != null) {
                            if (UserService.isLoggedIn(requestData)) {
                                return new Response("302", "/index.html");
                            }
                        }
                    }
                    return new Response("200", url);
                } else {
                    logger.debug("유효하지 않은 파일 경로입니다.");
                    return new Response("404", "/error/notfound.html");
                }
            } else if (fileOrApi.equals("API")) {
                if (url.equals("/")) {
                    return new Response("302", "/index.html");
                } else if (url.startsWith("/user/create")) {
                    userService.registerUser(requestData);
                    return new Response("302", "/index.html");
                } else if (url.equals("/user/login")) {
                    logger.debug("[API] /user/login");
                    String sessionId = UserService.login(requestData);
                    if (sessionId != null) {
                        return new Response("302", "/index.html", sessionId);
                    }
                    return new Response("302", "/index.html");
                } else if (url.equals("/user/logout")) {
                    logger.debug("[API] /user/logout");
                    String cookieHeader = UserService.logout(requestData);
                    return new Response("302", "/index.html", cookieHeader);
                } else {
                    logger.debug("유효하지 않은 API입니다.");
                    return new Response("404", "/error/notfound.html");
                }
            } else {
                return new Response("404", "/error/notfound.html");
            }
        } catch (IllegalArgumentException e) {
            logger.debug("IllegalArgumentException caught: {}", e.getMessage());
            return new Response("400", "/error/badrequest.html");
        }
    }
}

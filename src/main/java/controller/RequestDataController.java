package controller;

import data.CookieData;
import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.ResourceLoader;
import data.Response;

import java.io.File;

import static util.RequestParserUtil.getFileExtension;
import static util.ResourceLoader.getResourceType;

public class RequestDataController {

    private static final Logger logger = LoggerFactory.getLogger(RequestDataController.class);

    public static Response routeRequest(RequestData requestData) {
        String url = requestData.getRequestContent();

        String extension = getFileExtension(url);

        try {
            String fileOrApi = getResourceType(url); // URL이 FILE을 나타내는지 API를 나타내는지 문자열로 반환

            if (fileOrApi.equals("FILE")) {
                return handleFileRequest(url, extension, requestData);
            } else if (fileOrApi.equals("API")) {
                return handleApiRequest(url, requestData);
            } else {
                return new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html");
            }
        } catch (IllegalArgumentException e) {
            logger.debug("IllegalArgumentException caught: {}", e.getMessage());
            return new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html");
        }
    }

    private static Response handleFileRequest(String url, String extension, RequestData requestData) {
        String directory = ResourceMapping.valueOf(extension.toUpperCase()).getDirectory();
        File file = new File(ResourceLoader.url + directory + url);
        if (file.exists() && !file.isDirectory()) {
            if (url.equals("/user/login.html")) {
                if (requestData.getHeaderValue("Cookie") != null) {
                    if (UserService.isLoggedIn(requestData)) {
                        return new Response(HttpStatusCode.FOUND, "/index.html");
                    }
                }
            }
            return new Response(HttpStatusCode.OK, url);
        } else {
            logger.debug("유효하지 않은 파일 경로입니다.");
            return new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html");
        }
    }

    private static Response handleApiRequest(String url, RequestData requestData) {
        if (url.equals("/")) {
            return new Response(HttpStatusCode.FOUND, "/index.html");
        } else if (url.startsWith("/user/create")) {
            UserService.registerUser(requestData);
            return new Response(HttpStatusCode.FOUND, "/index.html");
        } else if (url.equals("/user/login")) {
            logger.debug("[API] /user/login");
            return handleLogin(requestData);
        } else if (url.equals("/user/logout")) {
            logger.debug("[API] /user/logout");
            return handleLogout(requestData);
        } else {
            logger.debug("유효하지 않은 API입니다.");
            return new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html");
        }
    }

    private static Response handleLogin(RequestData requestData) {
        CookieData cookieData = UserService.login(requestData);
        if (cookieData != null) {
            return new Response(HttpStatusCode.FOUND, "/index.html", cookieData);
        }
        return new Response(HttpStatusCode.FOUND, "/index.html");
    }

    private static Response handleLogout(RequestData requestData) {
        CookieData cookieData = UserService.logout(requestData);
        return new Response(HttpStatusCode.FOUND, "/index.html", cookieData);
    }
}

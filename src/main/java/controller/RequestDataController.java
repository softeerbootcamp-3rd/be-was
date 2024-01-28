package controller;

import data.CookieData;
import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.MethodMapper;
import util.ResourceLoader;
import data.Response;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static util.RequestParserUtil.getFileExtension;

public class RequestDataController {

    private static final Logger logger = LoggerFactory.getLogger(RequestDataController.class);

    public static Response routeRequest(RequestData requestData) {
        String url = requestData.getRequestContent();

        String extension = getFileExtension(url);

        try {
            Method method = findMethod(requestData.getMethod(), url);

            // 매핑된 메서드가 있으면 호출
            if (method != null) {
                return (Response) method.invoke(null, requestData);
            }

            // 매핑된 메서드가 없으면 파일 요청
            return handleFileRequest(url, extension, requestData);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Error invoking method: {}", e.getMessage());
            return new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html");
        }
    }

    private static Method findMethod(HttpMethod httpMethod, String url) {
        Map<String, Method> routeMap = (httpMethod == HttpMethod.GET) ? MethodMapper.getRouteMap : MethodMapper.postRouteMap;
        return routeMap.get(url);
    }

    private static Response handleFileRequest(String url, String extension, RequestData requestData) {
        try {
            if (extension.isEmpty()) {
                throw new UnsupportedOperationException("유효하지 않은 API 접근입니다.");
            }

            String directory = ResourceMapping.valueOf(extension.toUpperCase()).getDirectory();
            File file = new File(ResourceLoader.url + directory + url);
            if (file.exists() && !file.isDirectory()) {
                return new Response(HttpStatusCode.OK, url);
            } else {
                logger.debug("유효하지 않은 파일 경로입니다.");
                return new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html");
            }
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            logger.debug("유효하지 않은 접근: {}", e.getMessage());
            return new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html");
        }
    }

    @Route(method = HttpMethod.GET, uri = "/")
    public static Response handleApiRedirectToHome(RequestData requestData) {
        return new Response(HttpStatusCode.FOUND, "/index.html");
    }

    @Route(method = HttpMethod.POST, uri = "/user/create")
    public static Response handleApiSignup(RequestData requestData) {
        try {
            UserService.registerUser(requestData);
            return new Response(HttpStatusCode.FOUND, "/index.html");
        } catch (IllegalArgumentException e) {
            logger.debug("회원가입을 위한 파라미터의 수가 부족합니다.");
            return new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html");
        }
    }

    @Route(method = HttpMethod.POST, uri = "/user/login")
    private static Response handleApiLogin(RequestData requestData) {
        CookieData cookieData = UserService.login(requestData);
        if (cookieData != null) {
            return new Response(HttpStatusCode.FOUND, "/index.html", cookieData);
        }
        return new Response(HttpStatusCode.FOUND, "/user/login_failed.html");
    }

    @Route(method = HttpMethod.GET, uri = "/user/logout")
    private static Response handleApiLogout(RequestData requestData) {
        CookieData cookieData = UserService.logout(requestData);
        return new Response(HttpStatusCode.FOUND, "/index.html", cookieData);
    }

    @Route(method = HttpMethod.GET, uri = "/user/list.html")
    private static Response handleFileList(RequestData requestData) {
        if (requestData.isLoggedIn()) {
            return new Response(HttpStatusCode.OK, requestData.getRequestContent());
        }
        return new Response(HttpStatusCode.FOUND, "/index.html");
    }

    @Route(method = HttpMethod.GET, uri = "/user/login.html")
    private static Response handleFileLogin(RequestData requestData) {
        if (requestData.isLoggedIn()) {
            return new Response(HttpStatusCode.FOUND, "/index.html");
        }
        return new Response(HttpStatusCode.OK, requestData.getRequestContent());
    }
}

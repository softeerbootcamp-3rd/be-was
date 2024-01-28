package controller;

import data.CookieData;
import data.RequestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.RequestParserUtil;
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
import static util.ResourceLoader.getResourceType;

public class RequestDataController {

    private static final Logger logger = LoggerFactory.getLogger(RequestDataController.class);

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Route {
        String value();
    }

    private static Map<String, Method> routeMap = new HashMap<>();

    static {
        // 라우팅 메서드 등록
        registerRoutes();
    }

    private static void registerRoutes() {
        Method[] methods = RequestDataController.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Route.class)) {
                Route route = method.getAnnotation(Route.class);
                routeMap.put(route.value(), method);
            }
        }
    }

    public static Response routeRequest(RequestData requestData) {
        String url = requestData.getRequestContent();

        String extension = getFileExtension(url);

        try {
            if (routeMap.containsKey(url)) {
                Method method = routeMap.get(url);
                return (Response) method.invoke(null, requestData);
            } else {
                return handleFileRequest(url, extension, requestData);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Error invoking method: {}", e.getMessage());
            return new Response(HttpStatusCode.NOT_FOUND, "/error/notfound.html");
        }
    }

    private static Response handleFileRequest(String url, String extension, RequestData requestData) {
        try {
            if (extension.isEmpty()) {
                throw new UnsupportedOperationException("유효하지 않은 API 접근입니다.");
            }

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
        } catch (IllegalArgumentException e) {
            logger.debug("유효하지 않은 확장자로 접근!");
            return new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html");
        } catch (UnsupportedOperationException e) {
            logger.error("유효하지 않은 API로 접근!");
            return new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html");
        }
    }

    @Route("/")
    public static Response handleApiRedirectToHome(RequestData requestData) {
        return new Response(HttpStatusCode.FOUND, "/index.html");
    }

    @Route("/user/create")
    public static Response handleApiSignup(RequestData requestData) {
        try {
            UserService.registerUser(requestData);
            return new Response(HttpStatusCode.FOUND, "/index.html");
        } catch (IllegalArgumentException e) {
            logger.debug("회원가입을 위한 파라미터의 수가 부족합니다.");
            return new Response(HttpStatusCode.BAD_REQUEST, "/error/badrequest.html");
        }
    }

    @Route("/user/login")
    private static Response handleApiLogin(RequestData requestData) {
        CookieData cookieData = UserService.login(requestData);
        if (cookieData != null) {
            return new Response(HttpStatusCode.FOUND, "/index.html", cookieData);
        }
        return new Response(HttpStatusCode.FOUND, "/user/login_failed.html");
    }

    @Route("/user/logout")
    private static Response handleApiLogout(RequestData requestData) {
        CookieData cookieData = UserService.logout(requestData);
        return new Response(HttpStatusCode.FOUND, "/index.html", cookieData);
    }
}

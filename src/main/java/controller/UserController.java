package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import model.User;
import request.http.HttpRequest;
import request.user.LoginRequest;
import response.http.HttpResponse;
import service.UserService;
import util.AuthFilter;
import util.Parser;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static db.Session.*;
import static util.Uri.*;
import static util.StatusCode.*;

public class UserController implements Controller {

    private volatile static UserController instance = new UserController();
    private static final Map<String, String> container = new HashMap<>();

    private UserController() {
    }

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        Method[] methods = UserController.class.getDeclaredMethods();
        for (Method m : methods) {
            if (m.isAnnotationPresent(GetMapping.class)) {
                container.put(m.getAnnotation(GetMapping.class).value(), m.getName());
            }
            if (m.isAnnotationPresent(PostMapping.class)) {
                container.put(m.getAnnotation(PostMapping.class).value(), m.getName());
            }
        }

        return instance;
    }

    private final UserService userService = UserService.getInstance();

    @Override
    public HttpResponse handleUserRequest(HttpRequest httpRequest) throws Exception {
        String uri = httpRequest.getUri();
        String filePath = httpRequest.getFilePath(uri);
        String method = httpRequest.getMethod();
        boolean isLogin = AuthFilter.isLogin(httpRequest);

        File file = new File(filePath);

        if (file.exists() && method.equals("GET") && container.containsKey(uri)) {
            Method declaredMethod = this.getClass().getDeclaredMethod(container.get(uri), String.class);
            boolean requiredLogin = declaredMethod.getAnnotation(GetMapping.class).requiredLogin();
            if (requiredLogin && !isLogin) {
                return new HttpResponse(FOUND, "text/html", USER_LOGIN.getUri(), null);
            }
            return (HttpResponse) declaredMethod.invoke(this, filePath);
        }

        if (method.equals("POST") && container.containsKey(uri)) {
            Method declaredMethod = this.getClass().getDeclaredMethod(container.get(uri), HttpRequest.class);
            return (HttpResponse) declaredMethod.invoke(this, httpRequest);
        }
        return new HttpResponse(NOT_FOUND);
    }

    @GetMapping(value = "/user/form.html", requiredLogin = false)
    public HttpResponse getUserForm(String filePath) {
        try {
            return new HttpResponse(OK, filePath);
        } catch (IOException e) {
            return new HttpResponse(NOT_FOUND);
        }
    }

    @GetMapping(value = "/user/form_failed.html", requiredLogin = false)
    public HttpResponse getUserFormFailed(String filePath) {
        try {
            return new HttpResponse(OK, filePath);
        } catch (IOException e) {
            return new HttpResponse(FOUND, "text/html", USER_FORM_FAILED.getUri(), null);
        }
    }

    @GetMapping(value = "/user/list.html", requiredLogin = true)
    public HttpResponse getUserList(String filePath) {
        StringBuilder builder = new StringBuilder();
        List<User> users = userService.findAll();

        builder.append("<tbody>\n");
        for (User user : users) {
            builder.append("<tr>\n");
            builder.append("<th scope=\"row\">").append(user.getUserId()).append("</th>\n");
            builder.append("<td>").append(user.getName()).append("</td>\n");
            builder.append("<td>").append(user.getEmail()).append("</td>\n");
            builder.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n");
            builder.append("<tr>\n");
        }
        builder.append("</tbody>\n");

        try {
            return new HttpResponse(OK, filePath, builder);
        } catch (IOException e) {
            return new HttpResponse(FOUND, "text/html", USER_LOGIN.getUri(), null);
        }
    }

    @GetMapping(value = "/user/login.html", requiredLogin = false)
    public HttpResponse getUserLogin(String filePath) {
        try {
            return new HttpResponse(OK, filePath);
        } catch (IOException e) {
            return new HttpResponse(FOUND, "text/html", USER_LOGIN_FAILED.getUri(), null);
        }
    }

    @GetMapping(value = "/user/login_failed.html", requiredLogin = false)
    public HttpResponse getUserLoginFailed(String filePath) {
        try {
            return new HttpResponse(OK, filePath);
        } catch (IOException e) {
            return new HttpResponse(FOUND, "text/html", USER_LOGIN_FAILED.getUri(), null);
        }
    }

    @PostMapping(value = "/user/create", requiredLogin = false)
    public HttpResponse userCreate(HttpRequest httpRequest) {
        try {
            User user = Parser.jsonParser(User.class, httpRequest.getRequestBodyS());
            userService.join(user);

            return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), null); // 회원가입 성공시 홈으로 이동
        } catch (Exception e) {
            return new HttpResponse(FOUND, "text/html", USER_FORM_FAILED.getUri(), null); // 회원가입 실패시 회원가입 실패 페이지로 이동
        }
    }

    @PostMapping(value = "/user/login", requiredLogin = false)
    public HttpResponse userLogin(HttpRequest httpRequest) {
        try {
            LoginRequest loginRequest = Parser.jsonParser(LoginRequest.class, httpRequest.getRequestBodyS());
            if (!userService.login(loginRequest)) {
                return new HttpResponse(FOUND, "text/html", USER_LOGIN_FAILED.getUri(), null); // 로그인 실패시 로그인 실패 페이지로 이동
            }

            return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), getSessionIdByUserId(loginRequest.getUserId())); // 로그인 성공시 홈으로 이동
        } catch (Exception e) {
            return new HttpResponse(FOUND, "text/html", USER_LOGIN_FAILED.getUri(), null); // 로그인 실패시 로그인 실패 페이지로 이동
        }
    }

    @PostMapping(value = "/user/logout", requiredLogin = true)
    public HttpResponse userLogout(HttpRequest httpRequest) {
        try {
            String cookie = httpRequest.getRequestHeaders().getValue("Cookie");
            for (String cookieValue : cookie.split(";")) {
                if (cookieValue.contains("sid")) {
                    String sessionId = cookieValue.split("=")[1];
                    userService.logout(sessionId);
                    break;
                }
            }

            return new HttpResponse(FOUND, "text/html", HOME_INDEX.getUri(), "EXPIRED"); // 로그아웃 성공시 홈으로 이동
        } catch (Exception e) {
            return new HttpResponse(NOT_FOUND); // 로그아웃 실패시 404
        }
    }
}

package controller;

import annotation.Controller;
import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestMapping;
import db.Database;
import dto.HttpResponseDto;
import dto.UserLoginDto;
import dto.UserSignUpDto;
import model.User;
import model.http.Cookie;
import model.http.Status;
import model.http.request.HttpRequest;
import service.UserService;
import util.FileDetector;
import util.HtmlParser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static config.AppConfig.fileDetector;
import static config.AppConfig.userService;

@Controller
@RequestMapping(value = "/user")
public class UserControllerImpl{
    private static class UserControllerHolder {
        public static final UserControllerImpl INSTANCE = new UserControllerImpl(userService(), fileDetector());
    }

    public static UserControllerImpl getInstance() {
        return UserControllerHolder.INSTANCE;
    }

    private final UserService userService;
    private final FileDetector fileDetector;

    public UserControllerImpl(UserService userService, FileDetector fileDetector) {
        this.userService = userService;
        this.fileDetector = fileDetector;
    }

    @GetMapping(value = "/list.html")
    public void handleUserListRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        HtmlParser htmlParser = new HtmlParser(new String(fileDetector.getFile("/user/list.html")));
        int cnt = 0;
        Collection<User> all = Database.findAll();
        StringBuilder stringBuilder = new StringBuilder();

        for (User user : all) {
            stringBuilder.append("<tr>\n" + "<th scope=\"row\">")
                    .append(++cnt).append("</th> <td>")
                    .append(user.getUserId())
                    .append("</td> <td>").append(user.getName())
                    .append("</td> <td>").append(user.getEmail())
                    .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
        }
        htmlParser.appendContentByTag("tbody", stringBuilder.toString());
        httpResponseDto.setContentType(fileDetector.getContentType(httpRequest.getHeaders().getAccept(), httpRequest.getStartLine().getPathUrl()));
        httpResponseDto.setContent(htmlParser.getHtml().getBytes());
    }

    @PostMapping(value = "/login")
    public void handleUserLoginRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {

        Optional<UUID> sessionId = userService.login(UserLoginDto.fromRequestBody(httpRequest.getBody().getContent()));

        HashMap<String, String> map = new HashMap<>();
        map.put("Path", "/");
        if (sessionId.isPresent()) {
            Cookie cookie = new Cookie("sid", sessionId.get().toString(), map);
            httpResponseDto.addHeader("Set-Cookie", cookie.getCookieList());
            redirectToPath(httpResponseDto, "/index.html");
        }
        if (sessionId.isEmpty()) {
            redirectToPath(httpResponseDto, "/user/login_failed.html");
        }

    }

    @PostMapping(value = "/create")
    public void handleUserCreateRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        UserSignUpDto userSignUpDto = UserSignUpDto.fromRequestBody(httpRequest.getBody().getContent());
        userService.signUp(userSignUpDto);
        redirectToPath(httpResponseDto, "/index.html");
    }

    @GetMapping(value = "/logout")
    public void handleUserLogoutRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        String sessionId = httpRequest.getHeaders().getUserSessionId();
        userService.logout(UUID.fromString(sessionId));
        redirectToPath(httpResponseDto, "/index.html");
    }

    private void redirectToPath(HttpResponseDto httpResponseDto, String path) {
        httpResponseDto.setStatus(Status.REDIRECT);
        httpResponseDto.addHeader("Location", path);
    }
}

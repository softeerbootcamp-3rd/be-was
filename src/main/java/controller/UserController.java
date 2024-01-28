package controller;

import constant.HttpHeader;
import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HtmlBuilder;
import util.HttpResponseUtil;
import util.SessionUtil;
import util.WebUtil;

import java.util.Map;

public class UserController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        if (request.getMethod().equalsIgnoreCase("POST") && request.getUri().startsWith("/user/create")) {
            return createUser(request);
        }
        if (request.getMethod().equalsIgnoreCase("POST") && request.getUri().startsWith("/user/login")) {
            return loginUser(request);
        }
        if (request.getMethod().equalsIgnoreCase("GET") && request.getUri().startsWith("/user/list")) {
            return printUserList(request);
        }

        return HttpResponseUtil.loadResource(request, logger);
    }

    public HttpResponseDto createUser(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();

        try {
            Map<String, String> parameters = WebUtil.parseRequestBody(request.getBody());
            userService.createUser(parameters);

            return responseDtoBuilder.response302Header()
                    .setHeaders(HttpHeader.LOCATION, "/index.html").build();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());

            return responseDtoBuilder.response400Header().build();
        }
    }

    public HttpResponseDto loginUser(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();

        try {
            Map<String, String> parameters = WebUtil.parseRequestBody(request.getBody());
            String sessionId = userService.loginUser(parameters);

            return responseDtoBuilder.response302Header()
                    .setHeaders(HttpHeader.LOCATION, "/index.html")
                    .setCookie(SessionUtil.SESSION_ID, sessionId, new String[]{"Path=/"})
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());

            return responseDtoBuilder.response302Header()
                    .setHeaders(HttpHeader.LOCATION, "/user/login_failed.html").build();
        }
    }

    public HttpResponseDto printUserList(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();

        if (request.getUser() != null) {
            // 로그인 한 경우
            byte[] body = HtmlBuilder.buildUserListPage(request);

            return responseDtoBuilder.response200Header()
                    .setHeaders(HttpHeader.CONTENT_TYPE, WebUtil.getContentType(request.getUri()) + ";charset=utf-8")
                    .setHeaders(HttpHeader.CONTENT_LENGTH, Integer.toString(body.length))
                    .setBody(body)
                    .build();
        }

        // 로그인하지 않은 경우
        return responseDtoBuilder.response302Header()
                .setHeaders(HttpHeader.LOCATION, "/user/login.html").build();
    }
}

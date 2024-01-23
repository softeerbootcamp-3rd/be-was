package controller;

import dto.HttpRequestDto;
import dto.HttpResponseDto;
import dto.HttpResponseDtoBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpResponseUtil;
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
        if (request.getMethod().equals("POST") && request.getUri().startsWith("/user/create")) {
            return createUser(request);
        }
        if (request.getMethod().equals("POST") && request.getUri().startsWith("/user/login")) {
            return loginUser(request);
        }
        return getPage(request, logger);
    }

    public HttpResponseDto createUser(HttpRequestDto request) {
        try {
            Map<String, String> parameters = WebUtil.parseRequestBody(request.getBody());
            userService.createUser(parameters);

            return HttpResponseUtil.response302("/index.html");
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());

            return HttpResponseUtil.response400();
        }
    }

    public HttpResponseDto loginUser(HttpRequestDto request) {
        HttpResponseDtoBuilder responseDtoBuilder = new HttpResponseDtoBuilder();
        try {
            Map<String, String> parameters = WebUtil.parseRequestBody(request.getBody());
            String sessionId = userService.loginUser(parameters);

            return responseDtoBuilder.response302Header()
                    .setHeaders("Location", "/index.html")
                    .setCookie("sid", sessionId, new String[]{"Path=/"})
                    .build();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());

            return responseDtoBuilder.response302Header()
                    .setHeaders("Location", "/user/login_failed.html").build();
        }
    }
}

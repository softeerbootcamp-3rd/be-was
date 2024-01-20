package controller;

import dto.HttpRequestDto;
import dto.HttpResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpResponseUtil;
import util.WebUtil;

import java.util.Map;

public class UserController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final UserService userService = new UserService();

    @Override
    public HttpResponseDto handleRequest(HttpRequestDto request) {
        if (request.getMethod().equals("GET") && request.getUri().startsWith("/user/create")) {
            return createUser(request);
        }
        return getPage(request, logger);
    }

    public HttpResponseDto createUser(HttpRequestDto request) {
        try {
            Map<String, String> parameters = WebUtil.parseQueryString(request.getUri());
            userService.createUser(parameters);
            return HttpResponseUtil.response302("/index.html");
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return HttpResponseUtil.response400();
        }
    }
}

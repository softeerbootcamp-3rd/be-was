package controller;

import dto.HttpRequestDto;
import dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpResponseUtil;
import util.WebUtil;

import java.io.DataOutputStream;
import java.util.Map;

public class UserController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final UserService userService = new UserService();

    @Override
    public void handleRequest(HttpRequestDto request, DataOutputStream dos) {
        if (request.getMethod().equals("GET") && request.getUri().startsWith("/user/create")) {
            createUser(request, dos);
        }
        getPage(request, dos, logger);
    }

    public void createUser(HttpRequestDto request, DataOutputStream dos) {
        try {
            Map<String, String> parameters = WebUtil.parseQueryString(request.getUri());
            userService.createUser(UserDto.buildUserDtoFromParams(parameters));
            HttpResponseUtil.response302Header(dos, "/index.html");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}

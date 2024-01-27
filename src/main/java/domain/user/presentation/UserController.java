package domain.user.presentation;

import common.annotation.RequestBody;
import common.annotation.RequestMapping;
import common.http.request.HttpMethod;
import common.http.request.HttpRequest;
import common.http.response.HttpStatusCode;
import common.utils.CookieParser;
import common.utils.ResponseUtils;
import domain.user.command.application.UserCreateRequest;
import domain.user.command.application.UserCreateService;
import domain.user.command.application.UserLoginService;
import domain.user.query.application.UserSearchService;
import java.util.Optional;
import webserver.container.CustomThreadLocal;

public class UserController {
    private UserCreateService userCreateService;
    private UserLoginService userLoginService;
    private UserSearchService userSearchService;

    public UserController() {
        userCreateService = new UserCreateService();
        userLoginService = new UserLoginService();
        userSearchService = new UserSearchService();
    }

    @RequestMapping(method = HttpMethod.POST, path = "/user/create")
    public void signup
        (
            @RequestBody UserCreateRequest userCreateRequest
        )
    {
        if (RequestValidator.userCreateRequestValidate(userCreateRequest)) {
            return;
        }
        userCreateService.makeNewUser(userCreateRequest);
    }

    @RequestMapping(method = HttpMethod.POST, path = "/user/login")
    public void login
        (
            @RequestBody UserLoginRequest userLoginRequest
        )
    {
        if (RequestValidator.userLoginRequestValidate(userLoginRequest)) {
            return;
        }
        userLoginService.login(userLoginRequest);
    }

    @RequestMapping(method = HttpMethod.GET, path = "/user/my-info")
    public void getMyInfo()
    {
        HttpRequest httpRequest = CustomThreadLocal.getHttpRequest();
        Optional<String> sessionId = CookieParser.parseSidFromHeader(httpRequest);
        if (sessionId.isEmpty()) {
            CustomThreadLocal.onFailure(HttpStatusCode.FOUND, ResponseUtils.makeRedirection("/user/login.html"), new byte[0]);
            return;
        }

        userSearchService.getMyUserInfo(sessionId.get());
    }

    @RequestMapping(method = HttpMethod.GET, path = "/user/list")
    public void getLoginUserInfos()
    {
        HttpRequest httpRequest = CustomThreadLocal.getHttpRequest();
        Optional<String> sessionId = CookieParser.parseSidFromHeader(httpRequest);
        if (sessionId.isEmpty()) {
            CustomThreadLocal.onFailure(HttpStatusCode.FOUND, ResponseUtils.makeRedirection("/user/login.html"), new byte[0]);
            return;
        }

        userSearchService.getSignupUserInfos();
    }
}

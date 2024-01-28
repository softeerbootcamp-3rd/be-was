package domain.user.presentation;

import common.annotation.RequestBody;
import common.annotation.RequestMapping;
import common.http.request.HttpMethod;
import domain.user.command.application.UserCreateRequest;
import domain.user.command.application.UserCreateService;
import domain.user.command.application.UserLoginService;

public class UserController {
    private UserCreateService userCreateService;
    private UserLoginService userLoginService;

    public UserController() {
        userCreateService = new UserCreateService();
        userLoginService = new UserLoginService();
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
}

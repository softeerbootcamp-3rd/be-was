package domain.user.presentation;

import common.annotation.RequestBody;
import common.annotation.RequestMapping;
import common.http.request.HttpMethod;
import domain.user.command.application.UserCreateRequest;
import domain.user.command.application.UserCreateService;

public class UserController {
    private UserCreateService userCreateService = new UserCreateService();

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
}

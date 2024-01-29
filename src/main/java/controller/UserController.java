package controller;

import dto.ResourceDto;
import model.Model;
import util.QueryParams;
import service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class UserController {
    private final UserService userService = new UserService();
    private Map<String, Function<Object, ResourceDto>> methodMap =  new HashMap<>();

    {
        methodMap.put("/user/create", this::generateUserResource);
        methodMap.put("/index.html", this::process);
        methodMap.put("/user/form.html", this::process);
        methodMap.put("/user/login.html", this::process);
        methodMap.put("/user/login", this::loginUserResource);
        methodMap.put("/user/login_failed.html", this::process);

    }

    public ResourceDto loginUserResource(Object bodyData) {
        String sessionId = userService.loginUser((QueryParams) bodyData);
        if (sessionId == null) {
            return ResourceDto.of("/user/login_failed.html", 302);
        }
        Model.addAttribute("sessionId", sessionId);
        return ResourceDto.of("/index.html", 302);
    }

    public ResourceDto generateUserResource(Object queryParams) {
        userService.createUser((QueryParams) queryParams);
        return ResourceDto.of("/index.html", 302);
    }

    public ResourceDto process(Object path) {
        return ResourceDto.of((String) path);
    }

    public Function<Object, ResourceDto> getCorrectMethod(String path) {
        for (String key : methodMap.keySet()) {
            if (path.matches(key)) {
                return methodMap.get(key);
            }
        }
        return null;
    }
}

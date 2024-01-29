package controller;

import dto.ResourceDto;
import model.Model;
import model.User;
import util.ParseParams;
import service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class UserController {
    private final UserService userService = new UserService();
    private Map<String, BiFunction<String, Object, ResourceDto>> methodMap =  new HashMap<>();

    {
        methodMap.put("/user/create", this::generateUserResource);
        methodMap.put("/user/list.html", this::generateUserListResource);
        methodMap.put("/index.html", this::process);
        methodMap.put("/user/form.html", this::process);
        methodMap.put("/user/login.html", this::process);
        methodMap.put("/user/login", this::loginUserResource);
        methodMap.put("/user/login_failed.html", this::process);
    }

    public ResourceDto generateUserListResource(String session, Object path) {
        if (session == null) {
            return ResourceDto.of("/user/login.html", 302, false);
        }
        List<User> userList = userService.findAllUser();
        Model.addAttribute("userList", userList);
        return ResourceDto.of((String) path);
    }

    public ResourceDto loginUserResource(String session, Object bodyData) {
        String sessionId = userService.loginUser((ParseParams) bodyData);
        if (sessionId == null) {
            return ResourceDto.of("/user/login_failed.html", 302, false);
        }

        User user = userService.findUserWithSession(sessionId);
        Model.addAttribute("sessionId", sessionId);
        Model.addAttribute("username", user.getName());
        return ResourceDto.of("/index.html", 302);
    }

    public ResourceDto generateUserResource(String session, Object queryParams) {
        userService.createUser((ParseParams) queryParams);
        return ResourceDto.of("/index.html", 302, false);
    }

    public ResourceDto process(String session, Object path) {
        if (session == null) {
            return ResourceDto.of((String) path, false);
        }
        return ResourceDto.of((String) path);
    }

    public BiFunction<String, Object, ResourceDto> getCorrectMethod(String path) {
        for (String key : methodMap.keySet()) {
            if (path.matches(key)) {
                return methodMap.get(key);
            }
        }
        return null;
    }
}

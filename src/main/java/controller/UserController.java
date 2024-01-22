package controller;

import dto.ResourceDto;
import model.QueryParams;
import service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UserController {
    private final UserService userService = new UserService();
    private Map<String, Function<Object, ResourceDto>> methodMap =  new HashMap<>();

    {
        methodMap.put("/user/create", this::generateUserResource);
        methodMap.put("/index.html", this::process);
        methodMap.put("/user/form.html", this::process);
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
            if (path.startsWith(key)) {
                return methodMap.get(key);
            }
        }
        return null;
    }


}

package webserver;

import controller.UserController;
import dto.ResourceDto;
import util.ParseParams;

import java.util.function.BiFunction;

public class PathHandler {

    public static ResourceDto responseResource(RequestHeader requestHeader, String body, UserController controller) {
        String method = requestHeader.getMethod();
        String path = requestHeader.getPath();
        String cookie = requestHeader.getCookie();


        if (controller != null) {
            BiFunction<String, Object, ResourceDto> correctMethod = controller.getCorrectMethod(path);
            if (method.equals("POST")) {
                ParseParams queryParam = ParseParams.from(body);
                return correctMethod.apply(cookie, queryParam);
            }
            else if (method.equals("GET") && path.contains("?")) {
                String[] splits = path.split("\\?");
                ParseParams parseParams = ParseParams.from(splits[1]);
                return correctMethod.apply(cookie, parseParams);
            } else if (method.equals("GET")) {
                return correctMethod.apply(cookie, path);
            }
        }

        return ResourceDto.of(path);
    }
}
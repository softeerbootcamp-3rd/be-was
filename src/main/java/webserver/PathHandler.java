package webserver;

import controller.UserController;
import dto.ResourceDto;
import util.ParseParams;

import java.util.function.Function;

public class PathHandler {

    public static ResourceDto responseResource(String method, String path, String body, UserController controller) {
        if (controller != null) {
            Function<Object, ResourceDto> correctMethod = controller.getCorrectMethod(path);
            if (method.equals("POST")) {
                ParseParams queryParam = ParseParams.from(body);
                return correctMethod.apply(queryParam);
            }
            else if (method.equals("GET") && path.contains("?")) {
                String[] splits = path.split("\\?");
                ParseParams parseParams = ParseParams.from(splits[1]);
                return correctMethod.apply(parseParams);
            } else if (method.equals("GET")) {
                return correctMethod.apply(path);
            }
        }

        return ResourceDto.of(path);
    }
}
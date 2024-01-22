package webserver;

import controller.UserController;
import dto.ResourceDto;
import model.QueryParams;

import java.util.function.Function;

public class PathHandler {

    public static ResourceDto responseResource(String method, String path, UserController controller) {
        if (controller != null) {
            Function<Object, ResourceDto> correctMethod = controller.getCorrectMethod(path);
            if (method.equals("GET") && path.contains("?")) {
                String[] splits = path.split("\\?");
                QueryParams queryParams = QueryParams.from(splits[1]);
                return correctMethod.apply(queryParams);
            } else if (method.equals("GET")) {
                return correctMethod.apply(path);
            }
        }

        return ResourceDto.of(path);
    }
}
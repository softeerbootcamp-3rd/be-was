package webserver;

import controller.UserController;
import dto.ResourceDto;
import model.QueryParams;
import util.ResourceHandler;

import java.io.IOException;

public class PathHandler {

    public static byte[] responseResource(String method, String path, UserController controller) throws IOException {
        if (controller != null) {
            if (method.equals("GET") && path.contains("?")) {
                String[] splits = path.split("\\?");
                QueryParams queryParams = QueryParams.from(splits[1]);
                return ResourceHandler.resolveResource(controller.process(queryParams));
            } else if (method.equals("GET")) {
                return ResourceHandler.resolveResource(controller.process(path));
            }
        }

        return ResourceHandler.resolveResource(ResourceDto.of(path));
    }
}

package webserver;

import controller.UserController;
import dto.ResourceDto;
import model.QueryParams;

import java.io.IOException;

public class PathHandler {

    public static ResourceDto responseResource(String method, String path, UserController controller) throws IOException {
        if (controller != null) {
            if (method.equals("GET") && path.contains("?")) {
                String[] splits = path.split("\\?");
                QueryParams queryParams = QueryParams.from(splits[1]);
                return controller.process(queryParams);
            } else if (method.equals("GET")) {
                return controller.process(path);
            }
        }

        return ResourceDto.of(path);
    }
}
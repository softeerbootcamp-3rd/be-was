package webserver;

import controller.UserController;
import model.QueryParams;

import java.net.URL;

public class PathHandler {

    public static URL responseResource(String method, String path, UserController controller) {
        if (controller != null) {
            if (method.equals("GET") && path.contains("?")) {
                String[] splits = path.split("\\?");
                QueryParams queryParams = QueryParams.from(splits[1]);
                controller.process(queryParams);
            } else if (method.equals("GET") && path.contains(".html")) {
                return ClassLoader.getSystemClassLoader().getResource("./templates" + path);
            }
        }

        return ClassLoader.getSystemClassLoader().getResource("./static" + path);
    }
}

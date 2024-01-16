package webserver;

import Controller.UserController;

public class ServletContainer {
    public static Object find(String method, String path, String params) {
        String[] pathInfo = path.substring(1).split("/", 2);

        if (pathInfo[0].equals("user")) {
            return UserController.service(method, pathInfo[1], params);
        }

        return null;
    }
}

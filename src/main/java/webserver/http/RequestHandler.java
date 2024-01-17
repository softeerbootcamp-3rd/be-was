package webserver.http;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.UserFormDataParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final Map<String, Consumer<Request>> routeHandlers;

    public RequestHandler() {
        this.routeHandlers = new HashMap<>();
        initializeRoutes();
    }

    private void initializeRoutes() {
        routeHandlers.put("/user/create", this::handleUserCreate);
    }

    public void handleRequest(Request request) {
        String requestTarget = request.getRequestTarget().split("\\?")[0];
        if (routeHandlers.containsKey(requestTarget)) {
            routeHandlers.get(requestTarget).accept(request);
        } else {
            handleNotFound(request);
        }
    }

    private void handleUserCreate(Request request) {
        String data = request.getRequestTarget().split("\\?")[1];
        UserFormDataParser userFormDataParser = new UserFormDataParser(data);
        HashMap<String,String> formData = userFormDataParser.ParseData();
        User user = new User(formData.get("userId"), formData.get("password"), formData.get("name"), formData.get("email") );
        System.out.println(user.toString());
    }

    private void handleNotFound(Request request) {
        logger.error("request : NOT FOUND");
    }
}
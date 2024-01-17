package webserver.http;

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
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value);
        }
    }

    private void handleNotFound(Request request) {
        logger.error("request : NOT FOUND");
    }
}
package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class DynamicResourceHandler {
    private final Map<String, Consumer<Response>> resourceHandlers = new HashMap<>();

    public DynamicResourceHandler() {
        resourceHandlers.put("/index.html", this::indexFunction);
    }

    private void indexFunction(Response response) {
        byte[] responseBody = response.getResponseBody();
        String responseContent = new String(responseBody);
        System.out.println(responseContent);
        response.setResponseBody(responseBody);
    }

    public void handle(Response response) {
        if (resourceHandlers.containsKey(response.getRequestTarget())) {
            resourceHandlers.get(response.getRequestTarget()).accept(response);
        }
    }
}
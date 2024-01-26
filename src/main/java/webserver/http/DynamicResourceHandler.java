package webserver.http;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DynamicResourceHandler {
    private final Map<String, Function<byte [], byte[]>> resourceHandlers = new HashMap<>();

    public DynamicResourceHandler() {
        resourceHandlers.put("/index.html", this::indexFunction);
    }

    private byte[] indexFunction(byte[] s) {
        return s;
    }

    public byte[] handle(String requestTarget, byte[] responseBody) {
        Function<byte[], byte[]> handler = resourceHandlers.get(requestTarget);
        if (handler != null) {
            return handler.apply(responseBody);
        }

        return responseBody;
    }
}
package request;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String url;
    private String version;
    private Map<String, String> headers = new HashMap<>();
    private String body;
}

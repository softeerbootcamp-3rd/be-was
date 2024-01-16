package model;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String target;
    private String version;

    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> body = new HashMap<>();

    public HttpRequest(String method, String target, String version) {
        this.method = method;
        this.target = target;
        this.version = version;
    }

    public void updateHeaders(String key, String value) {
        headers.put(key, value);
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }

    @Override
    public String toString() {
        if(method.equals("POST")) {
            return "\n[HttpRequest]\n" +
                    "- 일반" +
                    "\n요청 URL: " + target +
                    "\n요청 메서드: " + method +
                    (headers.containsKey("Referer") ? "\n원격 주소: " + headers.get("Referer") + "\n" : "\n") +
                    "\n- 요청 헤더" +
                    "\n" + mapToString(headers) + "\n" +
                    "\nbodys:\n" + mapToString(body);
        }
        return "\n[HttpRequest]\n" +
                "- 일반" +
                "\n요청 URL: " + target +
                "\n요청 메서드: " + method +
                (headers.containsKey("Referer") ? "\n원격 주소: " + headers.get("Referer") + "\n" : "\n") +
                "\n- 요청 헤더" +
                "\n" + mapToString(headers);
    }

    private String mapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey())
                .append(" = ")
                .append(entry.getValue())
                .append("\n");
        }

        return sb.toString();
    }
}
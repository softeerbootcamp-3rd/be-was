package dto;

import java.util.HashMap;
import java.util.Map;

public class RequestDto {
    private String method;
    private String path;
    private String methodAndPath;
    private Map<String, String> params;
    private Map<String, String> headers;
    private Map<String, String> body;

    public RequestDto(String methodAndPath) {
        String[] arr = methodAndPath.split(" ");
        this.method = arr[0];
        this.path = arr[1];
        this.methodAndPath = methodAndPath;
        this.params = new HashMap<>();
        this.headers = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethodAndPath() {
        return methodAndPath;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setBody(Map<String, String> body) {
        this.body = params;
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public Integer getContentLength() {
        String len;
        if ((len = this.headers.get("Content-Length")) != null) {
            return Integer.parseInt(len);
        }
        return  null;
    }

    public String headersToString() {
        return  "\n-------------------------------------\n" +
                ("http method: " + this.method + "\n") +
                ("path: " + this.path + "\n") +
                ("Host: " + headers.get("Host") + "\n") +
                ("Accept: " + headers.get("Accept") + "\n") +
                "-------------------------------------\n";
    }
}

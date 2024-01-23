package dto;

import java.util.HashMap;
import java.util.Map;

public class RequestDto {
    private String method;
    private String path;
    private String methodAndPath;
    private Map<String, String> params;
    private Map<String, String> headers;

    public RequestDto(String methodAndpath) {
        String[] MAndP = methodAndpath.split(" ");
        this.method = MAndP[0];
        this.path = MAndP[1];
        this.methodAndPath = methodAndpath;
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

    public void addParam(String paramKey, String paramValue) {
        this.params.put(paramKey, paramValue);
    }

    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    public String headersToString() {
        return  "\n-------------------------------------\n" +
                ("http method: " + this.method + "\n") +
                ("path: " + this.path + "\n") +
                ("Host: " + headers.get("Host") + "\n") +
                "-------------------------------------\n";
    }
}

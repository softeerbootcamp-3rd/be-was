package model;

import java.util.HashMap;
import java.util.Map;

public class RequestDto {
    private String method;
    private String path;
    private String params;
    private Map<String, String> header;

    public RequestDto(String method, String path, String params) {
        this.method = method;
        this.path = path;
        this.params = params;
        this.header = new HashMap<>();
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String  getParams() { return params; }

    public void addHeader(String key, String value) {
        this.header.put(key, value);
    }

    public String getHeader() {
        return  "\n-------------------------------------\n" +
                ("http method: " + this.method + "\n") +
                ("path: " + this.path + "\n") +
                ("Host: " + header.get("Host") + "\n") +
                "-------------------------------------\n";
    }
}

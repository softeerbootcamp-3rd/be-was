package config;

import java.util.HashMap;

public class HTTPResponse {
    private String HTTPType;
    private int code;
    private String status;
    private HashMap<String,String> head;
    private HashMap<String,String> body;

    public HTTPResponse(String HTTPType, int code, String status, HashMap<String, String> head, HashMap<String, String> body) {
        this.HTTPType = HTTPType;
        this.code = code;
        this.status = status;
        this.head = head;
        this.body = body;
    }
}

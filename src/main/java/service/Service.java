package service;

import http.request.HttpMethod;
import java.util.Map;

public class Service {

    public void validate(HttpMethod method, Map<String, String> params, Map<String, String> body) {
    }

    public byte[] execute(HttpMethod method, Map<String, String> params, Map<String, String> body) {
        return "hello".getBytes();
    }

}

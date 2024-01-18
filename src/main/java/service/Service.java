package service;

import java.util.Map;

public class Service {
    public void validate(String method, Map<String, String> params, Map<String, String> body) {
    }
    public byte[] execute(String method, Map<String, String> params, Map<String, String> body) {
        return "hello".getBytes();
    }

}

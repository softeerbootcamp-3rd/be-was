package webserver;

import dto.HttpResponse;

import java.io.*;
import java.util.Map;

public class ResponseHandler {

    public static void send(DataOutputStream dos, HttpResponse response) throws IOException {
        //헤더 전송
        dos.writeBytes("HTTP/1.1 " + response.getStatus().toString() + "\r\n");
        Map<String, String> headers = response.getHeaders();
        for (String key : headers.keySet()) {
            dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
        }

        dos.writeBytes("\r\n");

        //바디 전송
        if (response.getBody() != null) {
            dos.write(response.getBody(), 0, response.getBody().length);
            dos.flush();
        }
    }
}

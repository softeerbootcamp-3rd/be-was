package model;

import db.SessionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private static final HashMap<String, String> statusCodeMessage = new HashMap<>() {{
        put("200", "OK"); put("302", "Found"); put("404", "Not Found"); }};
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private String statusCode;
    private byte[] body;
    private Map<String, String> headers = new HashMap<>();

    public void setStatusCode(String statusCode) {this.statusCode = statusCode;}
    public void setBody(byte[] body) {this.body = body;}
    public void addHeader(String key, String value) {this.headers.put(key, value);}

    public Response() {}

    public void write(DataOutputStream dos) {
        handleResponseHeader(dos);
        if(this.body != null)
            handleResponseBody(dos);
    }
    private void handleResponseHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusCodeMessage.get(statusCode) + " \r\n");
            for(String key : headers.keySet()) {
                dos.writeBytes(key + ": " + headers.get(key) + "\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void handleResponseBody(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
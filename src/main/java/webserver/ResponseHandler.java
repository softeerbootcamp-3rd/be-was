package webserver;

import model.Request;
import model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.util.HashMap;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private static final HashMap<String, String> statusCodeMessage = new HashMap<>() {{
        put("200", "OK"); put("302", "Found"); put("404", "Not Found"); }};

    private DataOutputStream dos;

    public ResponseHandler(DataOutputStream dataOutputStream) {
        this.dos = dataOutputStream;
    }

    public void handleResponse(Response response) {
        handleResponseHeader(response);
        if(response.getBody() != null)
            handleResponseBody(response.getBody());
    }
    private void handleResponseHeader(Response response) {
        String statusCode = response.getStatusCode();
        try {
            this.dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusCodeMessage.get(statusCode) + " \r\n");
            if(response.getStatusCode().equals("200")) {
                this.dos.writeBytes("Content-Type: " + response.getMimeType() + ";charset=utf-8\r\n");
                this.dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
            }
            else if(response.getStatusCode().equals("302")) {
                if(response.getCookie() != null)
                    this.dos.writeBytes("Set-Cookie: " + "sessionId=" + response.getCookie() + "; Path=/; Max-Age=3600\r\n");
                this.dos.writeBytes("Location: " + response.getRedirectUrl());
            }
            else if(response.getStatusCode().equals("404")) {

            }
//            if(response.getCookie() != null) {
//                this.dos.writeBytes("Set-Cookie: " + "sessionId=" + response.getCookie() + "; Path=/; Max-Age=3600\r\n");
//                this.dos.writeBytes("Location: " + response.getRedirectUrl());
//            }
//            else if(response.getBody() != null) {
//                this.dos.writeBytes("Content-Type: " + response.getMimeType() + ";charset=utf-8\r\n");
//                this.dos.writeBytes("Content-Length: " + response.getBody().length + "\r\n");
//            }
//            else if(response.getRedirectUrl() != null){
//                this.dos.writeBytes("Location: " + response.getRedirectUrl());
//            }
            this.dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    private void handleResponseBody(byte[] body) {
        try {
            this.dos.write(body, 0, body.length);
            this.dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

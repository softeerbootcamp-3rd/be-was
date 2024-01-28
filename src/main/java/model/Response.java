package model;

import db.SessionStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Response {
    private static final HashMap<String, String> statusCodeMessage = new HashMap<>() {{
        put("200", "OK"); put("302", "Found"); put("404", "Not Found"); }};
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private String statusCode;
    private byte[] body;
    private String mimeType;
    private String redirectUrl;
    private String cookie;

    public String getStatusCode() {return this.statusCode;}
    public byte[] getBody() {return this.body;}
    public String getMimeType() {return this.mimeType;}
    public String getRedirectUrl() {return this.redirectUrl;}
    public String getCookie() {return this.cookie;}
    public void setStatusCode(String statusCode) {this.statusCode = statusCode;}
    public void setBody(byte[] body) {this.body = body;}
    public void setMimeType(String mimeType) {this.mimeType = mimeType;}
    public void setRedirectUrl(String redirectUrl) {this.redirectUrl = redirectUrl;}
    public void setCookie(String cookie) {this.cookie = cookie;}

    public Response() {}
    public Response(String statusCode, byte[] body) {
        this.statusCode = statusCode;
        this.body = body;
    }
    public Response(String statusCode, String mimeType) {
        this.statusCode = statusCode;
        this.mimeType = mimeType;
    }
    public Response(String statusCode, byte[] body, String mimeType, String redirectUrl, String cookie) {
        this.statusCode = statusCode;
        this.body = body;
        this.mimeType = mimeType;
        this.redirectUrl = redirectUrl;
        this.cookie = cookie;
    }
    public void write(DataOutputStream dos) {
        handleResponseHeader(dos);
        if(this.body != null)
            handleResponseBody(dos);
    }
    private void handleResponseHeader(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 " + statusCode + " " + statusCodeMessage.get(statusCode) + " \r\n");
            if(cookie != null) {
                dos.writeBytes("Set-Cookie: " + "sessionId=" + cookie + "; Path=/; Max-Age=" + SessionStorage.SESSION_TIME + "\r\n");
                dos.writeBytes("Location: " + redirectUrl);
            }
            else
            if(body != null) {
                dos.writeBytes("Content-Type: " + mimeType + ";charset=utf-8\r\n");
                dos.writeBytes("Content-Length: " + body.length + "\r\n");
            }
            else if(redirectUrl != null){
                dos.writeBytes("Location: " + redirectUrl);
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
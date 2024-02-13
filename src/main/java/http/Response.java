package http;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Response {
    private static final Logger logger = LoggerFactory.getLogger(Response.class);
    private DataOutputStream dos;
    private String location;
    private HttpStatus status = HttpStatus.OK;
    private String contentType = "";

    private ArrayList<Cookie> cookie = new ArrayList<>();
    //constructors
    public Response(DataOutputStream dos){
        this.dos = dos;
    }
    //getter
    public String getLocation() {
        return location;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public String getContentType(){return this.contentType;}

    //setter
    public void setLocation(String location) {
        this.location = location;
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    public void setContentType(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            String fileExtension = fileName.substring(dotIndex + 1);
            this.contentType = HttpContentType.getValue(fileExtension);
        }
    }

    //methods
    public void send(Request req){
        byte[] body = new byte[0];
        setResponseHeader(body.length,req);
        setResponseBody(body);
    }
    public void send(byte[] body, Request req){
        setResponseHeader(body.length,req);
        setResponseBody(body);
    }
    private void setResponseHeader(int lengthOfBodyContent, Request req) {
        try {
            dos.writeBytes(new StringBuilder("HTTP/1.1 ").append(status.toString()).append("\r\n").toString());
            dos.writeBytes(new StringBuilder(getContentTypeString(req)).toString());
            dos.writeBytes("charset=utf-8\r\n");
            dos.writeBytes(new StringBuilder("Content-Length: ").append(lengthOfBodyContent).append("\r\n").toString());
            dos.writeBytes(new StringBuilder("Location : ").append(location).append("\r\n").toString());
            dos.writeBytes("Set-Cookie: "+getCookieString()+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(String.valueOf(e.getCause()));
            e.printStackTrace();
        }

    }

    private void setResponseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(String.valueOf(e.getCause()));
        }
    }

    private String getContentTypeString(Request req){
        if(req.getRequestParam().containsKey("content")) {
            setContentType(req.getRequestParam().get("content"));
            return new StringBuilder("Content-Type: ").append(contentType).append(";").toString();
        }
        return "";
    }

    public String getCookieString(){
        StringBuilder cookieStringBuilder = new StringBuilder();

        for (Cookie c : cookie) {
            cookieStringBuilder.append(c.getKey())
                    .append("=")
                    .append(c.getValue())
                    .append("; ");
        }

        if (cookieStringBuilder.length() > 0) {
            cookieStringBuilder.setLength(cookieStringBuilder.length() - 2);
        }

        return cookieStringBuilder.toString();
    }
    public void addCookie(Cookie cookie){
        this.cookie.add(cookie);
    }

}
